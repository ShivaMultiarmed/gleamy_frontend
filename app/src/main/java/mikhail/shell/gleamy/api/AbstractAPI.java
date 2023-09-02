package mikhail.shell.gleamy.api;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAPI {
    protected AppHttpClient httpClient;
    protected final Map<String, Activity> activities;
    public AbstractAPI()
    {
        httpClient = AppHttpClient.getClient();
        activities = new HashMap<>();
    }
    public void addActivity(String name, Activity activity)
    {
        activities.put(name, activity);
        getHttpClient().currentActivity = activity;
    }
    public AppHttpClient getHttpClient()
    {
        return  httpClient;
    }
}
