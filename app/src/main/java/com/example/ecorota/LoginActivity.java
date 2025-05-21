package com.example.ecorota;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecorota.model.Usuario;
import com.example.ecorota.service.UsuarioService;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnEntrar;
    private UsuarioService usuarioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar o serviço de usuários
        usuarioService = UsuarioService.getInstance();
        usuarioService.inicializar(this);
        
        // Log para mostrar o diretório de arquivos do aplicativo
        android.util.Log.d("LoginActivity", "Diretório de arquivos do aplicativo: " + getFilesDir().getAbsolutePath());
        
        // Verificar detalhes do diretório de arquivos e permissões
        com.example.ecorota.util.FileUtil.verificarDiretorioArquivos(this);
        
        // Verificar se o arquivo de usuários existe
        java.io.File usuariosFile = new java.io.File(getFilesDir(), "usuarios.json");
        android.util.Log.d("LoginActivity", "Arquivo de usuários existe? " + usuariosFile.exists() +
                           ", Tamanho: " + (usuariosFile.exists() ? usuariosFile.length() : 0) + " bytes");

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        Button btnQueroMeCadastrar = findViewById(R.id.btnQueroMeCadastrar);

        // Configurar o listener para o botão de cadastro
        btnQueroMeCadastrar.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, com.example.ecorota.activity.CadastroActivity.class);
            startActivity(intent);
        });

        btnEntrar.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();
            
            android.util.Log.d("LoginActivity", "Tentando login com email: " + email);

            // Autentica usando o UsuarioService
            Usuario usuario = usuarioService.autenticar(email, senha);
            
            if (usuario != null) {
                android.util.Log.d("LoginActivity", "Login bem-sucedido como " + usuario.getNome());
                Toast.makeText(this, "Login bem-sucedido como " + usuario.getNome() + "!", Toast.LENGTH_SHORT).show();

                // Redireciona para a tela de gerar rota
                Intent intent = new Intent(LoginActivity.this, GerarRotaActivity.class);
                startActivity(intent);
                finish();
            } else {
                android.util.Log.d("LoginActivity", "Login falhou para email: " + email);
                Toast.makeText(this, "Email ou senha inválidos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // Reinicializar o serviço de usuários para garantir que temos os dados mais recentes
        usuarioService = UsuarioService.getInstance();
        usuarioService.inicializar(this);
        
        // Verificar novamente o arquivo de usuários ao retornar para a tela
        java.io.File usuariosFile = new java.io.File(getFilesDir(), "usuarios.json");
        android.util.Log.d("LoginActivity", "onResume: Arquivo de usuários existe? " + usuariosFile.exists() +
                          ", Tamanho: " + (usuariosFile.exists() ? usuariosFile.length() : 0) + " bytes");
        
        // Exibir lista de usuários para depuração
        java.util.List<com.example.ecorota.model.Usuario> usuarios = usuarioService.getTodosUsuarios();
        android.util.Log.d("LoginActivity", "onResume: Número de usuários disponíveis: " + 
                          (usuarios != null ? usuarios.size() : 0));
        
        if (usuarios != null) {
            for (com.example.ecorota.model.Usuario usuario : usuarios) {
                android.util.Log.d("LoginActivity", "onResume: Usuário - ID: " + usuario.getID() + 
                                 ", Nome: " + usuario.getNome() + 
                                 ", Email: " + usuario.getEmail());
            }
        }
    }
}
