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
import shticell.exceptions.OutdatedSheetVersionException;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;
import utils.SessionUtils;

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
         Gson gson = new Gson();
        //String sheetName = request.getParameter("sheetName");
        String sheetName = SessionUtils.getViewedSheetName(request);
//        if (sheetName == null || sheetName.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"error\": \"Missing sheetName\"}");
//            return;
//        }
        if (sheetName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name");
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
        String[] columns = request.getParameterValues("columnsToSortBy");
        if (columns == null || columns.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json= gson.toJson("Missing columnsToSortBy");
            response.getWriter().write( json );
            return;
        }

        // המרת העמודות מרשימת מחרוזות לרשימת תווים
        List<Character> columnsToSortBy = new ArrayList<>();
        for (String column : columns) {
            if (column.length() == 1) {
                columnsToSortBy.add(column.charAt(0));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json= gson.toJson("Invalid column: " + column);
                response.getWriter().write(json);
                return;
            }
        }

        try {
            // קבלת ה-SheetEngine לצורך ביצוע הפעולה
            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

            String userVersion = SessionUtils.getViewedSheetVersion(request);
            sheetEngine.checkIfVersionIsUpdated(userVersion);
            ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);

            // קבלת ה-SheetDTO המסודר
            SheetDTO sortedSheet = sheetEngine.getSortedSheetDTO(rangeDefinition, columnsToSortBy);

            // המרת התוצאה ל-JSON
            Gson gsonBuilder = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                    .create();
            String jsonResponse = gsonBuilder.toJson(sortedSheet);

            // שליחת התשובה בחזרה ללקוח
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();

        }catch (OutdatedSheetVersionException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json =gson.toJson( e.getMessage());
            response.getWriter().write(json);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String json =gson.toJson( e.getMessage());
            response.getWriter().write(json);
        }
    }
}
