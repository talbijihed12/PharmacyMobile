package com.example.pharmacie2.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
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

public class SignInActivity extends AppCompatActivity {
    Button signInBtn;
    TextView signUp ;

    private EditText editTextEmail;
    private EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInBtn = findViewById(R.id.buttonSignIn);
        signUp = findViewById(R.id.signUp);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        signInBtn.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            new AuthenticateUserTask().execute(email, password);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Hello World",Toast.LENGTH_SHORT).show();



        });

        signUp.setOnClickListener(
                e -> {
                    Intent intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Hello World",Toast.LENGTH_SHORT).show();
                }
        );

    }

    private class AuthenticateUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            return PharmacyDB.getInstance(getApplicationContext()).userDao().getUserByEmailAndPassword(email, password);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }

    }

}