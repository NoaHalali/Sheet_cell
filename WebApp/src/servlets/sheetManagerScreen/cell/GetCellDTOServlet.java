package servlets.sheetManagerScreen.cell;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.cell.CellDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import utils.EffectiveValueSerializer;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet("/getCellDTO")
public class GetCellDTOServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson =new Gson();

        //String sheetName = request.getParameter("sheetName");
        String sheetName = SessionUtils.getViewedSheetName(request);
        String coordStr = request.getParameter("cellID");

        if(sheetName == null){
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name parameter");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json= gson.toJson("Missing sheet name parameter");
            response.getWriter().write( json );
            return;
        }

        if (coordStr == null) {
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing coordinate parameter");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json= gson.toJson("Missing coordinate parameter");
            response.getWriter().write( json );
            return;
        }


        try {

            Coordinate coordinate = CoordinateImpl.parseCoordinate(coordStr);
            CellDTO cellDTO = getCellDTOByCoordinate(sheetName, coordinate,request);

            if (cellDTO == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cell not found for coordinate: " + coordStr);
            } else {
                Gson gsonBuilder = new GsonBuilder()
                        .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                        .create();
                String json = gsonBuilder.toJson(cellDTO);
                response.getWriter().write(json);
            }
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json= gson.toJson(e.getMessage());
            response.getWriter().write( json );
        }
    }

    private CellDTO getCellDTOByCoordinate(String sheetName , Coordinate coordinate, HttpServletRequest request) throws Exception {

        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        //ServletUtils.checkIfClientSheetVersionIsUpdated(request,sheetEngine);
        String versionStr = SessionUtils.getViewedSheetVersion(request);
        try {
            int version = Integer.parseInt(versionStr);
            return sheetEngine.getCellDTOByVersion(coordinate,version);
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("Invalid version number: " + versionStr);
        }
    }

}
