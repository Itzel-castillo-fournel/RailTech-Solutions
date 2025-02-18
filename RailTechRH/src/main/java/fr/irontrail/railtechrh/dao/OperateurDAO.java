package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OperateurDAO {

    public List<TrajetModel> getTrajetsProgrammes() throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet WHERE heureDepart >= CURDATE()";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                TrajetModel trajet = new TrajetModel();
                trajet.setId(resultSet.getInt("id"));
                trajet.setHeureDepart(resultSet.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setHeureArrivee(resultSet.getTimestamp("heureArrivee").toLocalDateTime());
                trajet.setArretDepart(Arret.valueOf(resultSet.getString("arretDepart")));
                trajet.setArretArrivee(Arret.valueOf(resultSet.getString("arretArrivee")));
                trajet.setTrainImmat(resultSet.getString("trainImmat"));
                trajet.setConducteurId(resultSet.getInt("conducteurId"));
                trajets.add(trajet);
            }
        }
        return trajets;
    }
}
