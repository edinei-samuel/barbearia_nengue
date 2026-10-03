package com.example.barbearia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashSet;
import java.util.Set;

public class PerfilAdm extends AppCompatActivity {

    private ImageView btnVoltar, btnEditarPerfil;
    private MaterialButton btnEditar;
    private ShapeableImageView imgFotoPerfilView;
    private TextView tvNomeExibicao, tvEmailView, tvTelefoneView, tvDataNascimentoView, tvExperienciaView;
    private ChipGroup chipGroupServicosView;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perfil);

        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "vascaino@barbearia.com";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#111B27"));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(0, insets.top, 0, 0);
            return WindowInsetsCompat.CONSUMED;
        });

        btnVoltar = findViewById(R.id.btnVoltar);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnEditar = findViewById(R.id.btnEditar);

        imgFotoPerfilView = findViewById(R.id.imgFotoPerfilView);
        tvNomeExibicao = findViewById(R.id.tvNomeExibicao);
        tvEmailView = findViewById(R.id.tvEmailView);
        tvTelefoneView = findViewById(R.id.tvTelefoneView);
        tvDataNascimentoView = findViewById(R.id.tvDataNascimentoView);
        tvExperienciaView = findViewById(R.id.tvExperienciaView);
        chipGroupServicosView = findViewById(R.id.chipGroupServicosView);

        btnVoltar.setOnClickListener(v -> finish());
        btnEditarPerfil.setOnClickListener(v -> abrirTelaEdicao());
        btnEditar.setOnClickListener(v -> abrirTelaEdicao());
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDadosAtualizados();
    }

    private void carregarDadosAtualizados() {
        SharedPreferences prefs = getSharedPreferences("PerfilBarbeiroPrefs", MODE_PRIVATE);

        String nomePadrao = userId.contains("vascaino") ? "Vascaino da Navalha" : "Mestre Navalha";
        String fonePadrao = userId.contains("vascaino") ? "(47) 98812-3456" : "(47) 99988-7766";
        String expPadrao = userId.contains("vascaino") ? "5 anos de experiência" : "10 anos de experiência";

        if (tvNomeExibicao != null) tvNomeExibicao.setText(prefs.getString(userId + "_nome", nomePadrao));
        if (tvEmailView != null) tvEmailView.setText(prefs.getString(userId + "_email", userId));
        if (tvTelefoneView != null) tvTelefoneView.setText(prefs.getString(userId + "_telefone", fonePadrao));
        if (tvDataNascimentoView != null) tvDataNascimentoView.setText(prefs.getString(userId + "_dataNascimento", "02 / 05 / 1999"));
        if (tvExperienciaView != null) tvExperienciaView.setText(prefs.getString(userId + "_experiencia", expPadrao));

        String fotoUri = prefs.getString(userId + "_fotoUri", null);
        if (fotoUri != null && imgFotoPerfilView != null) {
            imgFotoPerfilView.setImageURI(Uri.parse(fotoUri));
        }

        boolean jaSalvoAntes = prefs.contains(userId + "_servicos");
        Set<String> servicos = prefs.getStringSet(userId + "_servicos", null);

        // se nao tiver nada salvo antes o usuario recebe esses dados
        if (!jaSalvoAntes) {
            servicos = new HashSet<>();
            servicos.add("Corte de Cabelo");
            servicos.add("Barba");
            servicos.add("Sobrancelha");
        }

        if (chipGroupServicosView != null) {
            chipGroupServicosView.removeAllViews();
            if (servicos != null && !servicos.isEmpty()) {
                for (String servico : servicos) {
                    Chip chip = new Chip(this);
                    chip.setText(servico);

                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
                    chip.setTextColor(Color.parseColor("#111B27"));

                    chip.setClickable(false);
                    chip.setFocusable(false);
                    chipGroupServicosView.addView(chip);
                }
            }
        }
    }

    private void abrirTelaEdicao() {
        Intent intent = new Intent(PerfilAdm.this, PerfilAdmEditActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
}