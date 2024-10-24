package servlets.sheetManagerScreen.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.exceptions.OutdatedSheetVersionException;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;
import utils.SessionUtils;

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
        Gson gson=new Gson();
        // קבלת ה-rangeDefinition מה-query parameters
        //String sheetName = request.getParameter("sheetName");
        String sheetName = SessionUtils.getViewedSheetName(request);
        if (sheetName == null || sheetName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json= gson.toJson("Missing sheetName");
            response.getWriter().write(json);

            return;
        }

        String rangeDefinition = request.getParameter("rangeDefinition");
        if (rangeDefinition == null || rangeDefinition.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json= gson.toJson("Missing rangeDefinition");
            response.getWriter().write( json );
            return;
        }

        // קבלת העמודות לסינון מה-query parameters
        String[] cols = request.getParameterValues("cols");
        if (cols == null || cols.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json= gson.toJson("Missing columns");
            response.getWriter().write( json );
            return;
        }

        // המרת העמודות מרשימת מחרוזות לרשימת תווים
        List<Character> columnsToSortBy = new ArrayList<>();
        for (String col : cols) {
            if (col.length() == 1) {
                columnsToSortBy.add(col.charAt(0));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json= gson.toJson("Invalid column " + col);
                response.getWriter().write( json );
                return;
            }
        }

        try {
            Map<String, Set<EffectiveValue>> distinctValuesMap = getDistinctValuesOfMultipleColsInRange(request, sheetName, columnsToSortBy, rangeDefinition);

            Gson gsonBuilder = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())  // הוספת הסריאליזר
                    .create();
            String jsonResponse = gsonBuilder.toJson(distinctValuesMap);

            // שליחת התשובה בחזרה ללקוח
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();

        }
        catch (OutdatedSheetVersionException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            String json = gson.toJson(e.getMessage());
            response.getWriter().write(json);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String json= gson.toJson(e.getMessage());
            response.getWriter().write( json );
        }
    }

    private Map<String, Set<EffectiveValue>> getDistinctValuesOfMultipleColsInRange(HttpServletRequest request, String sheetName,
                                                                                    List<Character> columnsToSortBy, String rangeDefinition)
            throws OutdatedSheetVersionException
    {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);
        Map<String, Set<EffectiveValue>> distinctValuesMap = sheetEngine.getDistinctValuesOfMultipleColsInRange(columnsToSortBy, rangeDefinition);
        return distinctValuesMap;
    }
}
