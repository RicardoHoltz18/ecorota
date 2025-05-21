package com.example.ecorota;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ScrollView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InicioActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "InicioActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Ativar a funcionalidade Edge-to-Edge (para compatibilidade com a barra de status)
        setContentView(R.layout.activity_inicio);

        // Aplique a lógica de padding nos insets na raiz do ScrollView
        ScrollView scrollView = findViewById(R.id.main);  // Assegure-se de que o ID do ScrollView seja 'main'
        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botão "Entrar"
        Button btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(v -> {
            // Inicia a LoginActivity quando o botão "Entrar" for clicado
            Intent intent = new Intent(InicioActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        
        // Configurar o botão cadastrar se existir
        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        if (btnCadastrar != null) {
            btnCadastrar.setOnClickListener(v -> {
                Intent intent = new Intent(InicioActivity.this, CadastroActivity.class);
                startActivity(intent);
            });
        }
        
        // Configurar BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_home) {
            // Já estamos na tela inicial
            return true;
        } else if (itemId == R.id.nav_mapa) {
            startActivity(new Intent(this, Mapa.class));
            return true;
        } else if (itemId == R.id.nav_lixeiras) {
            startActivity(new Intent(this, LixeirasActivity.class));
            return true;
        } else if (itemId == R.id.nav_reciclagem) {
            // Navegação para tela de reciclagem
            try {
                Class<?> reciclagemActivityClass = Class.forName("com.example.ecorota.ReciclagemActivity");
                startActivity(new Intent(this, reciclagemActivityClass));
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Classe ReciclagemActivity não encontrada", e);
                // Pelo menos mostramos um aviso ao usuário
                Log.e(TAG, "Esta funcionalidade ainda não está disponível.");
            }
            return true;
        }
        
        return false;
    }
}
