package servlets.multiSheetsScreen;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.SheetDetailsDTO;
import shticell.sheets.manager.MultiSheetEngineManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/getSheetsDetailsList")
public class GetSheetsDetailsListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {

            Gson gson = new Gson();
            try {
                MultiSheetEngineManager manager = ServletUtils.getMultiSheetEngineManager(getServletContext());

                String username = SessionUtils.getUsername(request);
                if (username == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                List<SheetDetailsDTO> sheetDetailsDTOList = manager.getSheetsDetalisListForUser(username);

                String json = gson.toJson(sheetDetailsDTOList);
                out.println(json);
                out.flush();
            }
            catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }


        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }
}
