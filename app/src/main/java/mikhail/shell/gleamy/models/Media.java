package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Media implements Serializable {
    public String uuid;
    public String type, extension;
    public Long userid;
    public LocalDateTime date_time;
}
