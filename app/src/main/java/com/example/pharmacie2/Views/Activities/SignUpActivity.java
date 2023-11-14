package com.example.pharmacie2.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacie2.Data.Dao.UserDao;
import com.example.pharmacie2.Data.Database.PharmacyDB;
import com.example.pharmacie2.Data.Entities.User;
import com.example.pharmacie2.R;

public class SignUpActivity extends AppCompatActivity {
    TextView signInBtn;
    Button buttonSignUp;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(v -> {
            if (validateInputs()) {
                // Proceed with sign-up
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if the email is already registered
                new CheckEmailTask().execute(name, email, password);
            } else {
                // Show a message indicating validation failure
                Toast.makeText(this, "Please fill in all fields with valid data", Toast.LENGTH_SHORT).show();
            }
        });

        signInBtn = findViewById(R.id.textViewSignInLink);
        signInBtn.setOnClickListener(
                e -> {
                    Intent intent = new Intent(this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();
                }
        );
    }
    private boolean validateInputs() {
        return validateEditText(editTextName, "Name") &&
                validateEmail(editTextEmail) &&
                validateEditText(editTextPassword, "Password");
    }

    private boolean validateEditText(EditText editText, String fieldName) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            editText.setError(fieldName + " cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validateEmail(EditText editText) {
        // Use a simple regex for basic email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String text = editText.getText().toString().trim();
        if (!text.matches(emailPattern)) {
            editText.setError("Enter a valid email address");
            return false;
        }
        return true;
    }


    private class CheckEmailTask extends AsyncTask<String, Void, Boolean> {
        private String[] params;
        @Override
        protected Boolean doInBackground(String... params) {
            this.params = params; // Stored the parameters
            String name = params[0];
            String email = params[1];
            String password = params[2];

            PharmacyDB userDatabase = PharmacyDB.getInstance(getApplicationContext());
            UserDao userDao = userDatabase.userDao();

            // Check if the email is already registered
            User existingUser = userDao.getUserByEmail(email);

            return existingUser == null;
        }

        @Override
        protected void onPostExecute(Boolean isEmailAvailable) {
            if (isEmailAvailable) {
                // Email is not registered, proceed with signup
                new SignUpTask().execute(params);
            } else {
                // Email is already registered
                Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SignUpTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String name = params[0];
            String email = params[1];
            String password = params[2];

            PharmacyDB userDatabase = PharmacyDB.getInstance(getApplicationContext());
            UserDao userDao = userDatabase.userDao();

            // Create a new user and insert into the database
            User newUser = new User(name, email, password);
            userDao.insert(newUser);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Signup successful
            Toast.makeText(SignUpActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();

            // Navigate to MainActivity or perform any other post-signup actions
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}