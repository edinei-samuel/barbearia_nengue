package com.example.barbearia;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AgendaBarber extends AppCompatActivity {

    private TextView tvMesAno;
    private RecyclerView rvCalendario;
    private YearMonth dataSelecionada;
    private CalendarAdapter adapter;
    private CalendarAdapter.DiaModel diaSelecionado;
    private MaterialButton btnMarcarFolga, btnNovoAgendamento;

    private Set<String> folgasManuais = new HashSet<>();
    private Set<String> diasTrabalhoManuais = new HashSet<>();
    private Set<String> feriados = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_barber);

        BarraNavegar.setup(this, R.id.navAgendamentos);

        ImageView btnVoltar = findViewById(R.id.btnVoltarAgendamentos);
        tvMesAno = findViewById(R.id.tvMesAno);
        rvCalendario = findViewById(R.id.rvCalendario);
        MaterialButton btnMesAnterior = findViewById(R.id.btnMesAnterior);
        MaterialButton btnProximoMes = findViewById(R.id.btnProximoMes);
        btnMarcarFolga = findViewById(R.id.btnMarcarFolga);
        btnNovoAgendamento = findViewById(R.id.btnNovoAgendamento);

        btnVoltar.setOnClickListener(v -> finish());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dataSelecionada = YearMonth.now();
            carregarFeriados(dataSelecionada.getYear());
            configurarCalendario();

            btnMesAnterior.setOnClickListener(v -> {
                dataSelecionada = dataSelecionada.minusMonths(1);
                diaSelecionado = null;
                carregarFeriados(dataSelecionada.getYear());
                atualizarBotoes();
                configurarCalendario();
            });

            btnProximoMes.setOnClickListener(v -> {
                dataSelecionada = dataSelecionada.plusMonths(1);
                diaSelecionado = null;
                carregarFeriados(dataSelecionada.getYear());
                atualizarBotoes();
                configurarCalendario();
            });

            btnNovoAgendamento.setOnClickListener(v -> {
                if (diaSelecionado == null) {
                    Toast.makeText(this, "Selecione um dia no calendário!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (diaSelecionado.ehPassado || !diaSelecionado.ehMesAtual) {
                    Toast.makeText(this, "Não é possível agendar em datas passadas!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (diaSelecionado.ehFolga) {
                    Toast.makeText(this, "Este dia é FOLGA/FERIADO. Desmarque a folga primeiro para agendar!", Toast.LENGTH_LONG).show();
                    return;
                }

                abrirPainelArrasta(diaSelecionado.dataCompleta);
            });

            btnMarcarFolga.setOnClickListener(v -> {
                if (diaSelecionado == null) {
                    Toast.makeText(this, "Selecione um dia no calendário primeiro!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (diaSelecionado.ehPassado || !diaSelecionado.ehMesAtual) {
                    Toast.makeText(this, "Não é possível alterar datas passadas!", Toast.LENGTH_SHORT).show();
                    return;
                }

                diaSelecionado.ehFolga = !diaSelecionado.ehFolga;

                if (diaSelecionado.ehFolga) {
                    folgasManuais.add(diaSelecionado.dataCompleta);
                    diasTrabalhoManuais.remove(diaSelecionado.dataCompleta);
                    Toast.makeText(this, "Dia " + diaSelecionado.dia + " marcado como FOLGA!", Toast.LENGTH_SHORT).show();
                } else {
                    diasTrabalhoManuais.add(diaSelecionado.dataCompleta);
                    folgasManuais.remove(diaSelecionado.dataCompleta);
                    Toast.makeText(this, "Folga desmarcada! Dia " + diaSelecionado.dia + " agora está DISPONÍVEL.", Toast.LENGTH_SHORT).show();
                }

                atualizarBotoes();
                adapter.notifyDataSetChanged();
            });
        }
    }

    private void carregarFeriados(int ano) {
        feriados.clear();
        String prefixo = ano + "-";

        feriados.add(prefixo + "04-27");
        feriados.add(prefixo + "01-01");
        feriados.add(prefixo + "04-21");
        feriados.add(prefixo + "05-01");
        feriados.add(prefixo + "09-01");
        feriados.add(prefixo + "09-07");
        feriados.add(prefixo + "10-12");
        feriados.add(prefixo + "11-02");
        feriados.add(prefixo + "11-15");
        feriados.add(prefixo + "11-20");
        feriados.add(prefixo + "12-25");
    }

    private void configurarCalendario() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR"));
            String mesAnoFormatado = dataSelecionada.format(formatter);
            tvMesAno.setText(mesAnoFormatado.substring(0, 1).toUpperCase() + mesAnoFormatado.substring(1));

            ArrayList<CalendarAdapter.DiaModel> dias = gerarDiasDoCalendarioCompleto(dataSelecionada);

            rvCalendario.setLayoutManager(new GridLayoutManager(this, 7));

            adapter = new CalendarAdapter(dias, (diaItem, position) -> {
                if (!diaItem.ehMesAtual || diaItem.ehPassado) {
                    Toast.makeText(this, "Máquina do tempo ainda não foi criada  \uD83D\uDC4D  \uD83D\uDC4D", Toast.LENGTH_SHORT).show();
                    return;
                }

                diaSelecionado = diaItem;
                atualizarBotoes();

                if (!diaItem.ehFolga) {
                    abrirPainelArrasta(diaItem.dataCompleta);
                }
            });

            rvCalendario.setAdapter(adapter);
        }
    }

    private void abrirPainelArrasta(String dataCompleta) {
        ArrastaPraCima arrasta = new ArrastaPraCima();
        Bundle bundle = new Bundle();
        bundle.putString("data_selecionada", dataCompleta);
        arrasta.setArguments(bundle);
        arrasta.show(getSupportFragmentManager(), "ArrastaPraCimaTag");
    }

    private void atualizarBotoes() {
        if (diaSelecionado != null && diaSelecionado.ehMesAtual && !diaSelecionado.ehPassado) {
            if (diaSelecionado.ehFolga) {
                btnMarcarFolga.setText("Desmarcar Folga");
                btnNovoAgendamento.setEnabled(false);
                btnNovoAgendamento.setAlpha(0.5f);
            } else {
                btnMarcarFolga.setText("Marcar Folga");
                btnNovoAgendamento.setEnabled(true);
                btnNovoAgendamento.setAlpha(1.0f);
            }
        } else {
            btnMarcarFolga.setText("Folga");
            btnNovoAgendamento.setEnabled(true);
            btnNovoAgendamento.setAlpha(1.0f);
        }
    }

    private ArrayList<CalendarAdapter.DiaModel> gerarDiasDoCalendarioCompleto(YearMonth date) {
        ArrayList<CalendarAdapter.DiaModel> listaDias = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate hoje = LocalDate.now();
            int diasNoMesAtual = date.lengthOfMonth();
            int primeiroDiaDaSemana = date.atDay(1).getDayOfWeek().getValue();

            YearMonth mesAnterior = date.minusMonths(1);
            int diasNoMesAnterior = mesAnterior.lengthOfMonth();

            int qtdDiasAnteriores = primeiroDiaDaSemana - 1;
            for (int i = qtdDiasAnteriores - 1; i >= 0; i--) {
                int diaNum = diasNoMesAnterior - i;
                LocalDate data = mesAnterior.atDay(diaNum);
                listaDias.add(new CalendarAdapter.DiaModel(String.valueOf(diaNum), data.toString(), false, false, true));
            }

            for (int i = 1; i <= diasNoMesAtual; i++) {
                LocalDate data = date.atDay(i);
                String dataStr = data.toString();
                DayOfWeek diaDaSemana = data.getDayOfWeek();

                boolean ehPassado = data.isBefore(hoje);
                boolean ehDomingoOuSegunda = (diaDaSemana == DayOfWeek.SUNDAY || diaDaSemana == DayOfWeek.MONDAY);
                boolean ehFeriado = feriados.contains(dataStr);
                boolean ehFolgaPadrao = ehDomingoOuSegunda || ehFeriado;

                boolean ehFolga;
                if (diasTrabalhoManuais.contains(dataStr)) {
                    ehFolga = false;
                } else if (folgasManuais.contains(dataStr)) {
                    ehFolga = true;
                } else {
                    ehFolga = ehFolgaPadrao;
                }

                listaDias.add(new CalendarAdapter.DiaModel(String.valueOf(i), dataStr, true, ehFolga, ehPassado));
            }

            int totalCelulas = (listaDias.size() <= 35) ? 35 : 42;
            int diasRestantes = totalCelulas - listaDias.size();
            YearMonth mesSeguinte = date.plusMonths(1);

            for (int i = 1; i <= diasRestantes; i++) {
                LocalDate data = mesSeguinte.atDay(i);
                listaDias.add(new CalendarAdapter.DiaModel(String.valueOf(i), data.toString(), false, false, false));
            }
        }

        return listaDias;
    }
}