package servlets.multiSheetsScreen.permissions;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.PermissionType;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet("/handleRequestPermission")
public class HandleRequestPermissionServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Load parameters from the request body using Properties
        Properties prop = new Properties();
        try (InputStream inputStream = req.getInputStream()) {
            prop.load(inputStream);


            // Read values from the Properties
            String sheetName = prop.getProperty("sheetName");
            String requestNumberString = prop.getProperty("requestNumber");
            String isApproved = prop.getProperty("isApproved");
           // String username = "noa"; //SessionUtils.getUsername(req);

            //String permissionType = prop.getProperty("permissionType");
            //PermissionType permission = PermissionType.valueOf(permissionType.toUpperCase());

            // Validate parameters
            if (sheetName == null || requestNumberString == null || isApproved == null) {
                // אם אחד הפרמטרים חסר, נחזיר שגיאה
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing cellID or newValue"); //TODO: check if its printed or need to be from out.println()
                return;
            }

            boolean approvedBoolean = Boolean.valueOf(isApproved);
            int requestNumber = Integer.parseInt(requestNumberString);

            handleRequestPermission(sheetName, requestNumber,approvedBoolean,out);

            response.setStatus(HttpServletResponse.SC_OK);//todo add in others
        } catch (Exception e) {

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private void handleRequestPermission(String sheetName, int requestNumber, boolean isApproved,PrintWriter out ) {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

        if (isApproved) {
            sheetEngine.approvePermissionRequest(requestNumber);
            //sheetEngine.
            out.println("permission approved successfully");

        }else{
            sheetEngine.denyPermissionRequest(requestNumber);
            out.println("permission denied successfully");
        }
    }
}
