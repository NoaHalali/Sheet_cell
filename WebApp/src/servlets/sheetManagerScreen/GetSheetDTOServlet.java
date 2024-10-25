package servlets.sheetManagerScreen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.PermissionType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static constant.Constants.*;

@WebServlet("/getSheetDTO")
public class GetSheetDTOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        Gson gson = new Gson();
        try (PrintWriter out = response.getWriter()) {
            String sheetName = request.getParameter("sheetName");
            String permission = request.getParameter("permissionType"); //TODO : maybe change to body and get permissionType

            if (sheetName == null || permission == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheetName or permission parameter");
                return;
            }

            try {
                PermissionType permissionType = PermissionType.valueOf(permission);
                Gson gsonBuilder = new GsonBuilder()
                        .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                        .create();
                SheetDTO sheetDTO = getSheetDTOByName(sheetName);  // זהו המקום בו תבצע את הלוגיקה שלך

                if (sheetDTO == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sheet not found");
                    return;
                }

                request.getSession(true).setAttribute(USER_VIEWED_SHEET_VERSION, sheetDTO.getVersion() + "");//מעדכנים את הגרסה  בSESSION
                request.getSession(true).setAttribute(USER_VIEWED_SHEET_NAME, sheetDTO.getName());//מעדכנים את השם בSESSION
                request.getSession(true).setAttribute(USER_VIEWED_SHEET_PERMISSION, permissionType.toString());

                // המרת ה-sheetDTO ל-JSON
                String json = gsonBuilder.toJson(sheetDTO);
                out.println(json);
            }catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                String json =gson.toJson("An error occurred: Invalid permission parameter");
                response.getWriter().write(json);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                String json =gson.toJson( e.getMessage());
                response.getWriter().write(json);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    private SheetDTO getSheetDTOByName(String sheetName) {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

        return sheetEngine.getCurrentSheetDTO();
    }
}
