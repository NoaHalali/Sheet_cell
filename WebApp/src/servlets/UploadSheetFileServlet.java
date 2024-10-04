package servlets;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlType;
import shticell.files.FileManager;
import shticell.sheets.sheet.Sheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
//@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)

public class UploadSheetFileServlet extends HttpServlet  {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        // קבלת חלקי הבקשה (הקובץ)
        Part filePart = request.getPart("file");
        InputStream fileContent = filePart.getInputStream();
        System.out.println("hola");
        // שימוש במנהל הקבצים שלך

        try {

            FileManager fileManager = ServletUtils.getFileManager(getServletContext());
            Sheet sheet = fileManager.processFile(fileContent);//fail here because load xml method wont even enter !!!
            out.println("File processed successfully.");

        } catch (Exception e) {
            out.println("Error processing file: " + e.getMessage());
        }
    }

}
