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
}
