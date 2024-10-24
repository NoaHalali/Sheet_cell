package servlets.sheetManagerScreen.range;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

@WebServlet("/addRange")
public class AddRangeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        // Set the response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Load parameters from the request body using Properties
        Properties prop = new Properties();
        try (InputStream inputStream = req.getInputStream()) {
            prop.load(inputStream);
        }

        // Read values from the Properties
        //String sheetName = prop.getProperty("sheetName");
        String sheetName = SessionUtils.getViewedSheetName(req);
        String rangeName = prop.getProperty("rangeName");
        String rangeDefinition = prop.getProperty("rangeDefinition");

        if (sheetName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name");
            return;
        }

        // Validate parameters
        if (rangeName == null || rangeDefinition == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing rangeName or rangeDefinition\"}");
            return;
        }




        // Process the request and add the range using the engine
        try {
            List<String> rangeNames = addRange(sheetName, rangeName, rangeDefinition);

            Gson gson = new Gson();
            String json = gson.toJson(rangeNames);
            out.println(json);  // This is the only valid JSON response

            // Set the response status as successful (optional, since 200 OK is the default)
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (IllegalArgumentException e) {
            // Handle invalid input error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Failed to add range: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            // Handle server error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to add range: " + e.getMessage() + "\"}");
        }
    }

    private List<String> addRange(String sheetName, String rangeName, String rangeDefinition) throws IllegalArgumentException {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        sheetEngine.addRange(rangeName, rangeDefinition);
        return sheetEngine.getRangesNames();
    }

}

