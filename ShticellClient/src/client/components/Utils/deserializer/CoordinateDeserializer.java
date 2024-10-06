package client.components.Utils.deserializer;

import com.google.gson.*;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;

import java.lang.reflect.Type;

public class CoordinateDeserializer implements JsonDeserializer<Coordinate> {

    @Override
    public Coordinate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int row = jsonObject.get("row").getAsInt();
        int column = jsonObject.get("column").getAsInt();

        // מחזיר את המימוש של CoordinateImpl
        return new CoordinateImpl(row, column);
    }
}
