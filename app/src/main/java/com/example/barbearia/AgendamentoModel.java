package com.example.barbearia;

public class AgendamentoModel {
    private String nomeCliente;
    private String servico;
    private String horario;
    private int fotoResId;

    public AgendamentoModel(String nomeCliente, String servico, String horario, int fotoResId) {
        this.nomeCliente = nomeCliente;
        this.servico = servico;
        this.horario = horario;
        this.fotoResId = fotoResId;
    }

    public String getNomeCliente() { return nomeCliente; }
    public String getServico() { return servico; }
    public String getHorario() { return horario; }
    public int getFotoResId() { return fotoResId; }
}