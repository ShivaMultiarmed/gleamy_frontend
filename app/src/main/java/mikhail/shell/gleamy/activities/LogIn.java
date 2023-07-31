package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//import java.net.http.HttpClient;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.models.User;
public class LogIn extends AppCompatActivity {


    //private HttpClient httpClient;
    private enum Status {
        OK, NOTFOUND, PASSINCORRECT, EMPTY
    }

    private EditText login, password;
    private Button btn;
    private TextView msgView;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        users = new ArrayList<>();
        users.add(new User("James","witch"));
        users.add(new User("Ashley","dog"));
        users.add(new User("Mark","car"));
        for (User u:users)
            System.out.println(u.login);
        initHttpBuilder();
        init();
        initBtn();
    }
    private void initHttpBuilder()
    {
        //httpClient  = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
    }
    private void init()
    {
        login = findViewById(R.id.logInName);
        password = findViewById(R.id.logInPassword);
        msgView = findViewById(R.id.logInMessage);




    }
    private void initBtn()
    {
        btn = findViewById(R.id.logInBtn);
        btn.setOnClickListener(e->{
            new LogInAsync().execute();
        });
    }
    private String validate(String login, String password)
    {
        if (login.equals("") || password.equals(""))
            return "EMPTY";
        else
            return serverValidate( login,  password);
    }
    private String serverValidate(String login, String password)
    {

        if (getUser(login) == null)
            return "NOTFOUND";
        else if (!getUser(login).password.equals(password))
            return "PASSINCORRECT";
        else {
            return "OK";
        }
    }
    private User getUser(String login)
    {
        User user = null;
        for (User u:users)
            if (Objects.equals(u.login, login))
                user = u;
        return user;
    }
    private void displayMessage(String status)
    {
        String msg = null;
        switch (status)
        {
            case "OK": msg = "Вы успешно вошли";
                break;
            case "PASSINCORRECT": msg = "Неверный пароль";
                break;
            case "NOTFOUND": msg = "Пользователь не найден";
                break;
            case "EMPTY": msg = "Заполните поля";
                break;
        }
        msgView.setText(msg);
    }
    private class LogInAsync extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            String url = "", status = validate(login.getText().toString(), password.getText().toString());
            /*httpClient = HttpClients.createDefault(); HttpClient
                    .newBuilder().
                    version(HttpClient.Version.HTTP_2).
                    connectTimeout(10000).build();
            //HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://google.com")).build();
            HttpGet getRequest = new HttpGet("https://google.com");
            HttpResponse response = null;
            HttpEntity entity = response.getEntity();
            try {
                if (entity != null) {
                    // Read the content as a string
                    String responseBody = EntityUtils.toString(entity);
                    return responseBody;
                }

                response = httpClient.execute(getRequest);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }*/
            return status;
        }
        @Override
        protected void onPostExecute(String result)
        {
            displayMessage(result);
        }
    }
}