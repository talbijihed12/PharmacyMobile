package com.example.pharmacie2.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pharmacie2.Data.Database.PharmacyDB;
import com.example.pharmacie2.Data.Entities.User;
import com.example.pharmacie2.R;

public class UpdateUserActivity extends AppCompatActivity {
    EditText name, email, password;
    Button updateBtn;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        updateBtn = findViewById(R.id.update);

        // Retrieve the user ID from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            int userId = intent.getIntExtra("userId", -1);
            String userName = intent.getStringExtra("userName");
            String userEmail = intent.getStringExtra("userEmail");
            String userPassword = intent.getStringExtra("userPassword");
            user = new User(userName,userEmail,userPassword);

            // Fetch the user details from your data source based on the user ID

        updateBtn.setOnClickListener(
                e-> {
                    if (validateInputs()) {
                        // Proceed with update
                        if (password.getText().toString().trim().isEmpty() || !password.getText().toString().equals(userPassword)) {
                            Toast.makeText(this, "Password Invalid", Toast.LENGTH_SHORT).show();
                        } else {
                            user = new User();
                            user.setId(userId);
                            // Update the user information with the new values
                            user.setName(name.getText().toString().trim());
                            user.setEmail(email.getText().toString().trim());
                            user.setPassword(password.getText().toString().trim());

                            // Execute the AsyncTask to update the user
                            new UpdateUserTask().execute(user);

                            // Optionally, you can also navigate to a different activity after the update
                            Intent newIntent = new Intent(this, MainActivity.class);
                            startActivity(newIntent);
                        }
                    } else {
                        // Show a message indicating validation failure
                        Toast.makeText(this, "Please fill in all fields with valid data", Toast.LENGTH_SHORT).show();
                    }
                });

            // Set the user information to the EditText fields
            if (user != null) {
                name.setText(user.getName());
                email.setText(user.getEmail());
                password.setText("");
            }
        }
    }
    private boolean validateInputs() {
        return validateEditText(name, "Name") &&
                validateEmail(email) ;
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



    private class UpdateUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            PharmacyDB.getInstance(getApplicationContext()).userDao().updateUser(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(UpdateUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}