package com.example.barbearia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashSet;
import java.util.Set;

public class PerfilAdm extends AppCompatActivity {

    private ImageView btnVoltar, btnEditarPerfil;
    private MaterialButton btnEditar, btnSairConta;
    private ShapeableImageView imgFotoPerfilView;
    private TextView tvNomeExibicao, tvEmailView, tvTelefoneView, tvDataNascimentoView, tvExperienciaView;
    private ChipGroup chipGroupServicosView;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perfil);

        BarraNavegar.setup(this, R.id.navPerfil);

        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "vascaino@barbearia.com";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#111B27"));
        }

        btnVoltar = findViewById(R.id.btnVoltar);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnEditar = findViewById(R.id.btnEditar);
        btnSairConta = findViewById(R.id.btnSairConta);

        imgFotoPerfilView = findViewById(R.id.imgFotoPerfilView);
        tvNomeExibicao = findViewById(R.id.tvNomeExibicao);
        tvEmailView = findViewById(R.id.tvEmailView);
        tvTelefoneView = findViewById(R.id.tvTelefoneView);
        tvDataNascimentoView = findViewById(R.id.tvDataNascimentoView);
        tvExperienciaView = findViewById(R.id.tvExperienciaView);
        chipGroupServicosView = findViewById(R.id.chipGroupServicosView);

        if (btnVoltar != null) btnVoltar.setOnClickListener(v -> finish());
        if (btnEditarPerfil != null) btnEditarPerfil.setOnClickListener(v -> abrirTelaEdicao());
        if (btnEditar != null) btnEditar.setOnClickListener(v -> abrirTelaEdicao());
        if (btnSairConta != null) btnSairConta.setOnClickListener(v -> confirmarSaidaSessao());
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

    private void confirmarSaidaSessao() {
        new AlertDialog.Builder(this)
                .setTitle("Sair da Conta")
                .setMessage("Deseja realmente encerrar a sessão?")
                .setPositiveButton("Sair", (dialog, which) -> encerrarSessao())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void encerrarSessao() {
        // 1. Limpa os dados salvos da sessão do usuário
        SharedPreferences preferences = getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Sessão encerrada", Toast.LENGTH_SHORT).show();

        // 2. Redireciona para a tela de Login e limpa o histórico de telas
        Intent intent = new Intent(PerfilAdm.this, LoginActivity.class); // Altere 'LoginActivity' se a sua classe de login tiver outro nome
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}