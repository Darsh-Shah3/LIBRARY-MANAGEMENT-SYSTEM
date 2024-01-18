import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Librarian {
    static Scanner sc = new Scanner(System.in);
    static Statement st;
    static Connection connection;

    // Librarian Methods
    public static void librarian() throws Exception {
        try {
            while (true) {
                System.out.println("Welcome LIBRARIAN ");
                System.out.println("Which function you want to perform?? ");
                System.out.println(
                        "1. Add user\n2. Add book\n3. View users\n4. View books\n5. View Issued books\n6. View returned books\n7. Update Book\n8. Delete Book\n9. Delete User\n0. Exit");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        addUser();
                        break;
                    case 2:
                        addBook();
                        break;
                    case 3:
                        viewUsers();
                        break;
                    case 4:
                        viewBooks();
                        break;
                    case 5:
                        viewIssuedBooks();
                        break;
                    case 6:
                        viewReturnedBooks();
                        break;
                    case 7:
                        updateBook();
                        break;
                    case 8:
                        deleteBook();
                        break;
                    case 9:
                        deleteUser();
                        break;
                    case 0:
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice!!");
                }
            }
        } catch (Exception e) {
            System.out.println("Error occured!!");
        }
    }

    // Check if the Table is Empty or not
    public static boolean isEmpty(String tableName) throws Exception {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);
            return count == 0;
        }
        return false;
    }

    public static boolean bookExists(int bookId) throws Exception {
        String sql = "SELECT * FROM books";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            if (rs.getInt("book_id") == bookId) {
                return true;
            }
        }
        return false;
    }

    // Check if User Exists or Not
    public static boolean userExists(int userId) throws Exception {
        String sql = "SELECT * FROM users";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            if (rs.getInt("user_id") == userId) {
                return true;
            }
        }
        return false;
    }

    // Adding User
    public static void addUser() throws Exception {
        sc.nextLine();
        System.out.println("Enter your name : ");
        String username = sc.nextLine();
        System.out.println("Enter your phone number : ");
        String phone = sc.nextLine();
        System.out.println("Enter your email : ");
        String email = sc.nextLine();

        if (phone.length() != 10) {
            System.out.println("Phone number is not of 10 digit.");
            return;
        }
        if (username.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        if (phone.trim().isEmpty()) {
            System.out.println("Phone Number cannot be empty.");
            return;
        }

        if (email.trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }

        String sql = "INSERT INTO users (user_name, phone_no, email) VALUES (?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, username);
        pst.setString(2, phone);
        pst.setString(3, email);
        int rc = pst.executeUpdate();

        if (rc > 0) {
            System.out.println("User added to the database");
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int uid = rs.getInt(1);
                System.out.println("Generated User ID : " + uid);
            }
        } else {
            System.out.println("Failed to add the user");
        }
    }

    // Adding Books
    public static void addBook() throws Exception {
        System.out.println("Enter Book ISBN: ");
        sc.nextLine();
        String book_isbn = sc.nextLine();
        System.out.println("Enter Book Name: ");
        String book_name = sc.nextLine();
        System.out.println("Enter Book Author: ");
        String book_author = sc.nextLine();
        System.out.println("Enter Book Price: ");
        double book_price = sc.nextDouble();

        String book_available = "Yes";

        String sql = "INSERT INTO books(book_ISBN,book_name,book_author,book_price,book_available) VALUES (?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, book_isbn);
        pst.setString(2, book_name);
        pst.setString(3, book_author);
        pst.setDouble(4, book_price);
        pst.setString(5, book_available);
        int rc = pst.executeUpdate();

        if (rc > 0) {
            System.out.println("Book added to database");
        } else {
            System.out.println("Failed to add the book");
        }
    }

    // Display All Users
    public static void viewUsers() throws Exception {
        if (isEmpty("users")) {
            System.out.println("The User table is empty.");
            return;
        }

        String sql = "SELECT * FROM users";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        System.out.println("User Table");
        System.out.println("----------------------------------------");

        while (rs.next()) {
            int uid = rs.getInt("user_id");
            String username = rs.getString("user_name");
            String phone = rs.getString("phone_no");
            String email = rs.getString("email");

            System.out.println("User ID   : " + uid);
            System.out.println("User Name : " + username);
            System.out.println("Phone     : " + phone);
            System.out.println("Email     : " + email);
            System.out.println("----------------------------------------");
        }
    }

    // Display All Books
    public static void viewBooks() throws Exception {
        if (isEmpty("books")) {
            System.out.println("The Book table is empty.");
            return;
        }

        String sql = "SELECT * FROM books";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        System.out.println("Book Table ");
        System.out.println("----------------------------------------");

        while (rs.next()) {
            int bid = rs.getInt("book_id");
            String book_isbn = rs.getString("book_ISBN");
            String book_name = rs.getString("book_name");
            String book_author = rs.getString("book_author");
            String book_price = rs.getString("book_price");
            String book_available = rs.getString("book_available");

            System.out.println("Book ID          : " + bid);
            System.out.println("ISBN             : " + book_isbn);
            System.out.println("Book Name        : " + book_name);
            System.out.println("Author           : " + book_author);
            System.out.println("Price            : " + book_price);
            System.out.println("Book Availbility : " + book_available);
            System.out.println("----------------------------------------");
        }
    }

    // Display Issued Book Table
    public static void viewIssuedBooks() throws Exception {
        if (isEmpty("issued_books")) {
            System.out.println("The Issued Book table is empty.");
            return;
        }

        String sql = "SELECT * FROM issued_books";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        System.out.println("Issue Book Table");
        System.out.println("----------------------------------------");

        while (rs.next()) {
            int iid = rs.getInt("issue_id");
            int uid = rs.getInt("user_id");
            int bid = rs.getInt("book_id");
            String issued_date = rs.getString("issued_date");
            int period = rs.getInt("period");
            String returned = rs.getString("returned");

            System.out.println("Issue ID      : " + iid);
            System.out.println("User ID       : " + uid);
            System.out.println("Book ID       : " + bid);
            System.out.println("Issued Date   : " + issued_date);
            System.out.println("Period        : " + period);
            System.out.println("Return Status : " + returned);
            System.out.println("----------------------------------------");
        }
    }

    // Display Returned Book Table
    public static void viewReturnedBooks() throws Exception {
        if (isEmpty("returned_books")) {
            System.out.println("The Returned Book table is empty.");
            return;
        }

        String sql = "SELECT * FROM returned_books";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        System.out.println("Return Book Table");
        System.out.println("----------------------------------------");

        while (rs.next()) {
            int rid = rs.getInt("return_id");
            int bid = rs.getInt("book_id");
            int uid = rs.getInt("user_id");
            String return_date = rs.getString("return_date");
            double fine = rs.getDouble("fine");

            System.out.println("Return ID   : " + rid);
            System.out.println("Book ID     : " + bid);
            System.out.println("User ID     : " + uid);
            System.out.println("Return Date : " + return_date);
            System.out.println("Fine        : " + fine);
            System.out.println("----------------------------------------");
        }
    }

    // Updating Book
    public static void updateBook() throws Exception {
        System.out.println("Enter Book ID : ");
        int bid = sc.nextInt();
        if (!bookExists(bid)) {
            System.out.println("Book with ID " + bid + " does not exist.");
            return;
        }

        String sql = "UPDATE books SET book_ISBN=?, book_name=?, book_author=?, book_price=?, book_available=? WHERE book_id=?";
        PreparedStatement pst = connection.prepareStatement(sql);

        System.out.println("Enter Book's new ISBN : ");
        sc.nextLine();
        String isbn = sc.nextLine();
        System.out.println("Enter Book's new Name : ");
        String name = sc.nextLine();
        System.out.println("Enter Book's new Author : ");
        String author = sc.nextLine();
        System.out.println("Enter Book's new Price : ");
        double price = sc.nextDouble();
        System.out.println("Enter Book's Availability Status : ");
        sc.nextLine();
        String status = sc.nextLine();

        pst.setString(1, isbn);
        pst.setString(2, name);
        pst.setString(3, author);
        pst.setDouble(4, price);
        pst.setString(5, status);
        pst.setInt(6, bid);

        int rs = pst.executeUpdate();
        if (rs > 0) {
            System.out.println("Book data updated successfully.");
        } else {
            System.out.println("Failed to update Book data.");
        }
    }

    // Deleting Book
    public static void deleteBook() throws Exception {
        System.out.println("Enter Book ID you want to delete : ");
        int bid = sc.nextInt();
        if (!bookExists(bid)) {
            System.out.println("Book with ID " + bid + " does not exists.");
            return;
        }

        String sql = "DELETE FROM books WHERE book_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, bid);
        int rs = pst.executeUpdate();

        if (rs > 0) {
            System.out.println("Book data deleted successfully.");
        } else {
            System.out.println("Failed to delete Book data.");
        }
    }

    // Deleting User
    public static void deleteUser() throws Exception {
        System.out.println("Enter User ID you want to delete : ");
        int userId = sc.nextInt();
        if (!userExists(userId)) {
            System.out.println("User with ID " + userId + " does not exist.");
            return;
        }

        String sql = "DELETE FROM users WHERE user_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        int rs = pst.executeUpdate();

        if (rs > 0) {
            System.out.println("User data deleted successfully.");
        } else {
            System.out.println("Failed to delete User data.");
        }
    }
}
