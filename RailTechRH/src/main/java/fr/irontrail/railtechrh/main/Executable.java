package fr.irontrail.railtechrh.main;

public class Executable {
    public static void main(String[] args) {
        System.out.println("🚀 Lancement de RailTechRH...");
        try {
            App.main(args);
        } catch (Exception e) {
            System.err.println("❌ Erreur au lancement de l'application :");
            e.printStackTrace();
        }
    }
}
