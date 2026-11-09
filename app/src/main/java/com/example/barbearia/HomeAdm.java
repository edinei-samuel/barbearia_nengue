package com.example.barbearia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeAdm extends AppCompatActivity {

    private String userId;

    // Variáveis do Sistema de Anúncios
    private ImageView imgAnuncioBanner;
    private ImageView btnFecharAnuncio;
    private List<AnuncioItem> listaAnuncios;
    private int indiceAtual = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnableRotacao;
    private static final long TEMPO_ROTACAO = 30000; // 30 segundos

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

        imgAnuncioBanner = findViewById(R.id.imgBannerFull);
        btnFecharAnuncio = findViewById(R.id.btnFecharAnuncio);

        configurarAnuncios();
    }

    private void configurarAnuncios() {
        listaAnuncios = new ArrayList<>();

        listaAnuncios.add(new AnuncioItem(R.drawable.notadez, "https://www.instagram.com/mileenafelix/"));
        listaAnuncios.add(new AnuncioItem(R.drawable.flavinhu_dupneu, "https://www.instagram.com/fkolenez/"));
        listaAnuncios.add(new AnuncioItem(R.drawable.paudogui, "https://www.instagram.com/guizzo_lz/"));
        listaAnuncios.add(new AnuncioItem(R.drawable.flarinthia, "https://instagram.com"));

        if (imgAnuncioBanner != null) {
            imgAnuncioBanner.setOnClickListener(v -> {
                if (!listaAnuncios.isEmpty()) {
                    String urlInstagram = listaAnuncios.get(indiceAtual).getInstagramUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlInstagram));
                    startActivity(intent);
                }
            });
        }

        // Botão de fechar q nao fecha nunca kkkkkkkkkkk
        if (btnFecharAnuncio != null) {
            btnFecharAnuncio.setOnClickListener(v -> {
                proximoAnuncio();
                reiniciarTimerRotacao();
            });
        }
    }

    private void iniciarRotacao() {
        if (listaAnuncios == null || listaAnuncios.isEmpty()) return;

        // Inicia com o anuncio da melhor propaganda
        indiceAtual = 0;
        exibirAnuncioAtual();

        runnableRotacao = new Runnable() {
            @Override
            public void run() {
                proximoAnuncio();
                handler.postDelayed(this, TEMPO_ROTACAO);
            }
        };
        handler.postDelayed(runnableRotacao, TEMPO_ROTACAO);
    }

    private void proximoAnuncio() {
        if (listaAnuncios == null || listaAnuncios.isEmpty()) return;
        indiceAtual = (indiceAtual + 1) % listaAnuncios.size();
        exibirAnuncioAtual();
    }

    private void exibirAnuncioAtual() {
        if (imgAnuncioBanner != null && !listaAnuncios.isEmpty()) {
            AnuncioItem anuncio = listaAnuncios.get(indiceAtual);
            imgAnuncioBanner.setImageResource(anuncio.getImagemResId());
        }
    }

    private void reiniciarTimerRotacao() {
        handler.removeCallbacks(runnableRotacao);
        handler.postDelayed(runnableRotacao, TEMPO_ROTACAO);
    }

    private void pararRotacao() {
        handler.removeCallbacks(runnableRotacao);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciarRotacao();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pararRotacao();
    }

    private static class AnuncioItem {
        private final int imagemResId;
        private final String instagramUrl;

        public AnuncioItem(int imagemResId, String instagramUrl) {
            this.imagemResId = imagemResId;
            this.instagramUrl = instagramUrl;
        }

        public int getImagemResId() { return imagemResId; }
        public String getInstagramUrl() { return instagramUrl; }
    }
}