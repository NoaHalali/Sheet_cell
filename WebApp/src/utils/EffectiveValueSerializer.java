package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.lang.reflect.Type;

public class EffectiveValueSerializer implements JsonSerializer<EffectiveValue> {

    @Override
    public JsonElement serialize(EffectiveValue effectiveValue, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        // Add the cellType
        jsonObject.addProperty("cellType", effectiveValue.getCellType().name());

        // Add the value with special handling for NaN
        Object value = effectiveValue.getValue();
        switch (effectiveValue.getCellType()) {
            case NUMERIC:
                if(((Double) value).isNaN()){
                    jsonObject.addProperty("value", "NAN");
                    jsonObject.addProperty("cellType", "STRING");
                }
                else{
                    jsonObject.addProperty("value", (Double) value);
                    jsonObject.addProperty("cellType", effectiveValue.getCellType().name());
                }
                break;
            case STRING:
                jsonObject.addProperty("value", value.toString());
                jsonObject.addProperty("cellType", effectiveValue.getCellType().name());
            break;
            case BOOLEAN:
                jsonObject.addProperty("value", ((Boolean) value).booleanValue());
                jsonObject.addProperty("cellType", effectiveValue.getCellType().name());


        }
        return jsonObject;
    }
}
