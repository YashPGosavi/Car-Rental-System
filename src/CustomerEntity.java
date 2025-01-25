import java.sql.*;
import java.util.Scanner;

public class CustomerEntity implements DatabaseConnection {
    private CarEntity car;
    private String Customer_Name;
    private String Address;
    private int rentID;
    private int Duration_Days;
    private int Phone_Number;

    public CustomerEntity(CarEntity car) {
        this.car = car;
    }
    
    public void collectData() {
        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("Car ID to rent: ");
            rentID = sc.nextInt();

            System.out.println("Enter Customer Name: ");
            Customer_Name = sc.next();

            System.out.println("Enter Customer Phone Number: ");
            Phone_Number = sc.nextInt();

            System.out.println("Enter Customer Address: ");
            Address = sc.next();

            System.out.println("How many days will you rent the car:");
            Duration_Days = sc.nextInt();
        }
    }

    public void generateBill(int Car_ID) {
        String query = "SELECT * FROM Customer WHERE Car_ID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Car_ID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String Car_Model = rs.getString("Car_Model");
                    String Car_Brand = rs.getString("Car_Brand");
                    String Customer_Name = rs.getString("Customer_Name");
                    int Phone_Number = rs.getInt("Phone_Number");
                    String Address = rs.getString("Address");
                    int Duration_Days = rs.getInt("Duration_Days");
                    int Total_Price = rs.getInt("Total_Price");

                    System.out.println(
                            "Car_ID: " + Car_ID + '\n' +
                                    "Car_Model: " + Car_Model + '\n' +
                                    "Car_Brand: " + Car_Brand + '\n' +
                                    "Customer_Name: " + Customer_Name + '\n' +
                                    "Phone_Number: " + Phone_Number + '\n' +
                                    "Address: " + Address + '\n' +
                                    "Duration_Days: " + Duration_Days + '\n' +
                                    "Total_Price: " + Total_Price);
                } else {
                    System.out.println("No rental found for Car ID: " + Car_ID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error generating bill: " + e.getMessage());
        }
    }

    public void rentCar() {

        if (this.car == null) {
            System.out.println("Car entity is not initialized.");
            return;
        }

        this.car.availableCar();

        collectData();

        // Collecting data from car table
        String selectId = "SELECT * FROM car WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {

            String car_Model;
            String car_Brand;
            int base_Price;
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectId)) {
                preparedStatement.setInt(1, rentID);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        car_Model = rs.getString("Car_Model");
                        car_Brand = rs.getString("Car_Brand");
                        base_Price = rs.getInt("Base_Price");
                    } else {
                        System.out.println("Car with ID " + rentID + " not found.");
                        return;
                    }
                }
            }

            // Updating Car Table
            String updateisAvailable = "UPDATE Car SET isAvailable = false WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateisAvailable)) {
                preparedStatement.setInt(1, rentID);
                preparedStatement.executeUpdate();
            }

            // Updating Customer Table
            String customerUpdate = "INSERT INTO Customer (Car_ID, Car_Model, Car_Brand, Customer_Name, Phone_Number, Address, Duration_Days, Total_Price) VALUES (?,?,?,?,?,?,?,?)";
            int total_Price = base_Price * Duration_Days;

            try (PreparedStatement preparedStatement = connection.prepareStatement(customerUpdate)) {
                preparedStatement.setInt(1, rentID);
                preparedStatement.setString(2, car_Model);
                preparedStatement.setString(3, car_Brand);
                preparedStatement.setString(4, Customer_Name);
                preparedStatement.setInt(5, Phone_Number);
                preparedStatement.setString(6, Address);
                preparedStatement.setInt(7, Duration_Days);
                preparedStatement.setInt(8, total_Price);
                preparedStatement.executeUpdate();
            }

            generateBill(rentID);
        } catch (SQLException e) {
            System.out.println("Error renting car: " + e.getMessage());
        }
    }

    public void returnCar() {
        try (Connection connection = DatabaseConnection.getConnection();
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Enter ID of the Car to return: ");
            rentID = sc.nextInt();

            // Updating Car Table
            String updateisAvailable = "UPDATE Car SET isAvailable = true WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateisAvailable)) {
                preparedStatement.setInt(1, rentID);
                preparedStatement.executeUpdate();
            }

            // Updating Customer Table
            String deleteCar = "DELETE FROM Customer WHERE Car_ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCar)) {
                preparedStatement.setInt(1, rentID);
                preparedStatement.executeUpdate();
            }

            System.out.println("Car returned successfully!");
        } catch (SQLException e) {
            System.out.println("Error returning car: " + e.getMessage());
        }
    }
}