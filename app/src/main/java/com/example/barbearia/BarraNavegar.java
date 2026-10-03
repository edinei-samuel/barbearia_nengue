package com.example.barbearia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.ImageViewCompat;

public class BarraNavegar {

    public static void setup(Activity activity, int idItemAtual) {
        // Verifica qual o tipo de perfil ativo (Admin/Barbeiro ou Cliente)
        SharedPreferences preferences = activity.getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        boolean isAdmin = preferences.getBoolean("is_admin", false);
        boolean isBarber = preferences.getBoolean("is_barber", false);

        LinearLayout navInicio = activity.findViewById(R.id.navInicio);
        LinearLayout navAgendamentos = activity.findViewById(R.id.navAgendamentos);
        LinearLayout navPerfil = activity.findViewById(R.id.navPerfil);

        if (navInicio == null) return;

        int corAtiva = Color.parseColor("#FFC107");  // Amarelo
        int corInativa = Color.parseColor("#FFFFFF"); // Branco

        // Aplica as cores correspondentes ao item selecionado
        aplicarCorItem(activity, R.id.iconInicio, R.id.textInicio, idItemAtual == R.id.navInicio, corAtiva, corInativa);
        aplicarCorItem(activity, R.id.iconAgendamentos, R.id.textAgendamentos, idItemAtual == R.id.navAgendamentos, corAtiva, corInativa);
        aplicarCorItem(activity, R.id.iconPerfil, R.id.textPerfil, idItemAtual == R.id.navPerfil, corAtiva, corInativa);

        // Ação: Início
        navInicio.setOnClickListener(v -> {
            if (idItemAtual != R.id.navInicio) {
                Class<?> destino = (isAdmin || isBarber) ? HomeAdm.class : HomeAdm.class;
                abrirTela(activity, destino);
            }
        });

        // Ação: Agendamentos
        navAgendamentos.setOnClickListener(v -> {
            if (idItemAtual != R.id.navAgendamentos) {
                Class<?> destino = (isAdmin || isBarber) ? AgendaBarber.class : activity_inexistente.class;
                abrirTela(activity, destino);
            }
        });

        // Ação: Perfil
        navPerfil.setOnClickListener(v -> {
            if (idItemAtual != R.id.navPerfil) {
                Class<?> destino = (isAdmin || isBarber) ? PerfilAdm.class : activity_inexistente.class;
                abrirTela(activity, destino);
            }
        });
    }

    private static void abrirTela(Activity activity, Class<?> telaDestino) {
        if (telaDestino == null) return;
        Intent intent = new Intent(activity, telaDestino);
        activity.startActivity(intent);
        activity.finish();
    }

    private static void aplicarCorItem(Activity activity, int idIcon, int idText, boolean selecionado, int corAtiva, int corInativa) {
        ImageView icon = activity.findViewById(idIcon);
        TextView text = activity.findViewById(idText);

        int corFinal = selecionado ? corAtiva : corInativa;

        if (icon != null) {
            ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(corFinal));
        }
        if (text != null) {
            text.setTextColor(corFinal);
        }
    }
}