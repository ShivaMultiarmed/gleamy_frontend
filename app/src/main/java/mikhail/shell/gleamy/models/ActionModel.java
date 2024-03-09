package mikhail.shell.gleamy.models;

import java.util.Map;

public class ActionModel<T> {
    private String action;
    private T model;
    private Map<String, String> details;
    public ActionModel(String action, T model, Map<String, String> details)
    {
        this.action = action;
        this.model = model;
        this.details = details;
    }

    public ActionModel(String action, T model)
    {
        this(action, model, null);
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
    public Map<?,?> getDetails()
    {
        return details;
    }
}
