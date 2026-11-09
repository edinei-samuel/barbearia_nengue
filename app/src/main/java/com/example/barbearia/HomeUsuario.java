package com.example.barbearia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeUsuario extends AppCompatActivity {

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
        setContentView(R.layout.activity_home);

        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "1";
        }

        BarraNavegar.setup(this, R.id.navInicio);

        // Configuração do RecyclerView de Serviços
        RecyclerView recyclerServicos = findViewById(R.id.recyclerServicos);
        recyclerServicos.setLayoutManager(new LinearLayoutManager(this));

        List<ServicoModel> meusServicos = new ArrayList<>();
        meusServicos.add(new ServicoModel("Corte de Cabelo", "Corte moderno e clássico", R.drawable.cabelo));
        meusServicos.add(new ServicoModel("Barba", "Barba alinhada com toalha quentinha", R.drawable.barba));
        meusServicos.add(new ServicoModel("Completo", "Pacote com corte de cabelo e barba", R.drawable.completo));

        ServicoAdapter adapter = new ServicoAdapter(meusServicos);
        recyclerServicos.setAdapter(adapter);

        // =========================================================================
        // CONFIGURAÇÃO DOS BOTÕES DE AGENDAMENTO PARA ABRIR A NOVA TELA
        // =========================================================================
        View cardAgendar = findViewById(R.id.cardAgendar);
        if (cardAgendar != null) {
            cardAgendar.setOnClickListener(v -> {
                Intent intent = new Intent(HomeUsuario.this, NovoAgendamentoActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            });
        }

        View btnAgendar = findViewById(R.id.btnAgendar);
        if (btnAgendar != null) {
            btnAgendar.setOnClickListener(v -> {
                Intent intent = new Intent(HomeUsuario.this, NovoAgendamentoActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            });
        }
        // =========================================================================

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

        // Clique no banner para abrir o perfil do Instagram
        if (imgAnuncioBanner != null) {
            imgAnuncioBanner.setOnClickListener(v -> {
                if (!listaAnuncios.isEmpty()) {
                    String urlInstagram = listaAnuncios.get(indiceAtual).getInstagramUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlInstagram));
                    startActivity(intent);
                }
            });
        }

        if (btnFecharAnuncio != null) {
            btnFecharAnuncio.setOnClickListener(v -> {
                proximoAnuncio();
                reiniciarTimerRotacao();
            });
        }
    }

    private void iniciarRotacao() {
        if (listaAnuncios == null || listaAnuncios.isEmpty()) return;

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

    // ================= CLASSES AUXILIARES =================

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

    public static class ServicoModel {
        private String titulo;
        private String descricao;
        private int imagemResId;

        public ServicoModel(String titulo, String descricao, int imagemResId) {
            this.titulo = titulo;
            this.descricao = descricao;
            this.imagemResId = imagemResId;
        }

        public String getTitulo() { return titulo; }
        public String getDescricao() { return descricao; }
        public int getImagemResId() { return imagemResId; }
    }

    public static class ServicoAdapter extends RecyclerView.Adapter<ServicoAdapter.ServicoViewHolder> {
        private List<ServicoModel> listaServicos;

        public ServicoAdapter(List<ServicoModel> listaServicos) {
            this.listaServicos = listaServicos;
        }

        @NonNull
        @Override
        public ServicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.servcompleto, parent, false);
            return new ServicoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ServicoViewHolder holder, int position) {
            ServicoModel servico = listaServicos.get(position);
            holder.tvTitulo.setText(servico.getTitulo());
            holder.tvDescricao.setText(servico.getDescricao());
            holder.imgIcone.setImageResource(servico.getImagemResId());

            // Clique no botão de adicionar (+) do serviço na lista
            holder.btnAdicionarServico.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, NovoAgendamentoActivity.class);
                intent.putExtra("SERVICO_SELECIONADO", servico.getTitulo());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return listaServicos.size();
        }

        public static class ServicoViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitulo, tvDescricao;
            ImageView imgIcone;
            com.google.android.material.button.MaterialButton btnAdicionarServico;

            public ServicoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitulo = itemView.findViewById(R.id.tvTituloServico);
                tvDescricao = itemView.findViewById(R.id.tvDescricaoServico);
                imgIcone = itemView.findViewById(R.id.imgServicoIcone);
                btnAdicionarServico = itemView.findViewById(R.id.btnAdicionarServico);
            }
        }
    }
}