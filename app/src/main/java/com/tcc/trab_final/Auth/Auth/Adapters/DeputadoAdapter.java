package com.tcc.trab_final.Auth.Auth.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tcc.trab_final.Auth.Auth.Models.Deputado;
import com.tcc.trab_final.R;

import java.util.List;

public class DeputadoAdapter extends RecyclerView.Adapter<DeputadoAdapter.DeputadoViewHolder> {

    private List<Deputado> deputados;
    private Context context;

    public DeputadoAdapter(List<Deputado> deputados, Context context) {
        this.deputados = deputados;
        this.context = context;
    }

    @NonNull
    @Override
    public DeputadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deputado_item, parent, false);
        return new DeputadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeputadoViewHolder holder, int position) {
        Deputado deputado = deputados.get(position);
        holder.textViewNomeDeputado.setText(deputado.getNome());
    }

    @Override
    public int getItemCount() {
        return deputados.size();
    }

    public static class DeputadoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNomeDeputado;

        public DeputadoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeDeputado = itemView.findViewById(R.id.textViewNomeDeputado);
        }
    }
}
