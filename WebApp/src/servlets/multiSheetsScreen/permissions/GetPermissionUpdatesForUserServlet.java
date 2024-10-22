package servlets.multiSheetsScreen.permissions;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.users.PermissionUpdate;
import shticell.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;
import java.time.LocalTime;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@WebServlet("/getPermissionUpdatesForUser")
public class GetPermissionUpdatesForUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        Random numberGenerator = new Random();
        try (PrintWriter out = response.getWriter()) {

            String userName = SessionUtils.getUsername(request);
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            PermissionUpdate update= userManager.getUserPermissionsUpdates(userName);
            //userManager.setNoUpdateForUser(userName);

            if (update != null) {
                // יש עדכון - מחזירים את ה-JSON
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
    public String getUpdateMessageFromPermissionUpdate(PermissionUpdate permissionUpdate, Random numberGenerator) {

        LocalTime currentTime = LocalTime.now();
        String message ="Update: permission: " + permissionUpdate.getPermission() +", for sheet: " + permissionUpdate.getSheetName()
                + ", has been changed to: " + permissionUpdate.getRequestStatus() +" at: "+currentTime+ "random num :" +numberGenerator;
        return message;

    }
}
