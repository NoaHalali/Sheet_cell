package servlets.sheetManagerScreen.cell;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.PermissionType;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constant.Constants.USER_VIEWED_SHEET_VERSION;

@WebServlet("/updateCell")
public class UpdateCellServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();

        try {
            //String sheetName = request.getParameter("sheetName");
            String cellID = request.getParameter("cellID");
            String newValue = request.getParameter("newValue");

            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            String permissionStr = SessionUtils.getUserViewedSheetPermission(request);
            boolean hasEditPermission = permissionStr.equals(PermissionType.OWNER.toString()) || permissionStr.equals(PermissionType.WRITER.toString());
            if(permissionStr == null || !hasEditPermission){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must have OWNER or WRITER permission to edit cells");
                return;
            }

            String sheetName = SessionUtils.getViewedSheetName(request);
            if(sheetName == null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name");
                return;
            }

            if (cellID == null || newValue == null ) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing cellID or newValue parameter");
                return;
            }
            boolean isUpdated = false;
            try {
                Coordinate coordinate = CoordinateImpl.parseCoordinate(cellID);
                isUpdated = updateCellInSheet(sheetName, coordinate, newValue,request, username);

            }
            catch (Exception e) {
                // במקרה שנזרקה שגיאה, נחזיר שגיאת שרת

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json= gson.toJson(e.getMessage());
                response.getWriter().write( json );
                return;
            }

            // החזרת התשובה – האם התא התעדכן או לא (true/false)

            String json = gson.toJson(isUpdated);
            response.getWriter().write(json);

        } catch (Exception e) {
            // במקרה של שגיאה כללית, נחזיר שגיאת שרת
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String json= gson.toJson(e.getMessage());
            response.getWriter().write( json );

        }

    }

    private boolean updateCellInSheet(String sheetName, Coordinate coordinate, String newValue,
                                      HttpServletRequest request, String username) throws Exception {

        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        ServletUtils.checkIfClientSheetVersionIsUpdated(request, sheetEngine);

        boolean isUpdated= sheetEngine.updateCellValue(newValue, coordinate, username);

        if(isUpdated) {
            int version = sheetEngine.getCurrentVersion();
            request.getSession(true).setAttribute(USER_VIEWED_SHEET_VERSION, version+"");
        }

        return isUpdated;
    }
}



