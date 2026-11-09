package com.example.barbearia;

public class Horariodisp {
    private String horario;
    private boolean selecionado;

    public Horariodisp(String horario) {
        this.horario = horario;
        this.selecionado = false;
    }

    public String getHorario() {
        return horario;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
}