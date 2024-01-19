package mikhail.shell.gleamy.api.json.adapters;

import android.os.Build;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public final class DateTimeAdapter implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
    {
        return parse(jsonElement.getAsString()+"+00:00");
    }
    public LocalDateTime parse(String json)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZoneId zoneId = TimeZone.getDefault().toZoneId();
            return ZonedDateTime.parse(json).withZoneSameInstant(zoneId).toLocalDateTime();
        }
        else {
            return null;
        }
    }
}
