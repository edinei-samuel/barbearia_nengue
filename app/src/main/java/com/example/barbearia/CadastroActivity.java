package com.example.barbearia;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNomeCadastro, etEmailCadastro, etTelefoneCadastro, etDataNascimentoCadastro, etSenhaCadastro, etConfirmarSenhaCadastro;
    private Button btnCadastrar;
    private TextView tvVoltarLogin;
    private ImageView btnVerSenhaCadastro, btnVerConfirmarSenhaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNomeCadastro = findViewById(R.id.etNomeCadastro);
        etEmailCadastro = findViewById(R.id.etEmailCadastro);
        etTelefoneCadastro = findViewById(R.id.etTelefoneCadastro);
        etDataNascimentoCadastro = findViewById(R.id.etDataNascimentoCadastro);
        etSenhaCadastro = findViewById(R.id.etSenhaCadastro);
        etConfirmarSenhaCadastro = findViewById(R.id.etConfirmarSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        tvVoltarLogin = findViewById(R.id.tvVoltarLogin);
        btnVerSenhaCadastro = findViewById(R.id.btnVerSenhaCadastro);
        btnVerConfirmarSenhaCadastro = findViewById(R.id.btnVerConfirmarSenhaCadastro);

        if (btnVerSenhaCadastro != null && etSenhaCadastro != null) {
            btnVerSenhaCadastro.setOnClickListener(v -> alternarVisibilidadeSenha(etSenhaCadastro, btnVerSenhaCadastro));
        }

        if (btnVerConfirmarSenhaCadastro != null && etConfirmarSenhaCadastro != null) {
            btnVerConfirmarSenhaCadastro.setOnClickListener(v -> alternarVisibilidadeSenha(etConfirmarSenhaCadastro, btnVerConfirmarSenhaCadastro));
        }

        // Aplica o texto com duas cores puxando do strings.xml IA salvou em fessora, basicamente essa parada q faz ele ter as duas cores la: amarelo e branco
        if (tvVoltarLogin != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvVoltarLogin.setText(Html.fromHtml(getString(R.string.texto_ja_tem_conta), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvVoltarLogin.setText(Html.fromHtml(getString(R.string.texto_ja_tem_conta)));
            }
        }

        // Ação do Botão Cadastrar
        btnCadastrar.setOnClickListener(v -> {
            String nome = etNomeCadastro.getText().toString().trim();
            String email = etEmailCadastro.getText().toString().trim();
            String telefone = etTelefoneCadastro.getText().toString().trim();
            String dataNascimento = etDataNascimentoCadastro.getText().toString().trim();
            String senha = etSenhaCadastro.getText().toString().trim();
            String confirmarSenha = etConfirmarSenhaCadastro.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else if (!senha.equals(confirmarSenha)) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Fecha a tela de cadastro e regressa ao login
            }
        });

        // Voltar para o Login
        tvVoltarLogin.setOnClickListener(v -> finish());
    }

    // Mostrar ou esconder senha
    private void alternarVisibilidadeSenha(EditText editText, ImageView imgOlho) {
        if (editText.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
            editText.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
            if (imgOlho != null) {
                imgOlho.setImageResource(android.R.drawable.ic_menu_view); // Olho
            }
        } else {
            editText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            if (imgOlho != null) {
                imgOlho.setImageResource(android.R.drawable.ic_secure); // Cadeado
            }
        }
        editText.setSelection(editText.getText().length());
    }
}