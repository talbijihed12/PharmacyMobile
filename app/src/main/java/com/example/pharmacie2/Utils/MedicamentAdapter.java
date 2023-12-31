package com.example.pharmacie2.Utils;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacie2.Data.Entities.Medecin;
import com.example.pharmacie2.Data.Entities.Medicament;
import com.example.pharmacie2.Data.Entities.User;
import com.example.pharmacie2.R;
import com.example.pharmacie2.Views.Activities.UpdateMedecinActivity;
import com.example.pharmacie2.Views.Activities.UpdateMedicamentActivity;
import com.example.pharmacie2.Views.Activities.UpdateUserActivity;

import java.util.List;

public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder> {
    private List<Medicament> medicamentList;
    private OnDeleteClickListener onDeleteClickListener;
    public interface OnDeleteClickListener {
        void onDeleteClick(Medicament medicament);
    }
    public MedicamentAdapter(List<Medicament> medicamentList, OnDeleteClickListener onDeleteClickListener) {
        this.medicamentList = medicamentList;
        this.onDeleteClickListener = onDeleteClickListener;


    }

    @NonNull
    @Override
    public MedicamentAdapter.MedicamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicament_item, parent, false);
        return new MedicamentAdapter.MedicamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentAdapter.MedicamentViewHolder holder, int position) {
        Medicament medicament  = medicamentList.get(position);
        holder.textViewName.setText(Html.fromHtml("<b>" + medicament.getNom() + "</b>"));

        // Set the user as a tag for the update button
        holder.buttonUpdate.setTag(medicament);

        // Set onClickListener for the update button
        holder.buttonUpdate.setOnClickListener(view -> {
            Medicament medicamentToUpdate = (Medicament) view.getTag();

            // Start the UpdateUserActivity with the user information
            Intent intent = new Intent(view.getContext(), UpdateMedicamentActivity.class);
            intent.putExtra("medicamentId", medicamentToUpdate.getId());
            intent.putExtra("medicamentNom", medicamentToUpdate.getNom());
            intent.putExtra("medicamentDescription", medicamentToUpdate.getDescription());
            intent.putExtra("medicamentFabrication", medicamentToUpdate.getFabricant());
            intent.putExtra("medicamentPrix", medicamentToUpdate.getPrix());
            intent.putExtra("medicamentQt", medicamentToUpdate.getQuantiteEnStock());


            view.getContext().startActivity(intent);
        });
        holder.buttonDelete.setTag(medicament);
        holder.buttonDelete.setOnClickListener(view -> {
            // Retrieve the user from the tag
            Medicament medicamentToDelete = (Medicament) view.getTag();
            onDeleteClickListener.onDeleteClick(medicamentToDelete);
        });


    }

    @Override
    public int getItemCount() {
        return medicamentList.size();
    }

    public class MedicamentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewprix;
        ImageButton buttonDelete;
        ImageButton buttonUpdate;

        public MedicamentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewprix = itemView.findViewById(R.id.prix);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.update);
        }
    }

    // Add a method to update the list in the adapter
    public void updateList(List<Medicament> newList) {
        medicamentList = newList;
        notifyDataSetChanged();
    }
}




