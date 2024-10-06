package client.components.Utils.deserializer;

import com.google.gson.*;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;

import java.lang.reflect.Type;

public class EffectiveValueDeserializer implements JsonDeserializer<EffectiveValue> {

    @Override
    public EffectiveValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // שולף את סוג התא והערך מתוך ה-JSON
        String cellTypeStr = jsonObject.get("cellType").getAsString();
        CellType cellType = CellType.valueOf(cellTypeStr); // assuming CellType is an enum
        //Object value = jsonObject.get("value").getAsString(); // אם הערך הוא מחרוזת, התאימי את זה לסוג המתקבל
        Object value = null;

        switch (cellType) {
            case NUMERIC:
                value = jsonObject.get("value").getAsDouble();
                break;
            case BOOLEAN:
                value = jsonObject.get("value").getAsBoolean();
                break;
            case STRING:
                value = jsonObject.get("value").getAsString();
                break;
        }

        // מחזיר את המימוש של EffectiveValueImpl
        return new EffectiveValueImpl(cellType, value);
    }
}

