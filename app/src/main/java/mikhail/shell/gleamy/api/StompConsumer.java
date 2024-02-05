package mikhail.shell.gleamy.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.dto.StompMessage;

public class StompConsumer implements Consumer<StompMessage> {
    private final static String TAG = StompConsumer.class.getName();
    private final List<Consumer> subConsumers;
    public StompConsumer()
    {
        subConsumers = new ArrayList<>();
    }
    @Override
    public void accept(StompMessage message) {
        subConsumers.stream().forEach(consumer -> {
            try {
                consumer.accept(message);
            } catch (Exception e) {
                Log.e(TAG, "Error while running consumers.");
            }
        });
    }
    public void addSubConsumer(Consumer<StompMessage> consumer)
    {
        subConsumers.add(consumer);
    }
}