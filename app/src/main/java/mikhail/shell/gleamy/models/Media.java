package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Media implements Serializable {
    public enum Type { IMAGE, VIDEO, AUDIO }
    public String uuid, extension;
    public Type type;
    public Long userid;
    public LocalDateTime date_time;
}
