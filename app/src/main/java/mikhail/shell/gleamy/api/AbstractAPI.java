package mikhail.shell.gleamy.api;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAPI {
    private AppHttpClient httpClient;
    protected final static Map<String, Activity> activities = new HashMap<>();
    public AbstractAPI()
    {
        httpClient = AppHttpClient.getClient();
    }
    public static void addActivity(String name, Activity activity)
    {
        activities.put(name, activity);
        AppHttpClient.currentActivity = activity;
    }
    public static Activity getActivity(String name)
    {
        return activities.get(name);
    }
    public AppHttpClient getHttpClient()
    {
        return  httpClient;
    }
}
