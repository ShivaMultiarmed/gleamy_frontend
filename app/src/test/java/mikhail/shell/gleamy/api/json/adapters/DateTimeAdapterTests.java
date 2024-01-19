package mikhail.shell.gleamy.api.json.adapters;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeAdapterTests {

    private final DateTimeAdapter adapter = new DateTimeAdapter();

    @Test
    public void checkDateTimeParsing()
    {
        String entry = "2011-12-03T10:15:30";
        LocalDateTime expected = LocalDateTime.of(2011, 12, 3, 10, 15, 30);
        System.out.println(expected.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        LocalDateTime dateTime = adapter.parse(entry);
        Assert.assertEquals(expected, dateTime);
    }
}
