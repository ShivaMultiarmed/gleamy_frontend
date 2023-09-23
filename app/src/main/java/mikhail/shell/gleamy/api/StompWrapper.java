package mikhail.shell.gleamy.api;

import java.io.Serializable;

public final class StompWrapper implements Serializable {
    private final String msgType;
    private final Serializable payload;
    public StompWrapper(String msgType,Serializable payload)
    {
        this.msgType = msgType;
        this.payload = payload;
    }

    public String getMsgType() {
        return msgType;
    }

    public Serializable getPayload() {
        return payload;
    }
}
