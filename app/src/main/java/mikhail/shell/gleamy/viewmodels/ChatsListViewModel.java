package mikhail.shell.gleamy.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;
import java.util.Set;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.repositories.ChatsRepository;

public class ChatsListViewModel extends ViewModel {
    private final static String TAG = "CHATS_VIEW_MODEL";
    private final MutableLiveData<Map<Long, Chat>> chatsLiveData;
    private ChatsRepository chatsRepo;

    public ChatsListViewModel() {
        Context appContext  = GleamyApp.getApp().getApplicationContext();
        chatsRepo = ChatsRepository.getInstance(appContext);
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
            long chatid = chatids.stream().skip(chatids.size()-1).findFirst().get(); // last chat is the first because chats are retrieved reversed
            return getAllChats().get(chatid);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
    public void logout()
    {
        chatsRepo.logout();
    }
}
