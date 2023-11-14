package com.example.pharmacie2.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacie2.Data.Entities.Medicament;
import com.example.pharmacie2.Data.Entities.User;
import com.example.pharmacie2.R;

import java.util.List;

public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder> {
    private List<Medicament> medicamentList;
    public MedicamentAdapter(List<Medicament> medicamentList) {
        this.medicamentList = medicamentList;


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
        holder.textViewName.setText(medicament.getNom());



    }

    @Override
    public int getItemCount() {
        return medicamentList.size();
    }

    public class MedicamentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        Button buttonDelete;

        public MedicamentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    // Add a method to update the list in the adapter
    public void updateList(List<Medicament> newList) {
        medicamentList = newList;
        notifyDataSetChanged();
    }
}




