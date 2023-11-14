package com.example.pharmacie2.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pharmacie2.Data.Database.PharmacyDB;
import com.example.pharmacie2.Data.Entities.Medecin;
import com.example.pharmacie2.Data.Entities.User;
import com.example.pharmacie2.R;

public class UpdateMedecinActivity extends AppCompatActivity {

    EditText nom,prenom,specialite, email, numero,localisation;
    Button updateBtn;
    Medecin medecin;

    int defaultValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medecin);

        nom = findViewById(R.id.nomMedecin);
        prenom = findViewById(R.id.prenomMedecin);
        specialite = findViewById(R.id.specialiteMedecin);
        email = findViewById(R.id.emailMedecin);
        numero = findViewById(R.id.numeroMedecin);
        localisation = findViewById(R.id.localisationMedecin);
        updateBtn = findViewById(R.id.updateMedecin);

        // Retrieve the user ID from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("medecinId")) {
            Log.e("medecinId", String.valueOf(intent.getIntExtra("medecinId", -1)));
            int medecinId = intent.getIntExtra("medecinId", -1);
            String medecinNom = intent.getStringExtra("medecinNom");
            String medecinPrenom = intent.getStringExtra("medecinPrenom");
            String medecinSpecialite = intent.getStringExtra("medecinSpecialite");
            String medecinEmail = intent.getStringExtra("medecinEmail");
            int medecinNumero = intent.getIntExtra("medecinNumero",1);
            String medecinLocalisation = intent.getStringExtra("medecinLocalisation");
            medecin = new Medecin(medecinNom,medecinPrenom,medecinSpecialite,medecinEmail,medecinNumero,medecinLocalisation);
            updateBtn.setOnClickListener(
                    e-> {

                        if (validateInputs()) {
                            // Proceed with updating the medecin
                            medecin.setId(medecinId);
                            medecin.setNom(nom.getText().toString().trim());
                            medecin.setPrenom(prenom.getText().toString().trim());
                            medecin.setSpecialite(specialite.getText().toString().trim());
                            medecin.setEmail(email.getText().toString().trim());
                            medecin.setNumero(Integer.parseInt(numero.getText().toString().trim()));
                            medecin.setLocalisation(localisation.getText().toString().trim());

                            // Execute the AsyncTask to update the medecin
                            new UpdateMedecinTask().execute(medecin);

                            // Optionally, you can also navigate to a different activity after the update
                            Intent newIntent = new Intent(this, MedecinActivity.class);
                            startActivity(newIntent);
                        } else {
                            // Show a message indicating validation failure
                            Toast.makeText(this, "Please fill in all fields with valid data", Toast.LENGTH_SHORT).show();
                        }
                    });


            // Set the user information to the EditText fields
            if (medecin != null) {
               nom.setText(medecin.getNom());
                prenom.setText(medecin.getPrenom());
                specialite.setText(medecin.getSpecialite());
                email.setText(medecin.getEmail());
                numero.setText(String.valueOf(medecin.getNumero()) );
                localisation.setText(medecin.getLocalisation());

            }
        }
}
    private boolean validateInputs() {
        return validateEditText(nom, "Nom") &&
                validateEditText(prenom, "Prenom") &&
                validateEditText(specialite, "Specialite") &&
                validateEmail(email) &&
                validateIntegerInput(numero, "Numero") &&
                validateEditText(localisation, "Localisation");
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

    private boolean validateIntegerInput(EditText editText, String fieldName) {
        try {
            Integer.parseInt(editText.getText().toString().trim());
            return true;
        } catch (NumberFormatException e) {
            editText.setError(fieldName + " must be a valid integer");
            return false;
        }
    }
    private class UpdateMedecinTask extends AsyncTask<Medecin, Void, Void> {

        @Override
        protected Void doInBackground(Medecin... medecins) {
            PharmacyDB.getInstance(getApplicationContext()).medecinDao().updateMedecin(medecins[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(UpdateMedecinActivity.this, "Medecin updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
