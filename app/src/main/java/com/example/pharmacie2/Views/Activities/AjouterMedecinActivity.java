package com.example.pharmacie2.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pharmacie2.Data.Dao.MedecinDao;
import com.example.pharmacie2.Data.Database.PharmacyDB;
import com.example.pharmacie2.Data.Entities.Medecin;
import com.example.pharmacie2.R;

public class AjouterMedecinActivity extends AppCompatActivity {
    ImageView backBtn;
    ImageButton saveMedButton;
    private EditText editTextNom ;
    private EditText editTextPrenom ;
    private EditText editTextSpecialite;
    private EditText editTextEmail;
    private EditText editTextNumero ;
    private EditText editTextLocalisation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_medecin);
        saveMedButton = findViewById(R.id.saveMed);
        editTextNom = findViewById(R.id.nomMedecin);
        editTextPrenom = findViewById(R.id.prenomMedecin);
        editTextSpecialite = findViewById(R.id.specialiteMedecin);
        editTextEmail = findViewById(R.id.emailMedecin);
        editTextNumero = findViewById(R.id.numeroMedecin);
        editTextLocalisation = findViewById(R.id.localisationMedecin);
        backBtn = findViewById(R.id.imageViewCalendrier);
        backBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, MedecinActivity.class);
            startActivity(intent);
            Toast.makeText(this, "main", Toast.LENGTH_SHORT).show();
            finish();
        });
     saveMedButton.setOnClickListener(v -> {
         if (validateInputs()) {
             // Proceed with saving the medecin
             String nom = editTextNom.getText().toString().trim();
             String prenom = editTextPrenom.getText().toString().trim();
             String specialite = editTextSpecialite.getText().toString().trim();
             String email = editTextEmail.getText().toString().trim();
             int numero = Integer.parseInt(editTextNumero.getText().toString().trim());
             String localisation = editTextLocalisation.getText().toString().trim();

             // Execute the AsyncTask to add the medecin
             new MedTask().execute(nom, prenom, specialite, email, String.valueOf(numero), localisation);

             // Optionally, you can also navigate to a different activity after the addition
             Intent intent = new Intent(this, MedecinActivity.class);
             startActivity(intent);
             finish();
             Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
         } else {
             // Show a message indicating validation failure
             Toast.makeText(this, "Please fill in all fields with valid data", Toast.LENGTH_SHORT).show();
         }
     });


    }
    private boolean validateInputs() {
        return validateEditText(editTextNom, "Nom") &&
                validateEditText(editTextPrenom, "Prenom") &&
                validateEditText(editTextSpecialite, "Specialite") &&
                validateEmail(editTextEmail) &&
                validateIntegerInput(editTextNumero, "Numero") &&
                validateEditText(editTextLocalisation, "Localisation");
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
private class MedTask extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        String nom = params[0];
        String prenom = params[1];
        String specialite = params[2];
        String email = params[3];
        int numero = Integer.parseInt(params[4]);
        String localisation = params[5];


        PharmacyDB medecinDatabase = PharmacyDB.getInstance(getApplicationContext());
        MedecinDao medecinDao = medecinDatabase.medecinDao();


        // Create a new Medicament and insert into the database
        Medecin newMedicament = new Medecin(nom, prenom, specialite, email, numero,localisation);
        medecinDao.insertMedecin(newMedicament);

        return true; // Medicament added successfully
    }
}

}