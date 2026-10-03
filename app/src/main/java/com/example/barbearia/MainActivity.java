package com.example.barbearia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- NÃO SEI DIREITO OQ FOI FEITO AQ, PEDI PRA IA ME AJUDAR A RESOLVER O BGLH DA TELA EM RELAÇÃO AO HORARIO E NOTIFICAÇÕES Q TAVA SOBREPONDO ---
        // 1. Configurar a cor da barra de status (onde fica o relógio)
        Window window = getWindow();
        WindowCompat.setDecorFitsSystemWindows(window, false); // Edge-to-Edge controlado

        // Define a cor de fundo da barra de status
        window.setStatusBarColor(Color.parseColor("#111B27"));

        // Se o Android for 6.0 ou superior, define os ícones da barra (hora, bateria) como Brancos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int flags = decorView.getSystemUiVisibility();
            // Desativa SYSTEM_UI_FLAG_LIGHT_STATUS_BAR para ícones brancos
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(flags);
        }

        // 2. Criar a separação (Safe Area) para o conteúdo não ficar debaixo do relógio
        // Isso aplica a separação automaticamente em todas as telas abertas a partir desta
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(0, insets.top, 0, 0); // Empurra todo o conteúdo para baixo da barra de status
            return WindowInsetsCompat.CONSUMED;
        });
        // -----------------------------------------------------------------------------

        // Apos os 3 segundos de tela de carregamento pucha a parte de fazer o login
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}