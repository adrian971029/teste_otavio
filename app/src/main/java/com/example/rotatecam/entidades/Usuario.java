package com.example.rotatecam.entidades;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {

    private String id;
    private String nome;
    private String sobrenome;
    private String email;

    public Usuario() {
    }

    public Usuario(String id, String nome, String sobrenome, String email) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
    }

    public void Salva() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Usuarios").child(String.valueOf(getId())).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
