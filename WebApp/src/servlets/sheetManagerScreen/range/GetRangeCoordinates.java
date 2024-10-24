package servlets.sheetManagerScreen.range;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/getRangeCoordinates")
public class GetRangeCoordinates extends HttpServlet{

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");

            //String sheetName = request.getParameter("sheetName");
            String sheetName = SessionUtils.getViewedSheetName(request);
            String rangeName = request.getParameter("rangeName");

            if (sheetName == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name");
                return;
            }

            if (rangeName == null || rangeName.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty range name parameter");
                return;
            }

            System.out.println("Getting range coordinates, request URI is: " + request.getRequestURI());

            try {

                List<Coordinate> Coordinates = getRangeCoordinates(sheetName, rangeName);

                if (Coordinates == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Range Coordinates not found for range: " + rangeName);
                } else {
                    Gson gson = new Gson();
                    String json = gson.toJson(Coordinates);
                    response.getWriter().write(json);
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
            }

        }

        private List<Coordinate> getRangeCoordinates(String sheetName ,String  rangeName) {
            SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
            return sheetEngine.getRangeCoordinates(rangeName);
        }
//        private List<Coordinate> getRangeCoordinates(String sheetName ,String  rangeName) {
//            MultiSheetEngineManager engineManager = ServletUtils.getMultiSheetEngineManager(getServletContext());
//            SheetEngine sheetEngine = engineManager.getSheetEngine(sheetName);
//            return sheetEngine.getRangeCoordinates(rangeName);
//        }
}
