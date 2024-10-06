package servlets.sheetManagerScreen;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engine.impl.SheetEngine;
import shticell.sheets.manager.MultiSheetEngineManager;
import shticell.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static constant.Constants.USERNAME;

public class GetSheetDTOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String sheetName = request.getParameter("sheetName");

        if (sheetName == null || sheetName.isEmpty()) {
            // אם השם לא קיים או ריק, נחזיר שגיאה
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty sheetName parameter");
            return;
        }

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();

            // כאן תוכל לחלץ את ה-SheetDTO לפי השם מה-Engine שלך
            SheetDTO sheetDTO = getSheetDTOByName(sheetName);  // זהו המקום בו תבצע את הלוגיקה שלך

            if (sheetDTO == null) {
                // אם ה-sheet לא נמצא, נחזיר שגיאה מתאימה
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sheet not found");
                return;
            }

            // המרת ה-sheetDTO ל-JSON
            String json = gson.toJson(sheetDTO);
            out.println(json);
        } catch (Exception e) {
            // במקרה של תקלה, נחזיר שגיאת שרת
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    // זו רק דוגמה איך לחלץ את ה-SheetDTO לפי השם. תצטרך להתאים את השיטה הזו לצרכים שלך.
    private SheetDTO getSheetDTOByName(String sheetName) {
        MultiSheetEngineManager engineManager = ServletUtils.getMultiSheetEngineManager(getServletContext());
        SheetEngine sheetEngine = engineManager.getSheetEngine(sheetName);
        return sheetEngine.getCurrentSheetDTO();
    }

}
