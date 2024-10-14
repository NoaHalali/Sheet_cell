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
            String permissionType = prop.getProperty("permissionType");
            String isApproved = prop.getProperty("isApproved");
            String username = "noa"; //SessionUtils.getUsername(req);
            PermissionType permission = PermissionType.valueOf(permissionType.toUpperCase());
            boolean approvedBoolean = Boolean.valueOf(isApproved);
            // Validate parameters
            if (sheetName == null || permissionType == null) {
                // אם אחד הפרמטרים חסר, נחזיר שגיאה
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing cellID or newValue");
                return;
            }

            // כאן תבצע את הלוגיקה שלך לעדכן את התא במנוע (Engine)

          handleRequestPermission(sheetName,permission,username,approvedBoolean,out);

            // החזרת התשובה – האם התא התעדכן או לא (true/false)
//            Gson gson = new Gson();
//            String json = gson.toJson(); // החזרת true אם התא השתנה, false אם לא
//            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_OK);//todo add in others
        } catch (Exception e) {
            // במקרה של שגיאה כללית, נחזיר שגיאת שרת
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
            //response.getWriter().println(e.getMessage());
        }
    }

    private void handleRequestPermission(String sheetName, PermissionType type, String username, boolean isApproved,PrintWriter out ) {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

        if (isApproved) {
            sheetEngine.givePermissionToUser(username,type);
            out.println(" permission approved");

        }else{
            sheetEngine.denyPermissionToUser(username);
            out.println("permission deneyed ");
        }
    }
}
