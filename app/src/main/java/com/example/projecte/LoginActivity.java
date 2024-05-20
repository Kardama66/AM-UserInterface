package com.example.projecte;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegisterLink;
    private UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLogin = findViewById(R.id.editTextLoginUsername);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegisterLink = findViewById(R.id.textViewRegisterLink);

        userDAO = UserDatabase.getInstance(this).useUserDao();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        textViewRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String login = editTextLogin.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter login and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sprawdzanie użytkownika w bazie danych w osobnym wątku
        new CheckUserTask(this).execute(login, password);
    }

    private static class CheckUserTask extends AsyncTask<String, Void, User> {

        private LoginActivity mActivity;
        public CheckUserTask(LoginActivity activity) {
            mActivity = activity;
        }

        @Override
        protected User doInBackground(String... params) {
            String login = params[0];
            String password = params[1];
            return mActivity.userDAO.getUserByLoginAndPassword(login, password);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null) {
                Toast.makeText(mActivity, "Logged in successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mActivity, LoggedIn.class);
                intent.putExtra("user", user);
                mActivity.startActivity(intent);
            } else {
                Toast.makeText(mActivity, "Invalid login or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
