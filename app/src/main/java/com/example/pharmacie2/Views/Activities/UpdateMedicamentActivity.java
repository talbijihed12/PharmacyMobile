package com.example.pharmacie2.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pharmacie2.Data.Database.PharmacyDB;
import com.example.pharmacie2.Data.Entities.Medecin;
import com.example.pharmacie2.Data.Entities.Medicament;
import com.example.pharmacie2.R;

public class UpdateMedicamentActivity extends AppCompatActivity {
    EditText nom,description,fabrication, prix, quantite;
    ImageButton updateBtn;
    Medicament medicament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicament);
        nom = findViewById(R.id.NameM);
        description = findViewById(R.id.description);
        fabrication = findViewById(R.id.fabrication);
        prix = findViewById(R.id.prix);
        quantite = findViewById(R.id.QT);
        updateBtn = findViewById(R.id.updateMedicament);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("medicamentId")) {
            Log.e("medicamentId", String.valueOf(intent.getIntExtra("medicamentId", -1)));
            int medicamentId = intent.getIntExtra("medicamentId", -1);
            String medicamentNom = intent.getStringExtra("medicamentNom");
            String medicamentDescription = intent.getStringExtra("medicamentDescription");
            String medicamentFabrication = intent.getStringExtra("medicamentFabrication");
            double medicamentPrix = intent.getDoubleExtra("medicamentPrix",1);
            int medicamentQt = intent.getIntExtra("medicamentQt",1);
            medicament = new Medicament(medicamentNom,medicamentDescription,medicamentFabrication,medicamentPrix,medicamentQt);
            updateBtn.setOnClickListener(
                    e-> {

                        if (validateInputs()) {
                            // Proceed with updating the medicament
                            medicament.setId(medicamentId);
                            medicament.setNom(nom.getText().toString().trim());
                            medicament.setDescription(description.getText().toString().trim());
                            medicament.setFabricant(fabrication.getText().toString().trim());
                            medicament.setPrix(Double.parseDouble(prix.getText().toString().trim()));
                            medicament.setQuantiteEnStock(Integer.parseInt(quantite.getText().toString().trim()));

                            // Execute the AsyncTask to update the medicament
                            new UpdateMedicamentTask().execute(medicament);

                            // Optionally, you can also navigate to a different activity after the update
                            Intent newIntent = new Intent(this, PharmacyActivity.class);
                            startActivity(newIntent);
                        } else {
                            // Show a message indicating validation failure
                            Toast.makeText(this, "Please fill in all fields with valid data", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Set the user information to the EditText fields
            if (medicament != null) {
                nom.setText(medicament.getNom());
                description.setText(medicament.getDescription());
                fabrication.setText(medicament.getFabricant());
                prix.setText(String.valueOf(medicament.getPrix()) );
                quantite.setText(String.valueOf(medicament.getQuantiteEnStock()) );
            }
        }

    }
    private boolean validateInputs() {
        return validateEditText(nom, "Name") &&
                validateEditText(description, "Description") &&
                validateEditText(fabrication, "Fabrication") &&
                validateDoubleInput(prix, "Prix") &&
                validateIntegerInput(quantite, "Quantite en Stock");
    }

    private boolean validateEditText(EditText editText, String fieldName) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            editText.setError(fieldName + " cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validateDoubleInput(EditText editText, String fieldName) {
        try {
            Double.parseDouble(editText.getText().toString().trim());
            return true;
        } catch (NumberFormatException e) {
            editText.setError(fieldName + " must be a valid number");
            return false;
        }
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

    private class UpdateMedicamentTask extends AsyncTask<Medicament, Void, Void> {

        @Override
        protected Void doInBackground(Medicament... medicaments) {
            PharmacyDB.getInstance(getApplicationContext()).medicamentDao().updateMedicament(medicaments[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(UpdateMedicamentActivity.this, "Medicament updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
