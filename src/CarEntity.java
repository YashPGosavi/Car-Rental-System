import java.sql.*;
import java.util.Scanner;

public class CarEntity implements DatabaseConnection {
    
    public void addCar() {
        try (Connection connection = DatabaseConnection.getConnection();

            Scanner sc = new Scanner(System.in)) {

            System.out.println("Enter Car Model: ");
            String Car_Model = sc.nextLine();

            System.out.println("Enter Car Brand: ");
            String Car_Brand = sc.nextLine();

            System.out.println("Enter Base Price: ");
            int Base_Price = sc.nextInt();

            System.out.println("Is it available for rent? true/false");
            boolean isAvailable = sc.nextBoolean();

            String query = "INSERT INTO car (Car_Model, Car_Brand, Base_Price, isAvailable) VALUES (?,?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, Car_Model);
                preparedStatement.setString(2, Car_Brand);
                preparedStatement.setInt(3, Base_Price);
                preparedStatement.setBoolean(4, isAvailable);
                preparedStatement.executeUpdate();
                System.out.println("Car added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding car: " + e.getMessage());
        }
    }

    public void deleteCar() {
        availableCar();
        try (Connection connection = DatabaseConnection.getConnection();
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Enter ID of the Car to delete: ");
            int ID = sc.nextInt();

            String query = "DELETE FROM car WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, ID);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Car deleted successfully!");
                } else {
                    System.out.println("Car with ID " + ID + " not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting car: " + e.getMessage());
        }
    }

    public void availableCar() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT ID, Car_Model, Car_Brand, Base_Price FROM Car WHERE isAvailable = true")) {

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("ID");
                String Car_Model = rs.getString("Car_Model");
                String Car_Brand = rs.getString("Car_Brand");
                int Base_Price = rs.getInt("Base_Price");
                System.out.println(ID + " " + Car_Model + " " + Car_Brand + " " + Base_Price);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching available cars: " + e.getMessage());
        }
    }

    public void allCar() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Car")) {

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("ID");
                String Car_Model = rs.getString("Car_Model");
                String Car_Brand = rs.getString("Car_Brand");
                int Base_Price = rs.getInt("Base_Price");
                boolean isAvailable = rs.getBoolean("isAvailable");
                System.out.println(ID + " " + Car_Model + " " + Car_Brand + " " + Base_Price + " " + isAvailable);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all cars: " + e.getMessage());
        }
    }

    public void updatePrice(int ID) {
        try (Connection connection = DatabaseConnection.getConnection();
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Enter new Base Price: ");
            int Base_Price = sc.nextInt();

            String query = "UPDATE Car SET Base_Price = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Base_Price);
                preparedStatement.setInt(2, ID);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Car price updated successfully!");
                } else {
                    System.out.println("Car with ID " + ID + " not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating car price: " + e.getMessage());
        }
    }
}