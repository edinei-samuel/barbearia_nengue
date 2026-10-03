package com.example.barbearia;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

public class ViewAgendaActivity extends AppCompatActivity {

    // Declaração dos componentes da tela
    private ImageView btnVoltarPainel;
    private View btnEditarDataHora;
    private MaterialButton btnFinalizarServico;

    private TextView tvHora;
    private TextView tvData;
    private TextView tvNomeServico;
    private TextView tvValorServico;
    private TextView tvValorTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_agenda_adm);
        inicializarComponentes();
        configurarListeners();
        carregarDadosAgendamento();
    }

    private void inicializarComponentes() {
        btnVoltarPainel = findViewById(R.id.btnVoltarPainel);
        btnEditarDataHora = findViewById(R.id.btnEditarDataHora);
        btnFinalizarServico = findViewById(R.id.btnFinalizarServico);

        tvHora = findViewById(R.id.tvHora);
        tvData = findViewById(R.id.tvData);
        tvNomeServico = findViewById(R.id.tvNomeServico);
        tvValorServico = findViewById(R.id.tvValorServico);
        tvValorTotal = findViewById(R.id.tvValorTotal);
    }

    private void configurarListeners() {
        // Botão de voltar na topbar
        btnVoltarPainel.setOnClickListener(v -> finish());

        btnEditarDataHora.setOnClickListener(v -> {
            Toast.makeText(this, "Ação para alterar data e hora", Toast.LENGTH_SHORT).show();
            // TODO: Abrir dialog/picker de data e hora
        });

        btnFinalizarServico.setOnClickListener(v -> finalizarServico());
    }

    private void carregarDadosAgendamento() {
        /*
            Configuração pensando em banco de dados ja
         */
        String nomeServico = "Cabelo";
        double valorServico = 60.00;
        double valorTotal = 60.00;
        String horaAgendada = "09:55";
        String dataAgendada = "Terça, 12 de Maio";

        tvHora.setText(horaAgendada);
        tvData.setText(dataAgendada);

        tvNomeServico.setText(nomeServico);
        tvValorServico.setText(formatarMoeda(valorServico));
        tvValorTotal.setText(formatarMoeda(valorTotal));
    }

    private void finalizarServico() {
        // Lógica para salvar a alteração/finalização no Banco de Dados
        Toast.makeText(this, "Serviço finalizado com sucesso!", Toast.LENGTH_LONG).show();

        // Fecha a Activity
        finish();
    }

    /**
     * Converte o valor numérico para exibição formatada (ex: 60.0 -> "60,00")
     */
    private String formatarMoeda(double valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formato.format(valor).replace("R$", "").trim();
    }
}