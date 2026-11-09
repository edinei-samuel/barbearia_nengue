package com.example.barbearia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class activity_inexistente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inexistente);

        Button btnVoltarInexistente = findViewById(R.id.btnVoltarInexistente);
        if (btnVoltarInexistente != null) {
            btnVoltarInexistente.setOnClickListener(v -> {
                // Direciona com segurança para a MainActivity (ou LoginActivity) limpando o histórico
                Intent intent = new Intent(activity_inexistente.this, HomeUsuario.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}