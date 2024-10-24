package servlets.multiSheetsScreen.permissions;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.PermissionType;
import shticell.permissions.PermissionsManager;
import shticell.permissions.RequestStatus;
import shticell.users.PermissionUpdate;
import shticell.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet("/handleRequestPermission")
public class HandleRequestPermissionServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Load parameters from the request body using Properties
        Properties prop = new Properties();
        try (InputStream inputStream = request.getInputStream()) {
            prop.load(inputStream);

            // Read values from the Properties
            String sheetName = prop.getProperty("sheetName");
            String requestNumberString = prop.getProperty("requestNumber");
            String requestStatusStr = prop.getProperty("requestStatus");

            String permissionStr = SessionUtils.getUserViewedSheetPermission(request);
            if(permissionStr == null || !permissionStr.equals(PermissionType.OWNER.toString())){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to perform this action");
                return;
            }

           // String sheetName = SessionUtils.getViewedSheetName(request);
            if(sheetName == null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name");
                return;
            }
            // Validate parameters
            if ( requestNumberString == null || requestStatusStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing request number or request status");
                return;
            }

            try {
                RequestStatus requestStatus = RequestStatus.valueOf(requestStatusStr);
                int requestNumber = Integer.parseInt(requestNumberString);
                handleRequestPermission(sheetName.toLowerCase(), requestNumber, requestStatus,out);
                response.setStatus(HttpServletResponse.SC_OK);//todo add in others

            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            }


        } catch (Exception e) {

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private void handleRequestPermission(String sheetName, int requestNumber, RequestStatus requestStatus, PrintWriter out ) {

            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            PermissionsManager sheetPermissionsManager = sheetEngine.getPermissionsManager();
            String username = sheetPermissionsManager.getRequestedUsername(requestNumber);
            PermissionType permission = sheetPermissionsManager.getRequestedPermission(requestNumber);

            switch (requestStatus) {
                case APPROVED:
                    sheetEngine.approvePermissionRequest(requestNumber);
                    out.println("permission approved successfully");

                    break;
                case REJECTED:
                    sheetEngine.denyPermissionRequest(requestNumber);
                    out.println("permission denied successfully");
                    break;
                case PENDING:
                    out.println("permission is still pending");
                    break;
            }

            userManager.addPermissionUpdate(username, new PermissionUpdate(sheetName, permission, requestStatus));
        }

}
