package servlets.sheetManagerScreen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getSheetDTOByVersion")
public class GetSheetDTOByVersion extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        Gson gson = new Gson();
        //String sheetName = request.getParameter("sheetName");
        String sheetName = SessionUtils.getViewedSheetName(request);
        String version = request.getParameter("version");

        if (sheetName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name");
            return;
        }

        System.out.println("Getting sheetDTO, request URI is: " + request.getRequestURI());
        try (PrintWriter out = response.getWriter()) {
            Gson gsonBuilder = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                    .create();

            try {
                SheetDTO sheetDTO = getSheetDTOByNameAndVersion(sheetName, Integer.parseInt(version));  // זהו המקום בו תבצע את הלוגיקה שלך

                if (sheetDTO == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    String json = gson.toJson("Sheet not found");
                    out.println(json);
                    //response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sheet not found");
                    return;
                }

                // המרת ה-sheetDTO ל-JSON
                String json = gsonBuilder.toJson(sheetDTO);
                out.println(json);
            }
            catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json = gson.toJson(e.getMessage());
                response.getWriter().write(json);
            }


        } catch (Exception e) {
            // במקרה של תקלה, נחזיר שגיאת שרת
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    private SheetDTO getSheetDTOByNameAndVersion(String sheetName, int sheetVersion) {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        return sheetEngine.getSheetDTOByVersion(sheetVersion);
    }

}
