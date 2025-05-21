package com.example.ecorota.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String ID;
    private String nome;
    private String email;
    private String telefone;
    private String funcao;
    private String senha;
    
    // Lista de rotas associadas ao usuário (se for motorista)
    private List<Rota> rotas;
    
    public Usuario() {
        this.rotas = new ArrayList<>();
    }
    
    public Usuario(String ID, String nome, String email, String telefone, String funcao, String senha) {
        this.ID = ID;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.funcao = funcao;
        this.senha = senha;
        this.rotas = new ArrayList<>();
    }
    
    public boolean autenticar(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }
    
    // Getters e Setters
    public String getID() {
        return ID;
    }
    
    public void setID(String ID) {
        this.ID = ID;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getFuncao() {
        return funcao;
    }
    
    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public List<Rota> getRotas() {
        return rotas;
    }
    
    public void setRotas(List<Rota> rotas) {
        this.rotas = rotas;
    }
    
    public void adicionarRota(Rota rota) {
        if (this.rotas == null) {
            this.rotas = new ArrayList<>();
        }
        this.rotas.add(rota);
    }
    
    public void addRota(Rota rota) {
        this.rotas.add(rota);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "ID='" + ID + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", funcao='" + funcao + '\'' +
                '}';
    }
}