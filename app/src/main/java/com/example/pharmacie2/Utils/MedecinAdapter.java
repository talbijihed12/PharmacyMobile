package com.example.pharmacie2.Utils;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacie2.Data.Entities.Medecin;
import com.example.pharmacie2.Data.Entities.User;
import com.example.pharmacie2.R;
import com.example.pharmacie2.Views.Activities.UpdateMedecinActivity;
import com.example.pharmacie2.Views.Activities.UpdateUserActivity;

import java.util.List;

public class MedecinAdapter extends RecyclerView.Adapter<MedecinAdapter.MedecinHolderView>{
    private List<Medecin> medecinList;
    private MedecinAdapter.OnDeleteClickListener onDeleteClickListener;
    public interface OnDeleteClickListener {
        void onDeleteClick(Medecin medecin);
    }
    public MedecinAdapter(List<Medecin> medecinList, MedecinAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.medecinList = medecinList;
        this.onDeleteClickListener = onDeleteClickListener;


    }

    @NonNull
    @Override
    public MedecinAdapter.MedecinHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_item, parent, false);
        return new MedecinAdapter.MedecinHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedecinAdapter.MedecinHolderView holder, int position) {
        Medecin medecin = medecinList.get(position);
        holder.textViewNomMedecin.setText(Html.fromHtml("<b>" + medecin.getNom() + "</b>"));

        // Set the user as a tag for the update button
        holder.buttonUpdate.setTag(medecin);

        // Set onClickListener for the update button
        holder.buttonUpdate.setOnClickListener(view -> {
            Medecin medecinToUpdate = (Medecin) view.getTag();

            // Start the UpdateUserActivity with the user information
            Intent intent = new Intent(view.getContext(), UpdateMedecinActivity.class);
            intent.putExtra("medecinId", medecinToUpdate.getId());
            intent.putExtra("medecinNom", medecinToUpdate.getNom());
            intent.putExtra("medecinPrenom", medecinToUpdate.getPrenom());
            intent.putExtra("medecinSpecialite", medecinToUpdate.getSpecialite());
            intent.putExtra("medecinEmail", medecinToUpdate.getEmail());
            intent.putExtra("medecinNumero", medecinToUpdate.getNumero());
            intent.putExtra("medecinLocalisation", medecinToUpdate.getLocalisation());

            view.getContext().startActivity(intent);
        });

        // Set the user as a tag for the delete button
        holder.buttonDelete.setTag(medecin);

        // Set onClickListener for the delete button
        holder.buttonDelete.setOnClickListener(view -> {
            Medecin updateToDelete = (Medecin) view.getTag();
            onDeleteClickListener.onDeleteClick(updateToDelete);
        });
    }



    @Override
    public int getItemCount() {
        return medecinList.size();
    }

    public class MedecinHolderView extends RecyclerView.ViewHolder {
        TextView textViewNomMedecin;
        TextView textViewNumeroMedecin;
        ImageButton buttonDelete;
        ImageButton buttonUpdate;

        public MedecinHolderView(@NonNull View itemView) {
            super(itemView);
            textViewNomMedecin = itemView.findViewById(R.id.textViewNom);
            textViewNumeroMedecin = itemView.findViewById(R.id.textViewNum);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.update);
        }
    }

    // Add a method to update the list in the adapter
    public void updateList(List<Medecin> newList) {
        medecinList = newList;
        notifyDataSetChanged();
    }
}
