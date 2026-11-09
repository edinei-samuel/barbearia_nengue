package com.example.barbearia;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class HorariosDisponiveisAdapter extends RecyclerView.Adapter<HorariosDisponiveisAdapter.ViewHolder> {

    private List<Horariodisp> listaHorarios;
    private int posicaoSelecionada = -1;
    private OnHorarioSelectedListener listener;

    public interface OnHorarioSelectedListener {
        void onHorarioSelected(String horario);
    }

    public HorariosDisponiveisAdapter(List<Horariodisp> listaHorarios, OnHorarioSelectedListener listener) {
        this.listaHorarios = listaHorarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_horario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Horariodisp item = listaHorarios.get(position);

        holder.tvHorario.setText(item.getHorario());
        holder.rbHorario.setChecked(item.isSelecionado());

        // Altera a borda do card se estiver selecionado
        if (item.isSelecionado()) {
            holder.cardView.setStrokeWidth(2);
            holder.cardView.setStrokeColor(Color.parseColor("#4285F4"));
        } else {
            holder.cardView.setStrokeWidth(1);
            holder.cardView.setStrokeColor(Color.parseColor("#EAEAEA"));
        }

        // Clique no card inteiro para selecionar o horário
        holder.itemView.setOnClickListener(v -> {
            int posicaoAnterior = posicaoSelecionada;
            posicaoSelecionada = holder.getAdapterPosition();

            if (posicaoAnterior != RecyclerView.NO_POSITION) {
                listaHorarios.get(posicaoAnterior).setSelecionado(false);
                notifyItemChanged(posicaoAnterior);
            }

            item.setSelecionado(true);
            notifyItemChanged(posicaoSelecionada);

            if (listener != null) {
                listener.onHorarioSelected(item.getHorario());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaHorarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvHorario;
        RadioButton rbHorario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvHorario = itemView.findViewById(R.id.tvHorarioDisponivel);
            rbHorario = itemView.findViewById(R.id.rbHorario);
        }
    }
}