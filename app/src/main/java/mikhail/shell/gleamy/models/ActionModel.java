package mikhail.shell.gleamy.models;

import java.io.Serializable;

public class ActionModel<T> {
    private String action;
    private T object;
    public ActionModel(String action, T object)
    {
        this.action = action;
        this.object = object;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
