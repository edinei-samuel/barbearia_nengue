package com.example.barbearia;

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
            btnVoltarInexistente.setOnClickListener(v -> finish());
        }
    }
}