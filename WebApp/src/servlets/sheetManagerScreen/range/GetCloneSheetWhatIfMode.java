package servlets.sheetManagerScreen.range;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.Sheet;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
@WebServlet("/getClonedSheet")
public class GetCloneSheetWhatIfMode extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String sheetName = request.getParameter("sheetName");

        if (sheetName == null || sheetName.isEmpty()) {
            // אם השם לא קיים או ריק, נחזיר שגיאה
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty sheetName parameter");
            return;
        }

        System.out.println("Getting sheetDTO, request URI is: " + request.getRequestURI());
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                    .create();
            // כאן תוכל לחלץ את ה-SheetDTO לפי השם מה-Engine שלך
            Sheet clonedSheet = getClonedSheet(sheetName);  // זהו המקום בו תבצע את הלוגיקה שלך

            if (clonedSheet == null) {
                // אם ה-sheet לא נמצא, נחזיר שגיאה מתאימה
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sheet not found");
                return;
            }

            // המרת ה-sheetDTO ל-JSON
            String json = gson.toJson(clonedSheet);
            out.println(json);
        } catch (Exception e) {
            // במקרה של תקלה, נחזיר שגיאת שרת
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    // זו רק דוגמה איך לחלץ את ה-SheetDTO לפי השם. תצטרך להתאים את השיטה הזו לצרכים שלך.
    private Sheet getClonedSheet(String sheetName) {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        return sheetEngine.getClonedSheet();
    }
}
