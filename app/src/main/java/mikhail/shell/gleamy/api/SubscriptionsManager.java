package mikhail.shell.gleamy.api;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionsManager {
    private static SubscriptionsManager manager;
    private final Map<String, StompConsumer> consumers;
    private SubscriptionsManager()
    {
        consumers = new HashMap<>();
    }
    public static SubscriptionsManager getInstance()
    {
        if (manager == null)
            manager = new SubscriptionsManager();
        return manager;
    }
    public void addConsumer(String topic)
    {
        if (consumerExists(topic))
            consumers.put(topic, new StompConsumer());
    }
    public StompConsumer getConsumer(String topic)
    {
        return consumers.get(topic);
    }
    public boolean consumerExists(String topic)
    {
        return  consumers.containsKey(topic);
    }
}
