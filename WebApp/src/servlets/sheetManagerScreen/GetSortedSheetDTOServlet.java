package servlets.sheetManagerScreen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/getSortedSheetDTO")
public class GetSortedSheetDTOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String sheetName = request.getParameter("sheetName");
        String rangeDefinition = request.getParameter("rangeDefinition");
        if (rangeDefinition == null || rangeDefinition.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing rangeDefinition\"}");
            return;
        }

        // קבלת העמודות לסינון מה-query parameters
        String[] columns = request.getParameterValues("columnsToSortBy");
        if (columns == null || columns.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing columnsToSortBy\"}");
            return;
        }

        // המרת העמודות מרשימת מחרוזות לרשימת תווים
        List<Character> columnsToSortBy = new ArrayList<>();
        for (String column : columns) {
            if (column.length() == 1) {
                columnsToSortBy.add(column.charAt(0));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid column: " + column + "\"}");
                return;
            }
        }

        try {
            // קבלת ה-SheetEngine לצורך ביצוע הפעולה
            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

            // קבלת ה-SheetDTO המסודר
            SheetDTO sortedSheet = sheetEngine.getSortedSheetDTO(rangeDefinition, columnsToSortBy);

            // המרת התוצאה ל-JSON
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                    .create();
            String jsonResponse = gson.toJson(sortedSheet);

            // שליחת התשובה בחזרה ללקוח
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}
