package mikhail.shell.gleamy.repositories;

import android.content.Context;

import mikhail.shell.gleamy.api.WebClient;

public abstract class AbstractRepo {
    protected final WebClient webClient;
    protected final Context appContext;

    public AbstractRepo(Context context)
    {
        appContext = context.getApplicationContext();
        webClient = WebClient.getInstance(context);
    }
}
