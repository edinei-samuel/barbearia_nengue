package com.example.barbearia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class PerfilUsuario extends AppCompatActivity {

    private ImageView btnVoltar, btnEditarPerfil;
    private MaterialButton btnSairConta;
    private ShapeableImageView imgFotoPerfilView;
    private TextView tvNomeExibicao, tvEmailView, tvTelefoneView, tvDataNascimentoView;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        // Configura a barra de navegação inferior destacando o item Perfil
        BarraNavegar.setup(this, R.id.navPerfil);

        // Recupera o ID do usuário enviado pela Intent (ou usa um padrão)
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "vascaino@barbearia.com";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#111B27"));
        }

        inicializarComponentes();
        configurarCliques();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Carrega os dados atualizados sempre que a tela ganha foco (ex: ao voltar da edição)
        carregarDadosUsuario();
    }

    private void inicializarComponentes() {
        btnVoltar = findViewById(R.id.btnVoltar);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnSairConta = findViewById(R.id.btnSairConta);

        imgFotoPerfilView = findViewById(R.id.imgFotoPerfilView);
        tvNomeExibicao = findViewById(R.id.tvNomeExibicao);
        tvEmailView = findViewById(R.id.tvEmailView);
        tvTelefoneView = findViewById(R.id.tvTelefoneView);
        tvDataNascimentoView = findViewById(R.id.tvDataNascimentoView);
    }

    private void carregarDadosUsuario() {
        SharedPreferences prefs = getSharedPreferences("PerfilUsuarioPrefs", MODE_PRIVATE);

        String nomePadrao = "Jão do fejão";
        String emailPadrao = userId;
        String fonePadrao = "(47) 98812-3456";
        String dataPadrao = "02 / 05 / 1999";

        // Busca nas preferências usando o userId como chave base
        String nomeSalvo = prefs.getString(userId + "_nome", nomePadrao);
        String emailSalvo = prefs.getString(userId + "_email", emailPadrao);
        String telefoneSalvo = prefs.getString(userId + "_telefone", fonePadrao);
        String dataSalva = prefs.getString(userId + "_dataNascimento", dataPadrao);

        if (tvNomeExibicao != null) tvNomeExibicao.setText(nomeSalvo);
        if (tvEmailView != null) tvEmailView.setText(emailSalvo);
        if (tvTelefoneView != null) tvTelefoneView.setText(telefoneSalvo);
        if (tvDataNascimentoView != null) tvDataNascimentoView.setText(dataSalva);

        // Carrega a foto de perfil salva (se houver)
        String fotoUri = prefs.getString(userId + "_fotoUri", null);
        if (fotoUri != null && imgFotoPerfilView != null) {
            imgFotoPerfilView.setImageURI(Uri.parse(fotoUri));
        }
    }

    private void configurarCliques() {
        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        if (btnEditarPerfil != null) {
            btnEditarPerfil.setOnClickListener(v -> {
                // Abre a tela de edição passando o ID do usuário atual
                Intent intent = new Intent(PerfilUsuario.this, PerfilUsuarioEdit.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            });
        }

        if (btnSairConta != null) {
            btnSairConta.setOnClickListener(v -> confirmarSaidaSessao());
        }
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
        // Limpa dados de sessão salvos
        SharedPreferences preferences = getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Sessão encerrada", Toast.LENGTH_SHORT).show();

        // Redireciona para a tela de Login e limpa o histórico de activities anteriores
        Intent intent = new Intent(PerfilUsuario.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}