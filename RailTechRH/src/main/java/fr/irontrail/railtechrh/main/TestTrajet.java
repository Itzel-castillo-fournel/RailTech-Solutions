package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestTrajet {
    public static void main(String[] args) {
        TrajetDAO trajetDAO = new TrajetDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            int conducteurId = 3;
            LocalDate date = LocalDate.now();

            System.out.println("=== Test récupération des trajets ===");
            System.out.println("Conducteur ID: " + conducteurId);
            System.out.println("Date: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("----------------------------------------");
    }
}