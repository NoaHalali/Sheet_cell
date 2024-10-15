package servlets.multiSheetsScreen.permissions;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.RequestStatus;
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
            String requestStatusStr = prop.getProperty("requestStatus");
           // String username = "noa"; //SessionUtils.getUsername(req);

            //String permissionType = prop.getProperty("permissionType");
            //PermissionType permission = PermissionType.valueOf(permissionType.toUpperCase());

            // Validate parameters
            if (sheetName == null || requestNumberString == null || requestStatusStr == null) {
                // אם אחד הפרמטרים חסר, נחזיר שגיאה
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing cellID or newValue"); //TODO: check if its printed or need to be from out.println()
                return;
            }

            try {
                RequestStatus requestStatus = RequestStatus.valueOf(requestStatusStr);
                int requestNumber = Integer.parseInt(requestNumberString);
                handleRequestPermission(sheetName, requestNumber, requestStatus,out);
                response.setStatus(HttpServletResponse.SC_OK);//todo add in others

            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            }

            //RequestStatus requestStatus = RequestStatus.valueOf(requestStatusStr);
            //int requestNumber = Integer.parseInt(requestNumberString);


        } catch (Exception e) {

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private void handleRequestPermission(String sheetName, int requestNumber, RequestStatus requestStatus, PrintWriter out ) {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

        switch (requestStatus){
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

//        if (isApproved) {
//            sheetEngine.approvePermissionRequest(requestNumber);
//            //sheetEngine.
//            out.println("permission approved successfully");
//
//        }else{
//            sheetEngine.denyPermissionRequest(requestNumber);
//            out.println("permission denied successfully");
//        }
    }
}
