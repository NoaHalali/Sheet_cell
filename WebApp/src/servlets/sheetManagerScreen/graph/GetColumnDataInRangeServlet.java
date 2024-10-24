package servlets.sheetManagerScreen.graph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.cell.CellDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/getColumnDataInRange")
public class GetColumnDataInRangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // קבלת ה-rangeDefinition וה-sheetName מה-query parameters
        String rangeDefinition = request.getParameter("rangeDefinition");
        String sheetName = request.getParameter("sheetName");

        if (sheetName == null || sheetName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing sheetName\"}");
            return;
        }

        if (rangeDefinition == null || sheetName == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing rangeDefinition or sheetName\"}");
            return;
        }

        try {
            // קבלת ה-SheetEngine והביצוע בפועל
            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
            ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);
            List<CellDTO> columnData = sheetEngine.getColumnDataInRange(rangeDefinition);

            // המרת התוצאה ל-JSON והחזרתה
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                    .create();
            String jsonResponse = gson.toJson(columnData);

            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}
