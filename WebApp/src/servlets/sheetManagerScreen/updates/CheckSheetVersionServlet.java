package servlets.sheetManagerScreen.updates;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/checkSheetVersion")
    public class CheckSheetVersionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()){
            String sheetName = request.getParameter("sheetName");

            String userVersion = SessionUtils.getViewedSheetVersion(request);
            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

            // בדיקה אם גרסת המשתמש מעודכנת
            if (!sheetEngine.isVersionUpToDate(userVersion)) {
                // אם יש גרסה חדשה, מחזירים סטטוס 200 עם הודעה פשוטה
                response.setStatus(HttpServletResponse.SC_OK);
                out.write("A new version of the sheet is available.");
            } else {
                // אם אין עדכון, מחזירים סטטוס 204
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                out.write("You are viewing the latest version of the sheet.");
            }
        } catch ( Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred: " + e.getMessage());
        }


    }
}

