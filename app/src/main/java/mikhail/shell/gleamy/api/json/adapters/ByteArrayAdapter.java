package mikhail.shell.gleamy.api.json.adapters;

import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ByteArrayAdapter implements JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String json = jsonElement.getAsString();
        return Base64.decode(json,Base64.NO_WRAP);
    }
}
