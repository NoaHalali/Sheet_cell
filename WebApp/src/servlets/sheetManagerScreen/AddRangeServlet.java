package servlets.sheetManagerScreen;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.manager.MultiSheetEngineManager;
import utils.ServletUtils;

import java.io.IOException;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet("/addRange")
public class AddRangeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // הגדרת סוג התגובה ל-JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // טעינת הפרמטרים מתוך ה-body של הבקשה באמצעות Properties
        Properties prop = new Properties();
        try (InputStream inputStream = req.getInputStream()) {
            prop.load(inputStream);
        }

        // קריאת הערכים מתוך ה-Properties
        String sheetName = prop.getProperty("sheetName");
        String rangeName = prop.getProperty("rangeName");
        String rangeDefinition = prop.getProperty("rangeDefinition");

        // בדיקה שהפרמטרים התקבלו
        if (rangeName == null || rangeDefinition == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing rangeName or rangeDefinition\"}");
            return;
        }

        // כאן תבצעי את הפעולה במנוע (נניח שיש לך מחלקה שנקראת 'engine' עם מתודה 'addRange')
        try {
            // קריאה למנוע להוספת הטווח
           addRange(sheetName, rangeName, rangeDefinition);

            // החזרת תגובה מוצלחת
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Range added successfully\"}");
        } catch (IllegalArgumentException e) {
            // טיפול במקרה של שגיאה בקלט
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Failed to add range: " + e.getMessage() + "\"}");
        }
        catch(Exception e) {
            // טיפול במקרה של שגיאה במנוע
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Failed to add range: " + e.getMessage() + "\"}");
        }
    }

    private void addRange(String sheetName, String rangeName, String rangeDefinition) throws IllegalArgumentException {
        MultiSheetEngineManager engineManager = ServletUtils.getMultiSheetEngineManager(getServletContext());
        SheetEngine sheetEngine = engineManager.getSheetEngine(sheetName);
        sheetEngine.addRange(rangeName, rangeDefinition);
    }
}
