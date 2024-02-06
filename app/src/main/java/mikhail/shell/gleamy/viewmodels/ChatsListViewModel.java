package mikhail.shell.gleamy.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsListViewModel extends ViewModel {
    private final static String TAG = "CHATS_VIEW_MODEL";
    private final MutableLiveData<Map<Long, Chat>> chatsLiveData;
    private ChatsRepo chatsRepo;

    public ChatsListViewModel() {
        chatsRepo = new ChatsRepo();
        chatsLiveData = new MutableLiveData<>();
    }
    public LiveData<Map<Long, Chat>> getChatsLiveData() {
        return chatsLiveData;
    }
    public Map<Long, Chat> getAllChats() {
        return chatsLiveData.getValue();
    }
    public void fetchAllChatsFromREST()
    {
        chatsRepo.fetchAllChats(chatsLiveData, GleamyApp.getApp().getUser().getId());
    }
    @Override
    protected void onCleared()
    {
        Log.i(TAG, "The viewmodel is cleared.");
    }
    public Chat getLastChat()
    {
        try {
            Set<Long> chatids = getAllChats().keySet();
            long chatid = chatids.stream().skip(chatids.size()-1).findFirst().get();
            return getAllChats().get(chatid);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

}
