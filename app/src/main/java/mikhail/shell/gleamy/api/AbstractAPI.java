package mikhail.shell.gleamy.api;

public abstract class AbstractAPI {
    protected AppHttpClient httpClient;
    public AbstractAPI()
    {
        httpClient = AppHttpClient.getClient();
    }
}
