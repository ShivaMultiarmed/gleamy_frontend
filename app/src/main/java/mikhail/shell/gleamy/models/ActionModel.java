package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Map;

public class ActionModel<T> {
    private String action;
    private T object;
    private Map<String, String> details;
    public ActionModel(String action, T object, Map<String, String> details)
    {
        this.action = action;
        this.object = object;
        this.details = details;
    }

    public ActionModel(String action, T object)
    {
        this(action, object, null);
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
    public Map<?,?> getDetails()
    {
        return details;
    }
}
