package servlets.sheetManagerScreen.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueDeserializer;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet("/getFilteredSheetDTOFromMultipleCols")
public class GetFilteredSheetDTOFromMultipleColsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // קבלת ה-rangeDefinition מה-query parameters
        String rangeDefinition = request.getParameter("rangeDefinition");
        if (rangeDefinition == null || rangeDefinition.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing rangeDefinition\"}");
            return;
        }

        Map<String, Set<EffectiveValue>> selectedValues = getColToValuesMapFromRequest(request);

        try {
            // קבלת SheetEngine לצורך ביצוע הסינון
            String sheetName = request.getParameter("sheetName");

            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
            ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);

            SheetDTO filteredSheet = sheetEngine.getFilteredSheetDTOFromMultipleCols(selectedValues, rangeDefinition);

            // החזרת התוצאה כ-JSON
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                    .create();
            String jsonResponse = gson.toJson(filteredSheet);
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }

    private static Map<String, Set<EffectiveValue>> getColToValuesMapFromRequest(HttpServletRequest request) throws IOException {
        // קריאת המפה של selectedValues מה-body של הבקשה
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())  // שימוש ב-Deserializer
                .create();

        Map<String, Set<EffectiveValue>> selectedValues = gson.fromJson(request.getReader(), new TypeToken<Map<String, Set<EffectiveValue>>>(){}.getType());
        return selectedValues;
    }
}

