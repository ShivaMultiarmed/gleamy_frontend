package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.api.UserApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChatViewModel extends ViewModel {
    private final WebClient webClient;
    private final MutableLiveData<Map<Long, UserInfo>> usersData;
    private final MutableLiveData<String> statusData;
    private final UserApi userApi;
    private final ChatApi chatApi;
    private final ChatInfo chat;
    public CreateChatViewModel(UserInfo user)
    {
        webClient= WebClient.getInstance();

        userApi = webClient.createRetrofit(UserApi.class);
        chatApi = webClient.createRetrofit(ChatApi.class);

        chat = new ChatInfo();
        usersData = new MutableLiveData<>();
        addMember(user);

        statusData = new MutableLiveData<>();
    }
    public ChatInfo getChat() { return chat; }
    public void observeStatus(LifecycleOwner subscriber, Observer<String> observer)
    {
        statusData.observe(subscriber, observer);
    }
    public void observeChatMembers(LifecycleOwner subscriber, Observer<Map<Long, UserInfo>> observer)
    {
        usersData.observe(subscriber, observer);
    }
    public void getUsersByLogin(String login)
    {
        Call<Map<Long, UserInfo>> request = userApi.getUsersByLogin(login);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Map<Long, UserInfo>> call, Response<Map<Long, UserInfo>> response) {
                        usersData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Map<Long, UserInfo>> call, Throwable t) {
                        usersData.postValue(null);
                    }
                }
        );
    }
    public void postChat()
    {
        Call<Map<String, String>> request = chatApi.addChat(chat);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        statusData.postValue("OK");
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        statusData.postValue("ERROR");
                    }
                }
        );
    }
    public void addMember(UserInfo user)
    {
        usersData.getValue().put(user.getId(), user);
    }

    public static class Factory implements ViewModelProvider.Factory
    {
        private final UserInfo user;
        public Factory(UserInfo user)
        {
            this.user = user;
        }
        @Override
        public <T extends  ViewModel> T create(Class<T> type)
        {
            return  (T) new CreateChatViewModel(user);
        }
    }
}
