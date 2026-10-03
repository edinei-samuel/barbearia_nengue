package com.example.barbearia;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeAdm extends AppCompatActivity {

    private TextView tvBoasVindas;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_barber);

        // Recebe o ID do barbeiro passado na LoginActivity (se houver)
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "7";
        }

        BarraNavegar.setup(this, R.id.navInicio);
    }
}