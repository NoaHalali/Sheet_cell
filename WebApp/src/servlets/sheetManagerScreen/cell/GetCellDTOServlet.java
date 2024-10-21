package servlets.sheetManagerScreen.cell;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Request;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parts.cell.CellDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.exceptions.OutdatedSheetVersionException;
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

        String sheetName = request.getParameter("sheetName");
        String coordStr = request.getParameter("cellID");


        if (coordStr == null || coordStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty coordStr parameter");
            return;
        }

        System.out.println("Getting CellDTO, request URI is: " + request.getRequestURI());

        try {
            Coordinate coordinate = CoordinateImpl.parseCoordinate(coordStr);
            CellDTO cellDTO = getCellDTOByCoordinate(sheetName, coordinate,request);

            if (cellDTO == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cell not found for coordinate: " + coordStr);
            } else {
                // סידור ה-CellDTO ל-JSON (לא צריך Deserializers כאן)
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())
                        .create();
                String json = gson.toJson(cellDTO);
                response.getWriter().write(json);
            }
        }
        catch (OutdatedSheetVersionException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"OutdatedSheetVersion\":"+e.getMessage()+ "\"}");
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
        }
    }

    private CellDTO getCellDTOByCoordinate(String sheetName , Coordinate coordinate, HttpServletRequest request) {

        SheetEngine sheetEngine = ServletUtils.getSheetEngineByName(sheetName, getServletContext());
        String userVersion= SessionUtils.getViewedSheetVersion(request);
        sheetEngine.checkIfVersionIsUpdated(userVersion);
        return sheetEngine.getCellDTOByCoordinate(coordinate);
    }

}
