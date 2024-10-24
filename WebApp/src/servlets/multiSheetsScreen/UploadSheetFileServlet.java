package servlets.multiSheetsScreen;

import com.google.gson.Gson;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import shticell.files.FileManager;
import shticell.sheets.manager.MultiSheetEngineManager;
import shticell.sheets.sheet.Sheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.util.*;

@WebServlet("/uploadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)

public class UploadSheetFileServlet extends HttpServlet  {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        Collection<Part> parts = request.getParts();
        //out.println("Total parts : " + parts.size());

        List<InputStream> streams = new ArrayList<>(); //new
        StringBuilder fileStringContent = new StringBuilder();

        for (Part part : parts) {
            //to write the content of the file to a string
            fileStringContent.append(readFromInputStream(part.getInputStream()));
            streams.add(part.getInputStream()); //new
        }
        System.out.println("On upload file, request URI is: " + request.getRequestURI());
        //printFileContent(fileStringContent.toString(), out);

        if (!streams.isEmpty()) {
            // מיזוג כל הזרמים לזרם אחד
            InputStream concatenatedStream = new SequenceInputStream(Collections.enumeration(streams));

            try {
                FileManager fileManager = ServletUtils.getFileManager(getServletContext());
                Sheet sheet = fileManager.processFile(concatenatedStream);
                String sheetName = sheet.getSheetName();
                MultiSheetEngineManager multiSheetEngineManager = ServletUtils.getMultiSheetEngineManager(getServletContext());

                synchronized (this) {
                    if (multiSheetEngineManager.isSheetNameExists(sheetName)) {
                        String errorMessage = "Sheet with name: '" + sheetName + "' already exists. Please choose a file with different sheet name.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        //response.getOutputStream().print(errorMessage);
                        response.getWriter().println(errorMessage);


                    } else {
                        //add the new sheet to the multiSheetEngineManager
                        multiSheetEngineManager.addSheetEngine(sheet, SessionUtils.getUsername(request));

                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                //response.getOutputStream().print(errorMessage);
                response.getWriter().println(e.getMessage());
            }
        }
        else
        {
            out.println("No file part found in the request.");
        }
    }

    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Parameter Name: ").append(part.getName()).append("\n")
                .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
                .append("Size (of the file): ").append(part.getSize()).append("\n")
                .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }

        out.println(sb.toString());
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }

}
