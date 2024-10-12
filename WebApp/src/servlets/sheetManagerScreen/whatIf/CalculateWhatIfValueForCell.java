package servlets.sheetManagerScreen.whatIf;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
@WebServlet("/calculateWhatIfValueForCell")
public class CalculateWhatIfValueForCell extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set the response type to JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        // Load parameters from the request body using Properties
        Properties prop = new Properties();
        try (InputStream inputStream = req.getInputStream()) {
            prop.load(inputStream);
        }

        // Read values from the Properties
        String sheetName = prop.getProperty("sheetName");
        String valueSTR = prop.getProperty("value");


        // Validate parameters
        if (sheetName == null || valueSTR == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing sheet Name or value\"}");
            return;
        }
        double value = Double.parseDouble(valueSTR);

        // Process the request and add the range using the engine
        try {
            // Call the engine to add the range and get the updated list of ranges
            SheetDTO sheet = calculateWhatIfValueForCell(sheetName,value);

            // Convert the list of range names to JSON and send it as the response
            Gson gson = new Gson();
            String json = gson.toJson(sheet);
            out.println(json);  // This is the only valid JSON response

            // Set the response status as successful (optional, since 200 OK is the default)
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (IllegalArgumentException e) {
            // Handle invalid input error
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Failed to add range: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            // Handle server error
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Failed to add range: " + e.getMessage() + "\"}");
        }
    }

    private SheetDTO calculateWhatIfValueForCell(String sheetName, Double value) throws ServletException, IOException {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        return sheetEngine.calculateWhatIfValueForCell(value);
    }

}
