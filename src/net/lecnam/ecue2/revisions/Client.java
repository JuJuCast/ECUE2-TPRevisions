package net.lecnam.ecue2.revisions;

import java.util.Objects;

public class Client {
    private String nom;
    private String prenom;
    private String numeroClient;

    public Client(String nom, String prenom, String numeroClient) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroClient = numeroClient;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNumeroClient() {
        return numeroClient;
    }

    @Override
    public String toString() {
        return "Client [N°: " + numeroClient + "] " + prenom + " " + nom;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Client client = (Client) obj;
        return Objects.equals(numeroClient, client.numeroClient);
    }
}