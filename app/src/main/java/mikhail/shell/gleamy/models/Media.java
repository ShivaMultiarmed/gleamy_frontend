package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Media implements Serializable {
    public enum Type implements Serializable {
        IMAGE("image/*"), VIDEO("video/*"), AUDIO("audio/*");
        public final String mime;
        Type(String mime)
        {
            this.mime = mime;
        }
    }
    public String uuid, extension;
    public Type type;
    public Long userid;
    public LocalDateTime date_time;
    public Media()
    {}
    public Media(String extension, Type type, Long userid) {
        this(null, extension, type, userid, null);
    }
    public Media(String uuid, String extension, Type type, Long userid, LocalDateTime date_time) {
        this.uuid = uuid;
        this.extension = extension;
        this.type = type;
        this.userid = userid;
        this.date_time = date_time;
    }
    public static class Builder
    {
        private String extension;
        private Type type;
        private Long userid;
        public Builder() {}
        public Builder extension(String extension)
        {
            this.extension = extension;
            return this;
        }
        public Builder type(Type type)
        {
            this.type = type;
            return this;
        }
        public Builder userid(Long userid)
        {
            this.userid = userid;
            return this;
        }
        public Media build()
        {
            return new Media(extension, type, userid);
        }
    }
}
