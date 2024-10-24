package servlets.sheetManagerScreen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.engines.sheetEngine.SheetEngineImpl;
import shticell.sheets.manager.MultiSheetEngineManager;
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

        try (PrintWriter out = response.getWriter()) {
            String sheetName = request.getParameter("sheetName");

            if (sheetName == null || sheetName.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty sheetName parameter");
                return;
            }

            //System.out.println("Getting sheetDTO, request URI is: " + request.getRequestURI());
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                        .create();
                SheetDTO sheetDTO = getSheetDTOByName(sheetName);  // זהו המקום בו תבצע את הלוגיקה שלך

                if (sheetDTO == null) {
                    // אם ה-sheet לא נמצא, נחזיר שגיאה מתאימה
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sheet not found");
                    return;
                }

                request.getSession(true).setAttribute(USER_VIEWED_SHEET_VERSION, sheetDTO.getVersion() + "");//מעדכנים את הגרסה  בSESSION
                request.getSession(true).setAttribute(USER_VIEWED_SHEET_NAME, sheetDTO.getName());//מעדכנים את השם בSESSION


                // המרת ה-sheetDTO ל-JSON
                String json = gson.toJson(sheetDTO);
                out.println(json);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid version parameter");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
                return;
            }
        } catch (Exception e) {
            // במקרה של תקלה, נחזיר שגיאת שרת
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    // זו רק דוגמה איך לחלץ את ה-SheetDTO לפי השם. תצטרך להתאים את השיטה הזו לצרכים שלך.
    private SheetDTO getSheetDTOByName(String sheetName) {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

        return sheetEngine.getCurrentSheetDTO();
    }

}
