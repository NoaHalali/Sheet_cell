package servlets.sheetManagerScreen.cell;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import utils.ServletUtils;

import java.io.IOException;

import static constant.Constants.USER_VIEWED_SHEET_VERSION;

@WebServlet("/updateCell")
public class UpdateCellServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try {
            // חילוץ הפרמטרים מהבקשה (cellID והערך החדש)
            String sheetName = request.getParameter("sheetName");
            String cellID = request.getParameter("cellID");
            String newValue = request.getParameter("newValue");

            if (cellID == null || newValue == null) {
                // אם אחד הפרמטרים חסר, נחזיר שגיאה
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing cellID or newValue");
                return;
            }

            // כאן תבצע את הלוגיקה שלך לעדכן את התא במנוע (Engine)
            Coordinate coordinate = CoordinateImpl.parseCoordinate(cellID); // הנחה שיש לך Coordinate מתאים מזה
            boolean isUpdated = false;

            try {
                isUpdated = updateCellInSheet(sheetName, coordinate, newValue,request);



            } catch (Exception e) {
                // במקרה שנזרקה שגיאה, נחזיר שגיאת שרת
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }

            // החזרת התשובה – האם התא התעדכן או לא (true/false)
            Gson gson = new Gson();
            String json = gson.toJson(isUpdated); // החזרת true אם התא השתנה, false אם לא
            response.getWriter().write(json);

        } catch (Exception e) {
            // במקרה של שגיאה כללית, נחזיר שגיאת שרת
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }

    }

//    private boolean updateCellInSheet(String sheetName, Coordinate coordinate, String newValue) throws Exception {
//        MultiSheetEngineManager engineManager = ServletUtils.getMultiSheetEngineManager(getServletContext());
//        SheetEngine sheetEngine = engineManager.getSheetEngine(sheetName);
//        return sheetEngine.updateCellValue(newValue, coordinate); // המתודה שלך מה-Engine
//    }

    private boolean updateCellInSheet(String sheetName, Coordinate coordinate, String newValue,HttpServletRequest request) throws Exception {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);
        boolean isUpdated= sheetEngine.updateCellValue(newValue, coordinate);

        if(isUpdated) {
            int version = sheetEngine.getCurrentVersion();
            request.getSession(true).setAttribute(USER_VIEWED_SHEET_VERSION, version+"");
        }


        return isUpdated;
    }
}



