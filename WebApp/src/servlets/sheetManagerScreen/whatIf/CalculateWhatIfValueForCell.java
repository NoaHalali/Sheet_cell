package servlets.sheetManagerScreen.whatIf;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.exceptions.OutdatedSheetVersionException;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
@WebServlet("/calculateWhatIfValueForCell")
public class CalculateWhatIfValueForCell extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();

        // Load parameters from the request body using Properties
        Properties prop = new Properties();
        try (InputStream inputStream = request.getInputStream()) {
            prop.load(inputStream);

            // Read values from the Properties
            //String sheetName = prop.getProperty("sheetName");
            String sheetName = SessionUtils.getViewedSheetName(request);
            String valueStr = prop.getProperty("value");

            // Validate parameters
            if (sheetName == null || valueStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json = gson.toJson("Missing sheet Name or value");
                response.getWriter().write(json);
                return;
            }

            // Process the request and add the range using the engine
            try {
                double value = Double.parseDouble(valueStr);
                // Call the engine to add the range and get the updated list of ranges
                SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
                ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);
                SheetDTO sheet = sheetEngine.calculateWhatIfValueForCell(value);

                // Convert the list of range names to JSON and send it as the response
                Gson gsonBuilder = new Gson();
                String json = gsonBuilder.toJson(sheet);
                out.println(json);  // This is the only valid JSON response

                // Set the response status as successful (optional, since 200 OK is the default)
                response.setStatus(HttpServletResponse.SC_OK);

            } catch (OutdatedSheetVersionException e) {
                // Handle outdated sheet version error
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                String json = gson.toJson(e.getMessage());
                response.getWriter().write(json);
            } catch (IllegalArgumentException e) {
                // Handle invalid input error
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json = gson.toJson(e.getMessage());
                response.getWriter().write(json);

            }
            catch (Exception e) {
                // Handle server error
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json = gson.toJson(e.getMessage());
                response.getWriter().write(json);
            }

        }catch (Exception e) {
            // Handle server error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String json = gson.toJson(e.getMessage());
            response.getWriter().write(json);
        }
    }

}
