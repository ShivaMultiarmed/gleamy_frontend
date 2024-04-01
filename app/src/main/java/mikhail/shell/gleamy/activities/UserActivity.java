package mikhail.shell.gleamy.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ActivityUserBinding;
import mikhail.shell.gleamy.fragments.UserImagesFragment;
import mikhail.shell.gleamy.fragments.UserVideosFragment;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.viewmodels.MediaViewModel;
import mikhail.shell.gleamy.viewmodels.TheUserViewModel;
import mikhail.shell.gleamy.views.DialogView;
import mikhail.shell.gleamy.views.OptionView;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding B;
    private TheUserViewModel userViewModel;
    private MediaViewModel mediaViewModel;
    private Long userid;
    private final static Map<Integer, Class<? extends Fragment>> tabLayouts = new HashMap<>();
    static
    {
        tabLayouts.put(R.id.userImgsTab, UserImagesFragment.class);
        tabLayouts.put(R.id.userVidsTab, UserVideosFragment.class);
    }
    private ActivityResultLauncher<String> imagePickerLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        userid = getIntent().getLongExtra("userid", 0);

        initViewModels();

        observeUser();


        B.userPageAvatar.setOnClickListener( avatarView -> {
            Toast.makeText(this, "Openning an avatar", Toast.LENGTH_SHORT).show();
        });

        B.userPageAvatar.setOnLongClickListener(avatarView -> {
            long ownUserId = getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0);
            if (userid == ownUserId && ownUserId != 0)
                openImageDialog();
            return true;
        });

        initImagePickerLauncher();

        initTabs(changeTabListener());
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
                                byte[] bytes = getFileContent(uri);
                                String extension = getExtension(uri);
                                userViewModel.editAvatar(this, extension, bytes,
                                        response -> {
                                            Bitmap bitmap = getCircleBitmap(bytes);
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
    private byte[] getFileContent(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        byte[] content = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            content = inputStream.readAllBytes();
        }
        inputStream.close();
        return content;
    }
    private String getExtension(Uri uri)
    {
        String mimeType = getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }
    private void observeUser()
    {
        userViewModel.getUser(user -> {
            Log.i("UserActivity", user.toString());
            B.setUser(user);
            userViewModel.getAvatar(this, this::setAvatar);
        });
    }
    private <T extends Fragment> View.OnClickListener changeTabListener()
    {
        final int containerid = R.id.userContent;
        int anotherid =  B.userContent.getId();
        Log.d("UserActivity",String.valueOf(anotherid));
        return tab -> getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(containerid, tabLayouts.get(tab.getId()), null)
                .commit();
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
            Bitmap bitmap = getCircleBitmap(imageBytes);
            B.userPageAvatar.setImageBitmap(bitmap);
        }
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
    private void openImageSelector()
    {
        imagePickerLauncher.launch("image/*");
    }
    private void openImageDialog()
    {
        DialogView dialogLayout = (DialogView) getLayoutInflater().inflate(R.layout.dialog_view, null);

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
                B.userPageAvatar.setImageBitmap(null);
            dialog.dismiss();
        }));

        dialog.show();
    }
}