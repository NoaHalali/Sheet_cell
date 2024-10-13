package servlets.sheetManagerScreen.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/getDistinctValuesOfMultipleColsInRange")
public class GetDistinctValuesOfMultipleColsInRangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // קבלת ה-rangeDefinition מה-query parameters
        String sheetName = request.getParameter("sheetName");
        String rangeDefinition = request.getParameter("rangeDefinition");
        if (rangeDefinition == null || rangeDefinition.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing rangeDefinition\"}");
            return;
        }

        // קבלת העמודות לסינון מה-query parameters
        String[] cols = request.getParameterValues("cols");
        if (cols == null || cols.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing columns\"}");
            return;
        }

        // המרת העמודות מרשימת מחרוזות לרשימת תווים
        List<Character> columnsToSortBy = new ArrayList<>();
        for (String col : cols) {
            if (col.length() == 1) {
                columnsToSortBy.add(col.charAt(0));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid column: " + col + "\"}");
                return;
            }
        }

        try {
            // קבלת SheetEngine לצורך ביצוע הפעולה
            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
            Map<String, Set<EffectiveValue>> distinctValuesMap = sheetEngine.getDistinctValuesOfMultipleColsInRange(columnsToSortBy, rangeDefinition);

            // המרת התוצאה ל-JSON תוך שימוש ב-EffectiveValueSerializer
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())  // הוספת הסריאליזר
                    .create();
            String jsonResponse = gson.toJson(distinctValuesMap);

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
