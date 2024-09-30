package servlets;

import shticell.files.FileManager;
import shticell.sheets.sheet.Sheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class UploadSheetFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        // קבלת חלקי הבקשה (הקובץ)
        Part filePart = request.getPart("file");
        InputStream fileContent = filePart.getInputStream();

        // שימוש במנהל הקבצים שלך
        FileManager fileManager = new FileManager();
        try {
            Sheet sheet = fileManager.processFile(fileContent);
            out.println("File processed successfully.");
        } catch (Exception e) {
            out.println("Error processing file: " + e.getMessage());
        }
    }

}
