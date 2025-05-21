package com.example.ecorota.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecorota.R;
import com.example.ecorota.model.Usuario;
import com.example.ecorota.util.FileUtil;

import java.util.ArrayList;

public class CadastroActivity extends AppCompatActivity {
    
    private static final String TAG = "CadastroActivity";
    
    // Elementos da UI
    private EditText edtNomeCadastro;
    private EditText edtEmailCadastro;
    private EditText edtTelefoneCadastro;
    private EditText edtSenhaCadastro;
    private EditText edtConfirmarSenhaCadastro;
    private Button btnCadastrar;
    private Button btnVoltar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        
        // Verificar detalhes do diretório de arquivos antes de qualquer operação
        com.example.ecorota.util.FileUtil.verificarDiretorioArquivos(this);
        
        // Inicializa os elementos da UI
        inicializarElementos();
        
        // Configura os listeners para os botões
        configurarListeners();
    }
    
    private void inicializarElementos() {
        edtNomeCadastro = findViewById(R.id.edtNomeCadastro);
        edtEmailCadastro = findViewById(R.id.edtEmailCadastro);
        edtTelefoneCadastro = findViewById(R.id.edtTelefoneCadastro);
        edtSenhaCadastro = findViewById(R.id.edtSenhaCadastro);
        edtConfirmarSenhaCadastro = findViewById(R.id.edtConfirmarSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltar = findViewById(R.id.btnVoltar);
    }
    
    private void configurarListeners() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });
        
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volta para a tela anterior (Login)
            }
        });
    }
    
    private void cadastrarUsuario() {
        // Obtém os valores dos campos
        String nome = edtNomeCadastro.getText().toString().trim();
        String email = edtEmailCadastro.getText().toString().trim();
        String telefone = edtTelefoneCadastro.getText().toString().trim();
        String senha = edtSenhaCadastro.getText().toString();
        String confirmarSenha = edtConfirmarSenhaCadastro.getText().toString();
        
        // Valida os campos
        if (!validarCampos(nome, email, telefone, senha, confirmarSenha)) {
            return;
        }
        
        // Verifica se o usuário já existe (usando o serviço centralizado)
        com.example.ecorota.service.UsuarioService usuarioService = 
            com.example.ecorota.service.UsuarioService.getInstance();
        usuarioService.inicializar(this);
        
        if (usuarioService.emailJaExiste(email)) {
            Toast.makeText(this, "Este email já está cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Cria um novo usuário (o ID será gerado automaticamente)
            Usuario novoUsuario = new Usuario("", nome, email, telefone, "USUARIO", senha);
            novoUsuario.setRotas(new ArrayList<>());
            
            Log.d(TAG, "Tentando salvar novo usuário: " + nome + ", Email: " + email);
            
            // Salva o usuário usando o serviço centralizado (que usa o repositório)
            usuarioService.salvarUsuario(novoUsuario);
            
            // Verificando se o usuário foi salvo corretamente
            boolean verificacao = usuarioService.emailJaExiste(email);
            Log.d(TAG, "Verificação após cadastro - email existe: " + verificacao);
            
            if (verificacao) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Usuário cadastrado: " + novoUsuario.getNome() + ", ID: " + novoUsuario.getID());
                
                // Volta para a tela de login
                finish();
            } else {
                Toast.makeText(this, "Erro ao realizar cadastro: usuário não foi salvo", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erro ao verificar usuário após cadastro");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao realizar cadastro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Exceção ao cadastrar usuário: " + e.getMessage());
        }
    }
    
    private boolean validarCampos(String nome, String email, String telefone, String senha, String confirmarSenha) {
        // Verifica se todos os campos estão preenchidos
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || 
            TextUtils.isEmpty(telefone) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmarSenha)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Valida o formato do email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Digite um email válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Valida o formato do telefone (simplificado)
        if (telefone.length() < 10) {
            Toast.makeText(this, "Digite um telefone válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Verifica se as senhas são iguais
        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Verifica o tamanho da senha
        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
}
