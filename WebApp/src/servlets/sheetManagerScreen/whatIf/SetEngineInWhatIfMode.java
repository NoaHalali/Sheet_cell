package servlets.sheetManagerScreen.whatIf;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.exceptions.OutdatedSheetVersionException;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
@WebServlet("/setSheetInWhatIfMode")
public class SetEngineInWhatIfMode extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        // Set the response type to JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter out = resp.getWriter();

        // Load parameters from the request body using Properties
        Properties prop = new Properties();
        try (InputStream inputStream = request.getInputStream()) {
            prop.load(inputStream);
        }

        // Read values from the Properties
        //String sheetName = prop.getProperty("sheetName");
        String sheetName = SessionUtils.getViewedSheetName(request);
        String cellID = prop.getProperty("cellID");

        // Validate parameters
        if (sheetName == null || cellID == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json = gson.toJson("Missing sheet Name or cell ID");
            resp.getWriter().write(json);
            return;
        }

        try {
             setSubEngineInWhatIfMode(sheetName,cellID, request);


            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (IllegalArgumentException e) {
            // Handle invalid input error
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json = gson.toJson(e.getMessage());
            resp.getWriter().write(json);
        } catch (Exception e) {
            // Handle server error
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String json = gson.toJson(e.getMessage());
            resp.getWriter().write(json);
        }
    }

    private void setSubEngineInWhatIfMode(String sheetName, String cellID, HttpServletRequest request)throws OutdatedSheetVersionException {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);
        sheetEngine.setEngineInWhatIfMode(CoordinateImpl.parseCoordinate(cellID));
    }


}
