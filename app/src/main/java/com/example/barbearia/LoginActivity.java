package com.example.barbearia;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnEntrar;
    private TextView tvCadastreSe, tvEsqueceuSenha;
    private ImageView btnGoogle, btnVerSenha;

    // Estrutura/Modelo de dados para o Barbeiro
    public static class Barbeiro {
        private String id;
        private String senhaInterna;
        private String nome;

        public Barbeiro(String id, String senhaInterna, String nome) {
            this.id = id;
            this.senhaInterna = senhaInterna;
            this.nome = nome;
        }

        public String getId() { return id; }
        public String getSenhaInterna() { return senhaInterna; }
        public String getNome() { return nome; }
    }

    private Barbeiro barbeiroLogado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvCadastreSe = findViewById(R.id.tvCadastreSe);
        tvEsqueceuSenha = findViewById(R.id.tvEsqueceuSenha);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnVerSenha = findViewById(R.id.btnVerSenha);

        if (btnVerSenha != null && etSenha != null) {
            btnVerSenha.setImageResource(android.R.drawable.ic_secure);
            btnVerSenha.setOnClickListener(v -> alternarVisibilidadeSenha(etSenha, btnVerSenha));
        }

        // pega o texto de duas cor q meu super ultramega raciocinio ajudou a fazer né
        if (tvCadastreSe != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvCadastreSe.setText(Html.fromHtml(getString(R.string.texto_nao_tem_conta), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvCadastreSe.setText(Html.fromHtml(getString(R.string.texto_nao_tem_conta)));
            }
        }

        btnEntrar.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha e-mail e senha!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (validarUsuario(email, senha)) {

                // ve se o usuario é o barbeiro ou cliente
                if (email.contains("nenguebarber")) {
                    exibirDialogBarbeiro();
                } else {
                    Toast.makeText(this, "Login realizado como Cliente!", Toast.LENGTH_SHORT).show();

                    // Caso seja o cliente vai para a tela do VASCO gigante da colina | OBS SE NÃO FUNCIONAR O SISTEMA CAIU
                    // Fiz só o adm por enquanto
                    Intent intent = new Intent(LoginActivity.this, activity_inexistente.class);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(this, "E-mail ou senha incorretos!", Toast.LENGTH_SHORT).show();
            }
        });

        tvCadastreSe.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        //msg agradavel pro amiguinho menos para a EXCELENTISSIMA MARAVIGOLD PROFESSORA MILENA
        tvEsqueceuSenha.setOnClickListener(v -> {
            Toast.makeText(this, "Se fodeu otario kkkkkkkkkkkkkk", Toast.LENGTH_SHORT).show();
        });

        // ola macaquito
        btnGoogle.setOnClickListener(v -> {
            String url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQP_wsrDfdtdPPy-Fe7IH2hGZ5nCc1IjXk7c4VArCNUpA&s";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    private void alternarVisibilidadeSenha(EditText editText, ImageView imgOlho) {
        if (editText.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
            editText.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
            if (imgOlho != null) {
                imgOlho.setImageResource(android.R.drawable.ic_menu_view);
            }
        } else {
            editText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            if (imgOlho != null) {
                imgOlho.setImageResource(android.R.drawable.ic_secure);
            }
        }
        editText.setSelection(editText.getText().length());
    }

    private boolean validarUsuario(String email, String senha) {
        Map<String, String> usuariosCadastrados = new HashMap<>();
        usuariosCadastrados.put("cliente@gmail.com", "sete");
        usuariosCadastrados.put("nenguebarber@gmail.com", "sete");

        return usuariosCadastrados.containsKey(email) && usuariosCadastrados.get(email).equals(senha);
    }

    private Barbeiro autenticarBarbeiro(String id, String senhaInterna) {
        Barbeiro[] barbeiros = {
                new Barbeiro("7", "sete", "Vascaino da Navalha")
        };

        for (Barbeiro b : barbeiros) {
            if (b.getId().equals(id) && b.getSenhaInterna().equals(senhaInterna)) {
                return b;
            }
        }
        return null;
    }

    // Vai para a telinha de validar o admin
    private void exibirDialogBarbeiro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.login_barber, null);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText etIdBarbeiro = view.findViewById(R.id.etIdBarbeiro);
        EditText etSenhaBarbeiro = view.findViewById(R.id.etSenhaBarbeiro);
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarBarbeiro);
        Button btnCancelar = view.findViewById(R.id.btnCancelarDialog);

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnConfirmar.setOnClickListener(v -> {
            String id = etIdBarbeiro.getText().toString().trim();
            String senha = etSenhaBarbeiro.getText().toString().trim();

            if (id.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha o ID e a Senha!", Toast.LENGTH_SHORT).show();
                return;
            }

            Barbeiro barbeiroEncontrado = autenticarBarbeiro(id, senha);

            if (barbeiroEncontrado != null) {
                barbeiroLogado = barbeiroEncontrado;
                Toast.makeText(this, "Bem-vindo, " + barbeiroLogado.getNome() + "!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                Intent intent = new Intent(LoginActivity.this, PerfilAdm.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "ID ou Senha de Barbeiro incorretos!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}