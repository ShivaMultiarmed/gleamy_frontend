package mikhail.shell.gleamy.repositories;

import android.content.Context;

import mikhail.shell.gleamy.api.WebClient;

public abstract class AbstractRepository {
    protected final WebClient webClient;
    protected final Context appContext;

    public AbstractRepository(Context context)
    {
        appContext = context.getApplicationContext();
        webClient = WebClient.getInstance(context);
    }
}
