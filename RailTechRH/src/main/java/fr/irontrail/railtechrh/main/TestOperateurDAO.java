package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.OperateurDAO;
import fr.irontrail.railtechrh.model.TrajetModel;

import java.sql.SQLException;
import java.util.List;

public class TestOperateurDAO {
    public static void main(String[] args) {
        OperateurDAO operateurDAO = new OperateurDAO();

        try {
            // Récupérer les trajets programmés
            List<TrajetModel> trajets = operateurDAO.getTrajetsProgrammes();
            if (!trajets.isEmpty()) {
                System.out.println("Trajets programmés trouvés : ");
                for (TrajetModel trajet : trajets) {
                    System.out.println(trajet);
                }
            } else {
                System.out.println("Aucun trajet programmé trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets : " + e.getMessage());
        }
    }
}
