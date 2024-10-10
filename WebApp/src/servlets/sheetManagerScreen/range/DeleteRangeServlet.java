package servlets.sheetManagerScreen.range;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/deleteRange")
public class DeleteRangeServlet extends HttpServlet {

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            // חילוץ הפרמטרים מהבקשה (sheetName, range)

            String sheetName = request.getParameter("sheetName");
            String rangeName = request.getParameter("rangeName");
            System.out.println("Received sheetName: " + sheetName);
            System.out.println("Received rangeName: " + rangeName);

            if (sheetName == null || rangeName == null) {
                // אם אחד הפרמטרים חסר, נחזיר שגיאה
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheetName or range");
                return;
            }

            try {
                List<String> rangeNames = deleteRangeInSheet(sheetName, rangeName);

                // Convert the list of range names to JSON and send it as the response
                Gson gson = new Gson();
                String json = gson.toJson(rangeNames);
                out.println(json);  // This is the only valid JSON response

            } catch (IllegalArgumentException e) {
                // Handle invalid input error
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Failed to delete range: " + e.getMessage() + "\"}");

            } catch (Exception e) {
                // במקרה שנזרקה שגיאה, נחזיר שגיאת שרת
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }


        } catch (Exception e) {
            // במקרה של שגיאה כללית, נחזיר שגיאת שרת
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private List<String> deleteRangeInSheet (String sheetName, String range) throws IllegalArgumentException {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        sheetEngine.deleteRange(range);
        return sheetEngine.getRangesNames();
    }
}
