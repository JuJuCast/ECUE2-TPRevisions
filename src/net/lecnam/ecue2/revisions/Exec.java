package net.lecnam.ecue2.revisions;

public class Exec {

    public static void main(String[] args) {
        testerClient();
    }

    public static void testerClient() {
        Client c1 = new Client("Forestier", "Julien", "C001");
        Client c2 = new Client("Dupont", "Marie", "C002");
        Client c3 = new Client("Martin", "Lucas", "C001");

        System.out.println(c1);
        System.out.println(c2);

        System.out.println("c1 est égal à c2 ? " + c1.equals(c2));
        System.out.println("c1 est égal à c3 (même numéro) ? " + c1.equals(c3));
    }

    public static void testerExceptionSolde() {
        double soldeActuel = 50.0;
        double montantRetrait = 120.0;

        System.out.println("Solde du compte : " + soldeActuel + " €");
        System.out.println("Tentative de retrait de : " + montantRetrait + " €");

        try {
            if (montantRetrait > soldeActuel) {
                double manquant = montantRetrait - soldeActuel;
                throw new SoldeInsuffisantException("Erreur : Solde insuffisant pour effectuer le retrait.", manquant);
            }

            System.out.println("Retrait effectué avec succès !");

        } catch (SoldeInsuffisantException e) {
            System.out.println("\n[EXCEPTION CATTURÉE] " + e.getMessage());
            System.out.println("Montant manquant : " + e.getMontantManquant() + " €");
        }
    }
}
