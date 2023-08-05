package mikhail.shell.gleamy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.models.UserInfo;

public class UserAPIClient extends AbstractAPI{
    private static UserAPIClient client;
    private UserApi userApi;
    private List<UserInfo> uinfos;

    private UserAPIClient()
    {
        uinfos = new ArrayList<>();
        uinfos.add(new UserInfo("George", "123"));
        uinfos.add(new UserInfo("Gean", "qwerty"));
        uinfos.add(new UserInfo("Sam", "123"));
        uinfos.add(new UserInfo("Sarah", "123"));
    }
    public static UserAPIClient getClient()
    {
        if (client==null)
            client = new UserAPIClient();
        return client;
    }
    public List<UserInfo> getUsersByLogin(String login)
    {
        List<UserInfo> response = new ArrayList<>();
        Pattern pattern = Pattern.compile(login+".+?");
        Matcher matcher;
        for (UserInfo info : uinfos)
        {
            matcher = pattern.matcher(info.getLogin());
            if(matcher.find())
                response.add(info);
        }


        return response;
    }
}
