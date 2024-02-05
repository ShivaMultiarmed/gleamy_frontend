package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.UserRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChatViewModel extends ViewModel {
    private final MutableLiveData<Map<Long, User>> usersData;
    private final MutableLiveData<String> statusData;
    private final Chat chat;
    private final ChatsRepo chatsRepo;
    private final UserRepo usersRepo;
    public CreateChatViewModel(User user)
    {
        chatsRepo = new ChatsRepo();
        usersRepo = new UserRepo();

        chat = new Chat();
        chat.addMember(user);

        usersData = new MutableLiveData<>();
        statusData = new MutableLiveData<>();
    }
    public Chat getChat()
    {
        return chat;
    }
    public void observeStatus(LifecycleOwner subscriber, Observer<String> observer)
    {
        statusData.observe(subscriber, observer);
    }
    public void observeChatMembers(LifecycleOwner subscriber, Observer<Map<Long, User>> observer)
    {
        usersData.observe(subscriber, observer);
    }
    public void getUsersByLogin(String login)
    {
        usersRepo.getUsersByLogin(usersData, login);
    }
    public void addChat()
    {
        chatsRepo.postChat(statusData, chat);
    }

    public static class Factory implements ViewModelProvider.Factory
    {
        private final User user;
        public Factory(User user)
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
