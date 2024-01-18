import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class LibraryManagementSystem {
    static Scanner sc = new Scanner(System.in);
    static Statement st;
    static Connection connection;

    public static void main(String[] args) throws Exception {
        Initialization.creationOfTable();
        while (true) {
            System.out.println("WELCOME TO THE LIBRARY");
            System.out.println("From which side are you from ??\n1.Librarian\n2.User\n0.Exit");
            System.out.println("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    Librarian.librarian();
                    break;
                case 2:
                    User.user();
                    break;
                case 0:
                    System.out.println("VISIT AGAIN !!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!!");
            }
        }
    }
}