package com.example.barbearia;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    public static class DiaModel {
        public String dia;
        public String dataCompleta; // YYYY-MM-DD
        public boolean ehMesAtual;
        public boolean ehFolga;
        public boolean ehPassado; // Indica se a data é anterior ao dia de hoje

        public DiaModel(String dia, String dataCompleta, boolean ehMesAtual, boolean ehFolga, boolean ehPassado) {
            this.dia = dia;
            this.dataCompleta = dataCompleta;
            this.ehMesAtual = ehMesAtual;
            this.ehFolga = ehFolga;
            this.ehPassado = ehPassado;
        }
    }

    private final ArrayList<DiaModel> listaDias;
    private OnItemClickListener listener;
    private int posicaoSelecionada = -1;

    public interface OnItemClickListener {
        void onItemClick(DiaModel diaItem, int position);
    }

    public CalendarAdapter(ArrayList<DiaModel> listaDias, OnItemClickListener listener) {
        this.listaDias = listaDias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dia_calendario, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        DiaModel item = listaDias.get(position);
        holder.tvDia.setText(item.dia);

        if (!item.ehMesAtual) {
            // Dias fora do mês atual: cinza e sem fundo
            holder.tvDia.setBackground(null);
            holder.tvDia.setTextColor(Color.parseColor("#CCCCCC"));
            holder.tvDia.setAlpha(0.4f);
        } else if (item.ehPassado) {
            // Dias passados do mês atual: opacidade reduzida e cinza
            holder.tvDia.setBackground(null);
            holder.tvDia.setTextColor(Color.parseColor("#888888"));
            holder.tvDia.setAlpha(0.5f);
        } else {
            // Dias presentes ou futuros (válidos)
            holder.tvDia.setAlpha(1.0f);

            if (position == posicaoSelecionada) {
                // Dia selecionado no momento
                holder.tvDia.setBackgroundResource(R.drawable.dia_disp);
                holder.tvDia.setTextColor(Color.parseColor("#FFC107"));
            } else if (item.ehFolga) {
                // Dia de Folga (Vermelho)
                holder.tvDia.setBackgroundResource(R.drawable.dia_folga);
                holder.tvDia.setTextColor(Color.parseColor("#E53935"));
            } else {
                // Dia Disponível (Azul)
                holder.tvDia.setBackgroundResource(R.drawable.dia_disp);
                holder.tvDia.setTextColor(Color.parseColor("#4285F4"));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int posAntiga = posicaoSelecionada;
                posicaoSelecionada = holder.getAdapterPosition();

                if (posAntiga != -1) notifyItemChanged(posAntiga);
                notifyItemChanged(posicaoSelecionada);

                listener.onItemClick(item, posicaoSelecionada);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDias.size();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView tvDia;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDia = itemView.findViewById(R.id.tvDia);
        }
    }
}