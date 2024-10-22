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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String sheetName = request.getParameter("sheetName");

        String userVersion = SessionUtils.getViewedSheetVersion(request);
        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());

        // בדיקה אם גרסת המשתמש מעודכנת
        if (!sheetEngine.isVersionUpToDate(userVersion)) {
            // אם יש גרסה חדשה, מחזירים סטטוס 200 עם הודעה פשוטה
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("A new version of the sheet is available. Please refresh.");
        } else {
            // אם אין עדכון, מחזירים סטטוס 204
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}

