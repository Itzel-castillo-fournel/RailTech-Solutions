package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.IncidentModel;
import fr.irontrail.railtechrh.model.TrainModel;
import fr.irontrail.railtechrh.model.enums.Gravite;
import fr.irontrail.railtechrh.model.enums.TypeIncident;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentDAO {
    private static final String INSERT_INCIDENT = "INSERT INTO Incident (description, typeIncident, gravite, trainImmat VALUES (?,?,?,?)";
    private static final String GET_INCIDENT = "SELECT id, description, typeIncident, gravite, trainImmat FROM Incident WHERE id = ?";
    private static final String UPDATE_INCIDENT = "UPDATE Incident SET descritpion = ?, typeIncident = ?, gravite = ?, trainImmat = ? WHERE id = ?";
    private static final String DELETE_INCIDENT = "DELETE FROM Incident WHERE id = ?";
    private static final String GET_TRAIN = "SELECT immatriculation FROM Train";

    public IncidentModel createIncident(String description, TypeIncident typeIncident, Gravite gravite, TrainModel trainImmat) throws SQLException {
        String [] generatedColumns = {"id"};

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_INCIDENT);
            statement.setString(1, description);
            statement.setString(2, typeIncident.toString());
            statement.setString(3, gravite.toString());
            statement.setString(4, trainImmat.getImmatriculation());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        System.out.println("Incident created successfully");
                        return new IncidentModel(id, description, typeIncident, gravite, trainImmat);

                    }
                }
            }
        }catch (SQLException e) {
            System.out.println("Error inserting incident : " + e.getMessage());
        }
        return null;
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
        }
        return train;
    }
}
