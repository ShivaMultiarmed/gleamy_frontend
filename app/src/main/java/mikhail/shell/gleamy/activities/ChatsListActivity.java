package mikhail.shell.gleamy.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.databinding.ChatsListActivityBinding;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.viewmodels.TheUserViewModel;
import mikhail.shell.gleamy.views.ChatView;
import mikhail.shell.gleamy.viewmodels.ChatsListViewModel;
import mikhail.shell.gleamy.viewmodels.UserViewModel;

public class ChatsListActivity extends AppCompatActivity {
    private ChatsListActivityBinding B;
    private static final int CREATE_CHAT_INTENT = 1;
    private Map<Long,ChatView> chats;
    private OpenChatListener openChatListener;
    private ChatsListViewModel chatsListViewModel;
    private UserViewModel userViewModel;
    private TheUserViewModel theUserViewModel;
    private ActivityResultLauncher createChatLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ChatsListActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        retrieveBundle();
        initViewModels();
        chats = new LinkedHashMap<>();
        initViews();

        chatsListViewModel.fetchAllChatsFromREST();

        initCreateChatLauncher();

        B.profileBtn.setOnClickListener(btn -> {
            long userid = getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0);
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("userid", userid);
            startActivity(intent);
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();

    }
    private void retrieveBundle()
    {
        //Bundle b = getIntent().getExtras();
        //userid = b.getLong("userid");
    }
    private void initViewModels()
    {
        chatsListViewModel = ViewModelProviders.of(this).get(ChatsListViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        long userid = getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0);
        TheUserViewModel.UserViewModelFactory factory = new TheUserViewModel.UserViewModelFactory(userid);
        theUserViewModel = new ViewModelProvider(this, factory)
                .get(TheUserViewModel.class);

        chatsListViewModel.getChatsLiveData().observe(this,
                chatMap ->
                {
                    /*if (!chats.isEmpty())
                        removeEmptyMessage();*/

                    addAllChats(chatMap);

                    chatsListViewModel.getChatsLiveData().removeObservers(this);

                    chatsListViewModel.getChatsLiveData().observeForever(updatedChats -> {
                        Chat lastChat = chatsListViewModel.getLastChat();
                        if (lastChat != null)
                        {
                            if (!chats.containsKey(lastChat.getId()))
                                addChat(lastChat);
                            elevateChat(lastChat);
                        }
                    });
                }
        );

    }
    private void initViews()
    {
        B.addChatBtn.setOnClickListener(v-> createChat());
        theUserViewModel.getAvatar(this, bytes->{
            if (bytes != null)
                B.profileBtn.setImageBitmap(getCircleBitmap(bytes));
        });
        B.logoutbtn.setOnClickListener(v -> logout());
        openChatListener = new OpenChatListener();
    }
    private void addAllChats(Map<Long, Chat> chats)
    {
        if (!chats.isEmpty())
            chats.values().forEach(this::addChat);

        ChatView chatView = this.chats.values().stream()
                .skip(this.chats.size()-1)
                .findFirst().orElse(null); // first chatview that should be at the bottom
        if (chatView != null)
            lowerChat(chatView.getChat());
    }
    private ChatView createChatView(Chat chat)
    {
        ChatView chatView = new ChatView(this, chat);
        chatView.setOnClickListener(openChatListener);
        return chatView;
    }
    private void displayChat(ChatView chatView)
    {
        B.chatsListContent.addView(chatView);
    }
    public void addChat(Chat chat)
    {
        ChatView chatView = createChatView(chat);
        chats.put(chat.getId(), chatView);
        displayChat(chatView);
    }
    private void createChat()
    {
        long userid = GleamyApp.getApp().getUser().getId();
        createChatLauncher.launch(userid);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode)
            {
                case CREATE_CHAT_INTENT -> {
                    Bundle b = data.getExtras();
                    Chat chat = (Chat) b.getSerializable("chatinfo");
                    addChat(chat);
                }
                default -> {}
            }
    }

    private void openChat(Chat chat)
    {
        Intent openChat = new Intent(this, ChatActivity.class);
        Bundle info = new Bundle();
        long userid = GleamyApp.getApp().getUser().getId();
        info.putLong("userid", userid);
        info.putSerializable("chatInfo", chat);
        openChat.putExtras(info);
        startActivity(openChat);
    }

    public void removeEmptyMessage()
    {
        B.chatsListContent.removeAllViews();
    }
    public boolean isEmpty()
    {
        return chats.isEmpty();
    }

    private class OpenChatListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            ChatView chatView = (ChatView) view;
            openChat(chatView.getInfo());
        }
    }
    void elevateChat(Chat chat)
    {
        long chatid = chat.getId();
        ChatView chatView = getChatView(chatid);
        chatView.setChat(chat);
        B.chatsListContent.removeView(chatView);
        B.chatsListContent.addView(chatView, 0);
    }
    void lowerChat(Chat chat)
    {
        long chatid = chat.getId();
        ChatView chatView = getChatView(chatid);
        B.chatsListContent.removeView(chatView);
        B.chatsListContent.addView(chatView);
    }
    ChatView getChatView(long chatid)
    {
        return chats.get(chatid);
    }
    Chat getChat(long chatid)
    {
        return getChatView(chatid).getInfo();
    }
    public Chat getLatestChat()
    {
        return null;
    }
    private void initCreateChatLauncher()
    {
        ActivityResultContract<Long, Void> contract = new ActivityResultContract<>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Long userid) {
                Intent chatCreation = new Intent(context, CreateChatActivity.class);
                chatCreation.putExtra("userid", userid);
                return chatCreation;
            }

            @Override
            public Void parseResult(int i, @Nullable Intent intent) {
                return null;
            }
        };
        ActivityResultCallback<Void> callback = (aVoid) -> {};
        createChatLauncher = registerForActivityResult(contract, callback);
    }
    private void logout()
    {
        chatsListViewModel.logout();
        SharedPreferences.Editor editor = getSharedPreferences("authdetails", MODE_PRIVATE).edit();
        editor.remove("userid");
        editor.apply();
        goToAuth();
    }
    private void goToAuth()
    {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
    private Bitmap getCircleBitmap(byte[] imageBytes) {

        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return circleBitmap;
    }
}