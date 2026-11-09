package com.example.barbearia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class PerfilUsuarioEdit extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView btnVoltar;
    private ShapeableImageView imgFotoPerfil;
    private MaterialButton btnAlterarFoto, btnSalvar, btnSair;
    private EditText etNome, etEmail, etTelefone, etDataNascimento;

    private String userId;
    private String fotoUriString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil_edit);

        // Recupera o ID do usuário enviado pela intent (ou usa o padrão)
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "vascaino@barbearia.com";
        }

        // Deixa a StatusBar combinando com o tema escuro do toolbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#111B27"));
        }

        inicializarComponentes();
        carregarDadosAtuais();
        configurarCliques();
    }

    private void inicializarComponentes() {
        btnVoltar = findViewById(R.id.btnVoltar);
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        btnAlterarFoto = findViewById(R.id.btnAlterarFoto);

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etTelefone = findViewById(R.id.etTelefone);
        etDataNascimento = findViewById(R.id.etDataNascimento);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnSair = findViewById(R.id.btnSair);
    }

    private void carregarDadosAtuais() {
        SharedPreferences prefs = getSharedPreferences("PerfilUsuarioPrefs", MODE_PRIVATE);

        String nomePadrao = "Jão do fejão";
        String emailPadrao = userId;
        String fonePadrao = "(47) 98812-3456";
        String dataPadrao = "02 / 05 / 1999";

        etNome.setText(prefs.getString(userId + "_nome", nomePadrao));
        etEmail.setText(prefs.getString(userId + "_email", emailPadrao));
        etTelefone.setText(prefs.getString(userId + "_telefone", fonePadrao));
        etDataNascimento.setText(prefs.getString(userId + "_dataNascimento", dataPadrao));

        fotoUriString = prefs.getString(userId + "_fotoUri", null);
        if (fotoUriString != null && imgFotoPerfil != null) {
            imgFotoPerfil.setImageURI(Uri.parse(fotoUriString));
        }
    }

    private void configurarCliques() {
        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        View.OnClickListener selecionarFotoListener = v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        };

        if (btnAlterarFoto != null) btnAlterarFoto.setOnClickListener(selecionarFotoListener);
        if (imgFotoPerfil != null) imgFotoPerfil.setOnClickListener(selecionarFotoListener);

        if (btnSalvar != null) {
            btnSalvar.setOnClickListener(v -> salvarAlteracoes());
        }

        if (btnSair != null) {
            btnSair.setOnClickListener(v -> confirmarSaidaSessao());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            fotoUriString = imageUri.toString();

            // Dá permissão persistente para ler a imagem da galeria
            try {
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            imgFotoPerfil.setImageURI(imageUri);
        }
    }

    private void salvarAlteracoes() {
        String novoNome = etNome.getText().toString().trim();
        String novoEmail = etEmail.getText().toString().trim();
        String novoTelefone = etTelefone.getText().toString().trim();
        String novaData = etDataNascimento.getText().toString().trim();

        if (novoNome.isEmpty() || novoEmail.isEmpty()) {
            Toast.makeText(this, "Nome e E-mail são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("PerfilUsuarioPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(userId + "_nome", novoNome);
        editor.putString(userId + "_email", novoEmail);
        editor.putString(userId + "_telefone", novoTelefone);
        editor.putString(userId + "_dataNascimento", novaData);

        if (fotoUriString != null) {
            editor.putString(userId + "_fotoUri", fotoUriString);
        }

        editor.apply();

        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish(); // Retorna para a tela anterior
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
        // Limpa dados de sessão se houver
        SharedPreferences preferences = getSharedPreferences("SessaoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Sessão encerrada", Toast.LENGTH_SHORT).show();

        // Redireciona para o Login limpando a pilha de telas
        Intent intent = new Intent(PerfilUsuarioEdit.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}