package net.lecnam.ecue2.revisions;

public class SoldeInsuffisantException extends Exception {
    private double montantManquant;

    public SoldeInsuffisantException(String message) {
        super(message);
        this.montantManquant = 0.0;
    }

    public SoldeInsuffisantException(String message, double montantManquant) {
        super(message);
        this.montantManquant = montantManquant;
    }

    public double getMontantManquant() {
        return montantManquant;
    }
}