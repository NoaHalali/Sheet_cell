package servlets.multiSheetsScreen.permissions;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.permission.UserRequestDTO;
import shticell.engines.sheetEngine.SheetEngine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/getRequestsList")
public class GetRequestsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {

            String sheetName = request.getParameter("sheetName");
            if (sheetName == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
            List<UserRequestDTO> requestsDTOList = sheetEngine.getRequestsDTOList();

            response.setStatus(HttpServletResponse.SC_OK);

            Gson gson = new Gson();
            String json = gson.toJson(requestsDTOList);
            out.println(json);
            out.flush();
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }

}
