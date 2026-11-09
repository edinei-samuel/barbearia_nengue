package com.example.barbearia;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NovoAgendamentoActivity extends AppCompatActivity {

    private String pacoteSelecionado = "Completo";
    private String responsavelSelecionado = null; // Começa null para nenhum vir selecionado
    private String horarioSelecionado = null;     // Armazena o horário escolhido pelo cliente

    // Componentes dos Pacotes
    private MaterialCardView cardCompleto, cardCabelo, cardBarba;
    private RadioButton radioCompleto, radioCabelo, radioBarba;

    // Grelha onde os profissionais entram dinamicamente
    private GridLayout gridProfissionais;
    private final List<CardProfissionalHolder> listaCardsProfissionais = new ArrayList<>();

    // RecyclerView para os Horários Disponíveis
    private RecyclerView recyclerViewHorarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_agendamento);

        // 1. Configurar Botão Voltar
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        // 2. Inicializar Componentes dos Pacotes
        cardCompleto = findViewById(R.id.cardCompleto);
        cardCabelo = findViewById(R.id.cardCabelo);
        cardBarba = findViewById(R.id.cardBarba);

        radioCompleto = findViewById(R.id.radioCompleto);
        radioCabelo = findViewById(R.id.radioCabelo);
        radioBarba = findViewById(R.id.radioBarba);

        gridProfissionais = findViewById(R.id.gridProfissionais);

        // Inicialize o RecyclerView de horários no seu XML de activity_novo_agendamento se for usar ele direto na tela
        // recyclerViewHorarios = findViewById(R.id.recyclerViewHorarios);

        if (cardCompleto != null) cardCompleto.setOnClickListener(v -> selecionarPacote("Completo"));
        if (cardCabelo != null) cardCabelo.setOnClickListener(v -> selecionarPacote("Cabelo"));
        if (cardBarba != null) cardBarba.setOnClickListener(v -> selecionarPacote("Barba"));

        // Carrega os profissionais na tela
        carregarProfissionaisDoBanco();

        // Carrega os horários disponíveis (com as regras de 30 min, 09:00 até 20:30)
        carregarHorariosDisponiveisDoBanco("2026-11-12", 8);

        Button btnProximo = findViewById(R.id.btnProximo);
        if (btnProximo != null) {
            btnProximo.setOnClickListener(v -> {
                if (responsavelSelecionado == null) {
                    Toast.makeText(this, "Por favor, escolha um profissional antes de continuar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (horarioSelecionado == null) {
                    Toast.makeText(this, "Por favor, escolha um horário antes de continuar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Pacote: " + pacoteSelecionado + " | Profissional: " + responsavelSelecionado + " | Horário: " + horarioSelecionado, Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void selecionarPacote(String pacote) {
        pacoteSelecionado = pacote;

        if (cardCompleto != null) { cardCompleto.setStrokeWidth(1); cardCompleto.setStrokeColor(Color.parseColor("#E0E0E0")); }
        if (cardCabelo != null) { cardCabelo.setStrokeWidth(1); cardCabelo.setStrokeColor(Color.parseColor("#E0E0E0")); }
        if (cardBarba != null) { cardBarba.setStrokeWidth(1); cardBarba.setStrokeColor(Color.parseColor("#E0E0E0")); }

        if (radioCompleto != null) radioCompleto.setChecked(false);
        if (radioCabelo != null) radioCabelo.setChecked(false);
        if (radioBarba != null) radioBarba.setChecked(false);

        if (pacote.equals("Completo")) {
            if (cardCompleto != null) { cardCompleto.setStrokeWidth(2); cardCompleto.setStrokeColor(Color.parseColor("#4285F4")); }
            if (radioCompleto != null) radioCompleto.setChecked(true);
        } else if (pacote.equals("Cabelo")) {
            if (cardCabelo != null) { cardCabelo.setStrokeWidth(2); cardCabelo.setStrokeColor(Color.parseColor("#4285F4")); }
            if (radioCabelo != null) radioCabelo.setChecked(true);
        } else if (pacote.equals("Barba")) {
            if (cardBarba != null) { cardBarba.setStrokeWidth(2); cardBarba.setStrokeColor(Color.parseColor("#4285F4")); }
            if (radioBarba != null) radioBarba.setChecked(true);
        }
    }

    private void carregarProfissionaisDoBanco() {
        if (gridProfissionais == null) return;
        gridProfissionais.removeAllViews();
        listaCardsProfissionais.clear();

        // Simulação de dados (sem banco por enquanto)
        List<ProfissionalModel> listaDoBanco = new ArrayList<>();
        listaDoBanco.add(new ProfissionalModel(7, "Vascaíno Da Navalha", "Cabeleireiro", R.drawable.vascaino_da_navalha));
        listaDoBanco.add(new ProfissionalModel(8, "Michel", "Barbeiro", R.drawable.vascaino_da_navalha));

        LayoutInflater inflater = LayoutInflater.from(this);

        for (ProfissionalModel prof : listaDoBanco) {
            View cardView = inflater.inflate(R.layout.item_profissional, gridProfissionais, false);

            MaterialCardView cardCard = cardView.findViewById(R.id.cardProfissional);
            ImageView imgFoto = cardView.findViewById(R.id.imgProfissional);
            TextView tvNome = cardView.findViewById(R.id.tvNomeProfissional);
            TextView tvCargo = cardView.findViewById(R.id.tvCargoProfissional);

            tvNome.setText(prof.getNome());
            tvCargo.setText(prof.getCargo());
            imgFoto.setImageResource(prof.getFotoResId());

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(6, 6, 6, 6);
            cardView.setLayoutParams(params);

            CardProfissionalHolder holder = new CardProfissionalHolder(cardCard, prof.getNome());
            listaCardsProfissionais.add(holder);

            holder.destacar(false);

            cardCard.setOnClickListener(v -> {
                // VERIFICAÇÃO DO BARBEIRO DE ID 7 (Dispara o pop-up e bloqueia a seleção)
                if (prof.getId() == 7) {
                    mostrarDialogoIndisponivel();
                    return;
                }

                responsavelSelecionado = prof.getNome();

                for (CardProfissionalHolder h : listaCardsProfissionais) {
                    h.destacar(h.nomeBarbeiro.equals(responsavelSelecionado));
                }
            });

            gridProfissionais.addView(cardView);
        }
    }

    /**
     * GERAÇÃO DOS HORÁRIOS DISPONÍVEIS:
     * - Expediente: 09:00 até 20:30 (de 30 em 30 minutos).
     * - A parte de verificação com banco de dados está totalmente comentada para implementar depois.
     */
    private void carregarHorariosDisponiveisDoBanco(String dataSelecionada, int idProfissional) {
        List<Horariodisp> listaHorariosDisponiveis = new ArrayList<>();

        // =========================================================================
        // BANCO DE DADOS (COMENTADO - Sem conexão real no momento)
        // =========================================================================
        /*
        // Futuramente, você fará a busca aqui:
        // List<String> horariosOcupadosNoBanco = meuBanco.buscarOcupados(dataSelecionada, idProfissional);
        */

        // Lista simulada de horários ocupados (apenas para teste local)
        List<String> horariosOcupadosNoBanco = new ArrayList<>();
        horariosOcupadosNoBanco.add("09:30");
        horariosOcupadosNoBanco.add("10:00");

        // Geração automática dos horários (09:00 às 20:30 de 30 em 30 min)
        int horaInicio = 9;
        int minutoInicio = 0;
        int horaFim = 20;
        int minutoFim = 30;

        while (horaInicio < horaFim || (horaInicio == horaFim && minutoInicio <= minutoFim)) {
            String horarioFormatado = String.format(Locale.getDefault(), "%02d:%02d", horaInicio, minutoInicio);

            // Se o horário NÃO estiver na lista de ocupados, ele aparece como disponível
            if (!horariosOcupadosNoBanco.contains(horarioFormatado)) {
                listaHorariosDisponiveis.add(new Horariodisp(horarioFormatado));
            }

            minutoInicio += 30;
            if (minutoInicio >= 60) {
                minutoInicio = 0;
                horaInicio++;
            }
        }

        // Configuração do Adapter para preencher o RecyclerView de horários
        /*
        HorariosDisponiveisAdapter adapter = new HorariosDisponiveisAdapter(listaHorariosDisponiveis, horarioEscolhido -> {
            horarioSelecionado = horarioEscolhido;
        });

        if (recyclerViewHorarios != null) {
            recyclerViewHorarios.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewHorarios.setAdapter(adapter);
        }
        */
    }

    // Método para exibir o pop-up de Barbeiro Indisponível
    private void mostrarDialogoIndisponivel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_barbeiro_indisponivel, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        Button btnOk = view.findViewById(R.id.btnOkDialog);
        if (btnOk != null) {
            btnOk.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    // Modelo do Profissional contendo o ID
    private static class ProfissionalModel {
        private final int id;
        private final String nome;
        private final String cargo;
        private final int fotoResId;

        public ProfissionalModel(int id, String nome, String cargo, int fotoResId) {
            this.id = id;
            this.nome = nome;
            this.cargo = cargo;
            this.fotoResId = fotoResId;
        }

        public int getId() { return id; }
        public String getNome() { return nome; }
        public String getCargo() { return cargo; }
        public int getFotoResId() { return fotoResId; }
    }

    private static class CardProfissionalHolder {
        MaterialCardView card;
        String nomeBarbeiro;

        public CardProfissionalHolder(MaterialCardView card, String nomeBarbeiro) {
            this.card = card;
            this.nomeBarbeiro = nomeBarbeiro;
        }

        public void destacar(boolean selecionado) {
            if (selecionado) {
                card.setStrokeWidth(2);
                card.setStrokeColor(Color.parseColor("#4285F4")); // Azul ao selecionar
            } else {
                card.setStrokeWidth(1);
                card.setStrokeColor(Color.parseColor("#E2E8F0")); // Normal
            }
        }
    }
}