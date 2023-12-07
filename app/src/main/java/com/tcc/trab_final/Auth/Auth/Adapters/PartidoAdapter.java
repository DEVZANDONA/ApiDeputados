package com.tcc.trab_final.Auth.Auth.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tcc.trab_final.Auth.Auth.Models.Partido;
import com.tcc.trab_final.R;

import java.util.List;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder> {

    private List<Partido> partidoList;

    public PartidoAdapter(List<Partido> partidoList) {
        this.partidoList = partidoList;
    }

    @NonNull
    @Override
    public PartidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partido, parent, false);
        return new PartidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoViewHolder holder, int position) {
        Partido partido = partidoList.get(position);

        holder.textNome.setText(partido.getNome());
        holder.textSigla.setText(partido.getSigla());
    }

    @Override
    public int getItemCount() {
        return partidoList.size();
    }

    public class PartidoViewHolder extends RecyclerView.ViewHolder {
        TextView textNome;
        TextView textSigla;

        public PartidoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.text_nome_partido);
            textSigla = itemView.findViewById(R.id.text_sigla_partido);
        }
    }
}
