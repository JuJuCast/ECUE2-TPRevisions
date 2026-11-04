package net.lecnam.ecue2.revisions;

import java.time.LocalDate;

public abstract class CompteBancaire {
    private String numeroCompte;
    private double solde;
    private Client titulaire;
    private LocalDate dateOuverture;

    public CompteBancaire(String numeroCompte, Client titulaire) {
        this.numeroCompte = numeroCompte;
        this.titulaire = titulaire;
        this.dateOuverture = LocalDate.now();
        this.solde = 0.0;
    }

    public CompteBancaire(String numeroCompte, Client titulaire, LocalDate dateOuverture, double solde) {
        this.numeroCompte = numeroCompte;
        this.titulaire = titulaire;
        this.dateOuverture = dateOuverture;
        this.solde = solde;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public double getSolde() {
        return solde;
    }

    public Client getTitulaire() {
        return titulaire;
    }

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void deposer(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant à déposer doit être strictement positif.");
        }
        this.solde += montant;
    }

    protected void debiter(double montant) {
        this.solde -= montant;
    }

    public abstract void retirer(double montant) throws SoldeInsuffisantException;

    public abstract double calculerInterets();


    public void appliquerInterets() {
        double interets = calculerInterets();
        if (interets > 0) {
            this.solde += interets;
        }
    }

    @Override
    public String toString() {
        return "Compte N°: " + numeroCompte +
                " | Titulaire: " + titulaire +
                " | Solde: " + solde + " €" +
                " | Date d'ouverture: " + dateOuverture;
    }
}