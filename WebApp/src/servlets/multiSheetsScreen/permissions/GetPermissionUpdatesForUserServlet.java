package servlets.multiSheetsScreen.permissions;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.permissions.PermissionType;
import shticell.permissions.RequestStatus;
import shticell.users.PermissionUpdate;
import shticell.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static constant.Constants.USER_VIEWED_SHEET_PERMISSION;

@WebServlet("/getPermissionUpdatesForUser")
public class GetPermissionUpdatesForUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {

            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            PermissionUpdate update= userManager.getUserPermissionsUpdates(username);


            if (update != null) {
                //TODO: extrect sheetname from session and request, compare and if equal and  approve, change the session parameter
                updateSessionPermissionIfNeeded(request, update);

                response.setStatus(HttpServletResponse.SC_OK);
                //System.out.println(getUpdateMessageFromPermissionUpdate(update,numberGenerator));
                Gson gson = new Gson();
                String json = gson.toJson(update);
                out.println(json);
            } else {
                // אין עדכון - מחזירים סטטוס 204 ללא תוכן
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }

            out.flush();
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }

    private void updateSessionPermissionIfNeeded(HttpServletRequest request, PermissionUpdate update) throws Exception {

        boolean isAproved = update.getRequestStatus().equals(RequestStatus.APPROVED);
        if(!isAproved){
            String sheetName = SessionUtils.getViewedSheetName(request);
            String sheetNameFromUpdate = update.getSheetName();

            if(sheetNameFromUpdate.equals(sheetName)){
                PermissionType permissionType = update.getPermission();
                request.getSession(true).setAttribute(USER_VIEWED_SHEET_PERMISSION, permissionType.toString());
            }
        }
    }
}
