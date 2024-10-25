package servlets.sheetManagerScreen.range;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.PermissionType;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/deleteRange")
public class DeleteRangeServlet extends HttpServlet {

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        PrintWriter out = response.getWriter();

        try {
            String sheetName = SessionUtils.getViewedSheetName(request);
            String rangeName = request.getParameter("rangeName");

            if (sheetName == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name");
                return;
            }

            String permissionStr = SessionUtils.getUserViewedSheetPermission(request);
            boolean hasEditPermission = permissionStr.equals(PermissionType.OWNER.toString()) || permissionStr.equals(PermissionType.WRITER.toString());
            if(permissionStr == null || !hasEditPermission){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must have OWNER or WRITER permission to edit cells");
                return;
            }

            try {
                List<String> rangeNames = deleteRangeInSheet(sheetName, rangeName);

                // Convert the list of range names to JSON and send it as the response
                String json = gson.toJson(rangeNames);
                out.println(json);  // This is the only valid JSON response

            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json = gson.toJson(e.getMessage());

                response.getWriter().write( json);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }


        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private List<String> deleteRangeInSheet (String sheetName, String range) throws IllegalArgumentException {
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        sheetEngine.deleteRange(range);
        return sheetEngine.getRangesNames();
    }
}
