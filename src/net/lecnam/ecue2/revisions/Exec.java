package net.lecnam.ecue2.revisions;

public class Exec {

    public static void main(String[] args) {
        testerClient();
        testerExceptionSolde();
        testerCompteBancaire();
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

    public static void testerCompteBancaire() {
        // 1. Création d'un client
        Client client = new Client("Forestier", "Julien", "C001");

        // 2. Création d'un compte de test via une classe anonyme (puisqu'elle est abstraite)
        CompteBancaire compte = new CompteBancaire("FR7612345678", client) {
            @Override
            public void retirer(double montant) throws SoldeInsuffisantException {
                if (montant > getSolde()) {
                    double manquant = montant - getSolde();
                    throw new SoldeInsuffisantException("Retrait impossible : fonds insuffisants.", manquant);
                }
                debiter(montant); // Utilisation de la méthode protected de la classe mère
            }

            @Override
            public double calculerInterets() {
                return 0.0;
            }
        };

        // --- Test de l'état initial ---
        System.out.println("=== ÉTAT INITIAL ===");
        System.out.println(compte);
        System.out.println("Solde actuel : " + compte.getSolde() + " €\n");

        // --- Test d'un dépôt valide ---
        System.out.println("=== TEST DÉPÔT (500 €) ===");
        compte.deposer(500.0);
        System.out.println("Nouveau solde : " + compte.getSolde() + " €\n");

        // --- Test d'un dépôt invalide (montant négatif ou nul) ---
        System.out.println("=== TEST DÉPÔT INVALIDE (-50 €) ===");
        try {
            compte.deposer(-50.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception bien attrapée : " + e.getMessage() + "\n");
        }

        // --- Test d'un retrait valide (200 €) ---
        System.out.println("=== TEST RETRAIT VALIDE (200 €) ===");
        try {
            compte.retirer(200.0);
            System.out.println("Retrait réussi ! Solde restant : " + compte.getSolde() + " €\n");
        } catch (SoldeInsuffisantException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        // --- Test d'un retrait trop élevé (pour déclencher l'exception checked) ---
        System.out.println("=== TEST RETRAIT EXCESSIF (1000 €) ===");
        try {
            compte.retirer(1000.0);
        } catch (SoldeInsuffisantException e) {
            System.out.println("Exception attrapée avec succès !");
            System.out.println("Message : " + e.getMessage());
            System.out.println("Montant manquant : " + e.getMontantManquant() + " €");
        }

    }
}