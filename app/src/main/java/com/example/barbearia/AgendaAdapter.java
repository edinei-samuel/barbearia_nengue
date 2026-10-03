package com.example.barbearia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

    private List<AgendamentoModel> listaAgendamentos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AgendamentoModel item);
    }

    public AgendaAdapter(List<AgendamentoModel> listaAgendamentos, OnItemClickListener listener) {
        this.listaAgendamentos = listaAgendamentos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agenda_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AgendamentoModel item = listaAgendamentos.get(position);

        holder.tvNome.setText(item.getNomeCliente());
        holder.tvServico.setText(item.getServico());
        holder.tvHora.setText(item.getHorario());

        if (item.getFotoResId() != 0) {
            holder.imgCliente.setImageResource(item.getFotoResId());
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAgendamentos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvServico, tvHora;
        ImageView imgCliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeCliente);
            tvServico = itemView.findViewById(R.id.tvServicoCliente);
            tvHora = itemView.findViewById(R.id.tvHorarioCliente);
            imgCliente = itemView.findViewById(R.id.imgCliente);
        }
    }
}