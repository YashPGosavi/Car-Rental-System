import java.util.Objects;
import java.util.Scanner;

public class Main {
    private final CarEntity car;
    private final CustomerEntity customer;
    private final Scanner sc;

    private String password = "Pass123";

    public Main() {
        this.car = new CarEntity();
        this.customer = new CustomerEntity(car);
        this.sc = new Scanner(System.in);
        showMainMenu();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void showMainMenu() {
        int choice;
        do {
            System.out.println("1. Admin \n2. Rent a Car \n3. Exit");
            System.out.print("Enter Your Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    handleAdmin();
                    break;
                case 2:
                    handleRent();
                    break;
                case 3:
                    System.out.println("Thank you for visiting Car Rental Service!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    private void handleAdmin() {
        System.out.print("Enter Password: ");
        String passKey = sc.nextLine();
        if (Objects.equals(passKey, this.password)) {
            showAdminMenu();
        } else {
            System.out.println("Incorrect Password.");
        }
    }

    private void showAdminMenu() {
        int choice;
        do {
            System.out.println("1. Add New Car");
            System.out.println("2. Remove Car");
            System.out.println("3. View All Cars");
            System.out.println("4. View Available Cars");
            System.out.println("5. Update Car Price");
            System.out.println("6. Return to Main Menu");
            System.out.print("Enter Your Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    car.addCar();
                    break;
                case 2:
                    car.deleteCar();
                    break;
                case 3:
                    car.allCar();
                    break;
                case 4:
                    car.availableCar();
                    break;
                case 5:
                    System.out.print("Enter Car ID to update the price: ");
                    if (sc.hasNextInt()) {
                        int id = sc.nextInt();
                        sc.nextLine(); // Clear the buffer after reading the int
                        car.updatePrice(id);
                        System.out.println("Price Updated Successfully...");
                    } else {
                        sc.nextLine(); // Clear invalid input
                        System.out.println("Invalid ID. Please enter a valid number.");
                    }
                    break;
                case 6:
                    // Returning to main menu
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private void handleRent() {
        int choice;
        do {
            System.out.println("1. Rent New Car");
            System.out.println("2. Return Car");
            System.out.println("3. View Bill");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter Your Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    customer.rentCar();
                    break;
                case 2:
                    customer.returnCar();
                    break;
                case 3:
                    System.out.print("Enter Car ID: ");
                        int id = sc.nextInt();
                        customer.generateBill(id);
                    break;
                case 4:
                    // Returning to main menu
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    public static void main(String[] args) {
        new Main();
    }
}
