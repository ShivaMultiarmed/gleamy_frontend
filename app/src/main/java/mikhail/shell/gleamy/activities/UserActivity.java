package mikhail.shell.gleamy.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ActivityUserBinding;
import mikhail.shell.gleamy.fragments.UserImagesFragment;
import mikhail.shell.gleamy.fragments.UserMediaFragment;
import mikhail.shell.gleamy.fragments.UserVideosFragment;
import mikhail.shell.gleamy.utils.MediaUtils;
import mikhail.shell.gleamy.utils.ImageUtils;
import mikhail.shell.gleamy.viewmodels.MediaViewModel;
import mikhail.shell.gleamy.viewmodels.TheUserViewModel;
import mikhail.shell.gleamy.views.OptionView;

public class UserActivity extends AppCompatActivity {
    private boolean isMine;
    private ActivityUserBinding B;
    private TheUserViewModel userViewModel;
    private MediaViewModel mediaViewModel;
    private Long userid;
    private ActivityResultLauncher<String> imagePickerLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        userid = getIntent().getLongExtra("userid", 0);
        isMine = getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0) == userid;

        initViewModels();

        observeUser();

        B.userPageAvatar.setOnClickListener( avatarView -> {
            Toast.makeText(this, "Openning an avatar", Toast.LENGTH_SHORT).show();
        });

        B.userPageAvatar.setOnLongClickListener(avatarView -> {
            if (isMine)
                openImageDialog();
            return true;
        });

        initImagePickerLauncher();

        initTabs(this::changeTab);
        changeTab(B.userImgsTab);
    }
    private void initViewModels()
    {

        TheUserViewModel.UserViewModelFactory userViewModelFactory = new TheUserViewModel.UserViewModelFactory(userid);
        userViewModel = new ViewModelProvider(this, userViewModelFactory)
                .get(TheUserViewModel.class);

    }
    private void initImagePickerLauncher()
    {
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri != null)
                    {
                        try{
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                byte[] bytes = MediaUtils.getFileContent(getContentResolver(), uri);
                                String extension = MediaUtils.getExtension(getContentResolver(), uri);
                                userViewModel.editAvatar(this, extension, bytes,
                                        response -> {
                                            Bitmap bitmap = ImageUtils.getCircleBitmap(bytes);
                                            B.userPageAvatar.setImageBitmap(bitmap);
                                        }
                                );
                            }
                        }catch(FileNotFoundException e)
                        {
                            Log.e("UserActivity", "File not found.");
                        }
                        catch (IOException e)
                        {
                            Log.e("UserActivity", "File not read.");
                        }
                    }
                    else
                        Log.i("UserActivity", "None of files was chosen.");
                }
        );
    }
    private void observeUser()
    {
        userViewModel.getUser(user -> {
            B.setUser(user);
            userViewModel.getAvatar(this, this::setAvatar);
        });
    }
    private void changeTab(View tab)
    {
        final int containerid = R.id.userContent;
        int anotherid =  B.userContent.getId();
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(containerid, getFragment(tab.getId()), null)
                .commit();
    }
    private UserMediaFragment<?> getFragment(int containerid)
    {
        final boolean isPrivileged =
                userid == getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0);
        return
                switch(containerid)
                {
                    case R.id.userImgsTab -> new UserImagesFragment(userid, isPrivileged);
                    case R.id.userVidsTab -> new UserVideosFragment(userid, isPrivileged);
                    default -> null;
                };
    }
    private void initTabs(View.OnClickListener listener)
    {
        for (int i=0; i< B.tabsBar.getChildCount(); i++)
            B.tabsBar.getChildAt(i).setOnClickListener(listener);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void setAvatar(byte[] imageBytes)
    {
        if (imageBytes != null)
        {
            Bitmap bitmap = ImageUtils.getCircleBitmap(imageBytes);
            B.userPageAvatar.setImageBitmap(bitmap);
        }
    }
    private void openImageSelector()
    {
        imagePickerLauncher.launch("image/*");
    }
    private void openImageDialog()
    {
        LinearLayout dialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_view, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        OptionView optionView1 = new OptionView(this);
        optionView1.setText("Обновить фото");

        dialogLayout.addView(optionView1);

        OptionView optionView2 = new OptionView(this);
        optionView2.setText("Удалить фото");

        dialogLayout.addView(optionView2);

        builder.setView(dialogLayout);

        AlertDialog dialog = builder.create();

        optionView1.setOnClickListener(btn -> {
            openImageSelector();
            dialog.dismiss();
        });

        long userid = getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0);
        optionView2.setOnClickListener(btn -> userViewModel.removeAvatar(this,userid, result -> {
            if (result)
            {
                B.userPageAvatar.setImageBitmap(null);
                B.userPageAvatar.setBackgroundResource(R.drawable.default_user_icon_42);
            }

            dialog.dismiss();
        }));

        dialog.show();
    }
}