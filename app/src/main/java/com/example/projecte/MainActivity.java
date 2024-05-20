package com.example.projecte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.security.SecureRandom;


public class MainActivity extends AppCompatActivity {

    private boolean isStrongPassword = false;
    private EditText editTextLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private EditText editTextGeneratedPassword;
    private Button buttonGeneratePassword;
    private Button buttonUsePassword;
    private TextView textViewLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewPasswordStrength = findViewById(R.id.textViewPasswordStrength);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextGeneratedPassword = findViewById(R.id.editTextGeneratedPassword);
        buttonGeneratePassword = findViewById(R.id.buttonGeneratePassword);
        buttonUsePassword = findViewById(R.id.buttonUsePassword);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);

        textViewPasswordStrength.setVisibility(View.INVISIBLE);
        buttonRegister.setEnabled(false);

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();

                boolean hasLength = password.length() >= 8;
                boolean hasDigit = false;
                boolean hasSpecialChar = false;

                for (char c : password.toCharArray()) {
                    if (Character.isDigit(c)) {
                        hasDigit = true;
                    } else if (!Character.isLetterOrDigit(c)) {
                        hasSpecialChar = true;
                    }
                }

                isStrongPassword = hasLength && hasDigit && hasSpecialChar;

                String strength;
                if (isStrongPassword) {
                    strength = "Strong";
                    textViewPasswordStrength.setTextColor(Color.GREEN);
                } else if (hasLength) {
                    strength = "Medium";
                    textViewPasswordStrength.setTextColor(Color.rgb(255, 165, 0));
                } else {
                    strength = "Weak";
                    textViewPasswordStrength.setTextColor(Color.RED);
                }

                textViewPasswordStrength.setText("Password Strength: " + strength);
                textViewPasswordStrength.setVisibility(password.isEmpty() ? View.INVISIBLE : View.VISIBLE);

                buttonRegister.setEnabled(isStrongPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = editTextLogin.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (login.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(MainActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                User existingUser = UserDatabase.getInstance(MainActivity.this).useUserDao().getUserByLogin(login);
                if (existingUser != null) {
                    Toast.makeText(MainActivity.this, "User with this login already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                new InsertUserTask(MainActivity.this).execute(new User(login, password, email));

            }
        });




        buttonGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String generatedPassword = generatePassword();
                editTextGeneratedPassword.setText(generatedPassword);
                editTextGeneratedPassword.setVisibility(View.VISIBLE);
            }
        });


        buttonUsePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Generated Password", editTextGeneratedPassword.getText().toString());
                editTextPassword.setText(editTextGeneratedPassword.getText().toString());
                Toast.makeText(MainActivity.this, "Password copied to clipboard and used", Toast.LENGTH_SHORT).show();
            }
        });

        textViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private String generatePassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String special = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        String combinedChars = upper + lower + digits + special;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int randomIndex = secureRandom.nextInt(combinedChars.length());
            password.append(combinedChars.charAt(randomIndex));
        }

        return password.toString();
    }


    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private static class InsertUserTask extends AsyncTask<User, Void, Void> {
        private MainActivity mActivity;

        public InsertUserTask(MainActivity activity) {
            mActivity = activity;
        }

        @Override
        protected Void doInBackground(User... users) {
            UserDatabase.getInstance(mActivity).useUserDao().insertToDatabase(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mActivity, "Registration successful", Toast.LENGTH_SHORT).show();
        }
    }
}



