package com.example.ecorota;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecorota.model.Usuario;
import com.example.ecorota.service.UsuarioService;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";
    private EditText edtNome, edtEmail, edtTelefone, edtSenha, edtConfirmarSenha;
    private Button btnCadastrar, btnVoltar;
    private UsuarioService usuarioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicialização do serviço
        usuarioService = UsuarioService.getInstance();
        usuarioService.inicializar(this);

        // Inicialização dos componentes da UI
        edtNome = findViewById(R.id.edtNomeCadastro);
        edtEmail = findViewById(R.id.edtEmailCadastro);
        edtTelefone = findViewById(R.id.edtTelefoneCadastro);
        edtSenha = findViewById(R.id.edtSenhaCadastro);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Configuração dos listeners
        btnCadastrar.setOnClickListener(v -> realizarCadastro());
        btnVoltar.setOnClickListener(v -> voltarParaLogin());
    }

    private void realizarCadastro() {
        // Obtenção e validação dos dados de entrada
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String senha = edtSenha.getText().toString();
        String confirmarSenha = edtConfirmarSenha.getText().toString();

        // Validações básicas
        if (TextUtils.isEmpty(nome)) {
            edtNome.setError("Nome é obrigatório");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email é obrigatório");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email inválido");
            return;
        }

        if (TextUtils.isEmpty(telefone)) {
            edtTelefone.setError("Telefone é obrigatório");
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            edtSenha.setError("Senha é obrigatória");
            return;
        }

        if (TextUtils.isEmpty(confirmarSenha)) {
            edtConfirmarSenha.setError("Confirme sua senha");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            edtConfirmarSenha.setError("As senhas não conferem");
            return;
        }

        // Verifica se o email já está em uso
        if (usuarioService.emailJaExiste(email)) {
            edtEmail.setError("Este email já está cadastrado");
            return;
        }

        try {
            // Criação do novo usuário (por padrão como usuário comum)
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setTelefone(telefone);
            novoUsuario.setSenha(senha);
            novoUsuario.setFuncao("USUARIO"); // Função padrão para novos cadastros

            // Salvar o usuário
            usuarioService.salvarUsuario(novoUsuario);

            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

            // Volta para a tela de login após o cadastro
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Erro ao cadastrar usuário: " + e.getMessage());
            Toast.makeText(this, "Erro ao cadastrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void voltarParaLogin() {
        finish();
    }
}