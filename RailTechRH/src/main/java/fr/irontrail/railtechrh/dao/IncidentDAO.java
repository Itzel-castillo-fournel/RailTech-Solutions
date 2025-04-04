package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.IncidentModel;
import fr.irontrail.railtechrh.model.TrainModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.*;



public class IncidentDAO {
    private static final String INSERT_INCIDENT = "INSERT INTO incident (description, typeIncident, gravite, trainImmat) VALUES (?,?,?,?)";
    private static final String GET_INCIDENT = "SELECT id, description, typeIncident, gravite, trainImmat FROM Incident WHERE id = ?";
    private static final String UPDATE_INCIDENT = "UPDATE Incident SET descritpion = ?, typeIncident = ?, gravite = ?, trainImmat = ? WHERE id = ?";
    private static final String DELETE_INCIDENT = "DELETE FROM Incident WHERE id = ?";
    private static final String GET_TRAIN = "SELECT immatriculation FROM train";
    private static final String GET_TRAIN_BY_IMMAT = "SELECT immatriculation, modele, marque FROM train WHERE immatriculation = ?";

    private static Connection connection = null;

    public static boolean addIncident(IncidentModel incident) throws SQLException {
        Boolean ajoutIncident = false;
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_INCIDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, incident.getDescription());
            statement.setString(2, incident.getTypeIncident().toString());
            statement.setString(3, incident.getGravite().toString());
            statement.setString(4, incident.getTrainImmat().getImmatriculation());

            int rowsInserted = statement.executeUpdate();
            ajoutIncident = rowsInserted > 0;

            if (ajoutIncident) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        incident.setId(id);
                        System.out.println("Incident created successfully. ID : " + id);
                    }
                }
            }

        }catch (SQLException e) {
            System.out.println("Error inserting incident : " + e.getMessage());
        } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection : " + e.getMessage());
        }
    }
        return ajoutIncident;
    }

    public ObservableList<String> getTrain() throws SQLException {
        ObservableList<String> train = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_TRAIN);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String trainImmat = resultSet.getString("immatriculation");
                train.add(trainImmat);
            }
        } catch (SQLException e) {
            System.out.println("Error getting train : " + e.getMessage());
        } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection : " + e.getMessage());
        }
    }
        return train;
    }

    public static TrainModel getTrainByImmat(String immat) throws SQLException {

        TrainModel train = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_TRAIN_BY_IMMAT);
            statement.setString(1, immat);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                train = new TrainModel();
                train.setImmatriculation(resultSet.getString("immatriculation"));
                train.setMarque(resultSet.getString("marque"));
                train.setModele(resultSet.getString("modele"));
            }

        } catch (SQLException e) {
            System.out.println("Error getting train : " + e.getMessage());

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection : " + e.getMessage());
            }
        }
        return train;
    }
}
