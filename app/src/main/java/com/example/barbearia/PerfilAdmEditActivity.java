package com.example.barbearia;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class PerfilAdmEditActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etTelefone, etDataNascimento, etExperiencia;
    private MaterialButton btnSalvar, btnSair, btnAlterarFoto;
    private ImageView btnVoltar;
    private ShapeableImageView imgFotoPerfil;
    private ChipGroup chipGroupServicos;

    private String uriFotoSelecionada = null;
    private String userId;

    private final ActivityResultLauncher<String> selecionarFotoLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    uriFotoSelecionada = uri.toString();
                    imgFotoPerfil.setImageURI(uri);
                    Toast.makeText(this, "Foto alterada!", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perfil_edit);

        // Recupera o ID do utilizador logado
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "vascaino@barbearia.com";
        }

        // Configuração da cor da barra de status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#111B27"));
        }

        // bagulho da ia la no MainActivity
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(0, insets.top, 0, 0);
            return WindowInsetsCompat.CONSUMED;
        });

        btnVoltar = findViewById(R.id.btnVoltar);
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        btnAlterarFoto = findViewById(R.id.btnAlterarFoto);
        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etTelefone = findViewById(R.id.etTelefone);
        etDataNascimento = findViewById(R.id.etDataNascimento);
        etExperiencia = findViewById(R.id.etExperiencia);
        chipGroupServicos = findViewById(R.id.chipGroupServicos);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnSair = findViewById(R.id.btnSair);

        carregarDadosCampos();

        btnVoltar.setOnClickListener(v -> finish());
        btnAlterarFoto.setOnClickListener(v -> selecionarFotoLauncher.launch("image/*"));
        etDataNascimento.setOnClickListener(v -> abrirDatePicker());

        btnSalvar.setOnClickListener(v -> {
            salvarDadosLocais();
            Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnSair.setOnClickListener(v -> finish());
    }

    private void carregarDadosCampos() {
        SharedPreferences prefs = getSharedPreferences("PerfilBarbeiroPrefs", MODE_PRIVATE);

        String nomePadrao = userId.contains("vascaino") ? "Vascaino da Navalha" : "Mestre Navalha";
        String fonePadrao = userId.contains("vascaino") ? "(47) 98812-3456" : "(47) 99988-7766";
        String expPadrao = userId.contains("vascaino") ? "5 anos" : "10 anos";

        etNome.setText(prefs.getString(userId + "_nome", nomePadrao));
        etEmail.setText(prefs.getString(userId + "_email", userId));
        etTelefone.setText(prefs.getString(userId + "_telefone", fonePadrao));
        etDataNascimento.setText(prefs.getString(userId + "_dataNascimento", "02 / 05 / 1999"));
        etExperiencia.setText(prefs.getString(userId + "_experiencia", expPadrao));

        String fotoUri = prefs.getString(userId + "_fotoUri", null);
        if (fotoUri != null) {
            imgFotoPerfil.setImageURI(Uri.parse(fotoUri));
            uriFotoSelecionada = fotoUri;
        }

        Set<String> servicosSalvos = prefs.getStringSet(userId + "_servicos", null);
        if (servicosSalvos != null) {
            for (int i = 0; i < chipGroupServicos.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupServicos.getChildAt(i);
                chip.setChecked(servicosSalvos.contains(chip.getText().toString()));
            }
        } else {
            // Seleção padrão para o primeiro acesso
            for (int i = 0; i < chipGroupServicos.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupServicos.getChildAt(i);
                String texto = chip.getText().toString();
                if (texto.equals("Corte de Cabelo") || texto.equals("Barba") || texto.equals("Sobrancelha")) {
                    chip.setChecked(true);
                }
            }
        }
    }

    private void salvarDadosLocais() {
        SharedPreferences prefs = getSharedPreferences("PerfilBarbeiroPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(userId + "_nome", etNome.getText().toString().trim());
        editor.putString(userId + "_email", etEmail.getText().toString().trim());
        editor.putString(userId + "_telefone", etTelefone.getText().toString().trim());
        editor.putString(userId + "_dataNascimento", etDataNascimento.getText().toString().trim());
        editor.putString(userId + "_experiencia", etExperiencia.getText().toString().trim());

        if (uriFotoSelecionada != null) {
            editor.putString(userId + "_fotoUri", uriFotoSelecionada);
        }

        Set<String> servicosSelecionados = new HashSet<>();
        for (int i = 0; i < chipGroupServicos.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupServicos.getChildAt(i);
            if (chip.isChecked()) {
                servicosSelecionados.add(chip.getText().toString());
            }
        }

        // Força a remoção da referência antiga antes de gravar a nova lista
        editor.remove(userId + "_servicos");
        editor.apply();

        editor.putStringSet(userId + "_servicos", servicosSelecionados);
        editor.apply();
    }

    private void abrirDatePicker() {
        Calendar calendario = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> etDataNascimento.setText(String.format("%02d / %02d / %d", day, month + 1, year)),
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}