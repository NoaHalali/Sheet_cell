package servlets.multiSheetsScreen.permissions;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.PermissionType;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet("/requestPermission")
public class RequestPermissionServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Load parameters from the request body using Properties
        Properties prop = new Properties();
        try (InputStream inputStream = request.getInputStream()) {
            prop.load(inputStream);

            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            // Read values from the Properties
            //String sheetName = SessionUtils.getViewedSheetName(request);
            String sheetName = prop.getProperty("sheetName");
            String permissionType = prop.getProperty("permissionType");
            // Validate parameters
            if (sheetName == null || permissionType == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheetName or permissionType parameter");
                return;
            }

            try {
                PermissionType permission = PermissionType.valueOf(permissionType.toUpperCase());
                addUserPermissionRequest(sheetName.toLowerCase(), permission, username);
                response.setStatus(HttpServletResponse.SC_OK);//todo add in others
                out.println("Permission request " + permissionType + ", by the user '" + username +"', for sheet '" +sheetName+  "' added successfully");
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid permissionType: " + permissionType);
            }



        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private void  addUserPermissionRequest(String sheetName, PermissionType type,String username) throws IllegalArgumentException {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        sheetEngine.addUserPermissionRequest(username,type);
    }
}
