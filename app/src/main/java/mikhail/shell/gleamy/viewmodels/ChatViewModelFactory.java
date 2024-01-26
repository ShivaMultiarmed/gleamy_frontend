package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import mikhail.shell.gleamy.models.ChatInfo;

public class ChatViewModelFactory implements ViewModelProvider.Factory {
    private final ChatInfo chat;
    public ChatViewModelFactory(ChatInfo chat)
    {
        this.chat = chat;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> type)
    {
        return (T) new ChatViewModel(chat);
    }
}
