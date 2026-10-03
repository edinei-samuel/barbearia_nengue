package com.example.barbearia;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class ArrastaPraCima extends BottomSheetDialogFragment {

    private TextView tvDataArrasta;
    private TabLayout tabLayoutTurnos;
    private RecyclerView rvHorarios;

    private List<AgendamentoModel> listaTodosAgendamentos = new ArrayList<>();
    private List<AgendamentoModel> listaFiltrada = new ArrayList<>();
    private AgendaAdapter adapter;
    private String dataStr = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.arrasta_pra_cima, container, false);

        tvDataArrasta = view.findViewById(R.id.tvDataArrasta);
        tabLayoutTurnos = view.findViewById(R.id.tabLayoutTurnos);
        rvHorarios = view.findViewById(R.id.rvHorarios);

        rvHorarios.setLayoutManager(new LinearLayoutManager(getContext()));

        // Exibe a data recebida da AgendaBarber
        if (getArguments() != null) {
            dataStr = getArguments().getString("data_selecionada", "");
            if (!dataStr.isEmpty()) {
                tvDataArrasta.setText(dataStr);
            }
        }

        // Carrega dados fictícios ou do banco de dados
        carregarAgendamentosDoDia();

        // Configura o Adapter e o clique para abrir a tela de visualização/administração
        adapter = new AgendaAdapter(listaFiltrada, item -> {
            if (getActivity() != null) {
                // Se a sua Activity de detalhes se chamar ViewAgendaAdmActivity, substitua o tipo abaixo:
                Intent intent = new Intent(getActivity(), ViewAgendaActivity.class);

                // Passa os dados do card para a nova Activity
                intent.putExtra("nome_cliente", item.getNomeCliente());
                intent.putExtra("servico_cliente", item.getServico());
                intent.putExtra("horario_cliente", item.getHorario());
                intent.putExtra("data_cliente", dataStr);

                startActivity(intent);

                // Fecha o BottomSheet ao abrir a nova tela
                dismiss();
            }
        });

        rvHorarios.setAdapter(adapter);

        // Inicializa na aba Manhã (0)
        filtrarPorTurno(0);

        // Escuta a troca de abas (Manhã = 0, Tarde = 1, Noite = 2)
        tabLayoutTurnos.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filtrarPorTurno(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void carregarAgendamentosDoDia() {
        listaTodosAgendamentos.clear();

        // Exemplos de horários em diferentes turnos
        listaTodosAgendamentos.add(new AgendamentoModel("Michel", "Cabelo + Barba", "10:55", R.drawable.vascaino_da_navalha)); // Manhã
        listaTodosAgendamentos.add(new AgendamentoModel("Gabriel", "Corte Degradê", "11:30", R.drawable.vascaino_da_navalha)); // Manhã
        listaTodosAgendamentos.add(new AgendamentoModel("Lucas", "Barba", "14:00", R.drawable.vascaino_da_navalha));         // Tarde
        listaTodosAgendamentos.add(new AgendamentoModel("Rodrigo", "Cabelo + Barba", "18:30", R.drawable.vascaino_da_navalha)); // Tarde (limite)
        listaTodosAgendamentos.add(new AgendamentoModel("Matheus", "Pigmentação", "19:15", R.drawable.vascaino_da_navalha));    // Noite
    }

    /**
     * Regra dos Turnos:
     * 0 -> Manhã  (< 12:00)
     * 1 -> Tarde  (12:00 até 18:30)
     * 2 -> Noite  (> 18:30)
     */
    private void filtrarPorTurno(int turnoAlvo) {
        listaFiltrada.clear();

        for (AgendamentoModel item : listaTodosAgendamentos) {
            int turnoDoItem = calcularTurno(item.getHorario());
            if (turnoDoItem == turnoAlvo) {
                listaFiltrada.add(item);
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private int calcularTurno(String horario) {
        try {
            String[] partes = horario.split(":");
            int horas = Integer.parseInt(partes[0].trim());
            int minutos = Integer.parseInt(partes[1].trim());

            int totalMinutos = (horas * 60) + minutos;

            // 12:00 = 720 min | 18:30 = 1110 min
            if (totalMinutos < 720) {
                return 0; // Manhã
            } else if (totalMinutos <= 1110) {
                return 1; // Tarde
            } else {
                return 2; // Noite
            }
        } catch (Exception e) {
            return 0;
        }
    }
}