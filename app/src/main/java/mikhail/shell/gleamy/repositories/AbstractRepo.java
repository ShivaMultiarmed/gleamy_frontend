package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.api.WebClient;

public abstract class AbstractRepo {
    protected final WebClient webClient;

    public AbstractRepo()
    {
        webClient = WebClient.getInstance();
    }
}
