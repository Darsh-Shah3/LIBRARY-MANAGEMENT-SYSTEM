import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class User {
    static Scanner sc = new Scanner(System.in);
    static Statement st;
    static Connection connection;

     // User Methods
     public static void user() throws Exception {
        try {
            while (true) {
                System.out.println("Welcome USER ");
                System.out.println("Which function you want to perform?? ");
                System.out.println(
                        "1. Browse Books\n2. Search Books\n3. View Issued Books\n4. Issue Book\n5. Return Book\n6. Update User\n7. Generate Bill\n0. Exit");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        browseBooks();
                        break;
                    case 2:
                        searchBooks();
                        break;
                    case 3:
                        viewIssuedBooksByUser();
                        break;
                    case 4:
                        issueBook();
                        break;
                    case 5:
                        returnBook();
                        break;
                    case 6:
                        updateUser();
                        break;
                    case 7:
                        generateBill();
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

    // Check if the User has Issued The Book
    public static boolean hasUserIssuedBook(int userId) throws Exception {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE user_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);
            return count == 0;
        }
        return false;
    }

    // Check if book is available or not
    public static boolean isBookAvailable(int bookId) throws Exception {
        String sql = "SELECT book_available FROM books WHERE book_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, bookId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            boolean bookAvailable = rs.getBoolean("book_available");
            return bookAvailable;
        }

        // If the book with the specified ID does not exist, consider it unavailable.
        return false;
    }

    // Check that which book is issued to user
    public static boolean isBookIssuedToUser(int userId, int bookId) throws Exception {
        String sql = "SELECT * FROM issued_books WHERE user_id = ? AND book_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        pst.setInt(2, bookId);
        ResultSet rs = pst.executeQuery();

        return rs.next();
    }

    // Calculate Fine
    public static double calculateFine(String issued_date, String return_date, int iid) throws Exception {
        String sql = "SELECT period from issued_books WHERE issue_id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, iid);
        ResultSet rs = pst.executeQuery();
        int period = -1;
        if (rs.next()) {
            period = rs.getInt("period");
        }
        Date issuedDate = Date.valueOf(issued_date);
        Date returnDate = Date.valueOf(return_date);

        long daysDifference = calculateDateDifference(issuedDate, returnDate);

        double fine = 0.0;
        if (daysDifference < 0) {
            System.out.println("Return date is earlier than the issued date.");
            return -1;
        } else if (daysDifference <= period && daysDifference > 0) {
            System.out.println("You have returned the book on time within the period of " + period + " days.");
        } else {
            System.out.println("You have exceeded the allowed period by " + (daysDifference - period) + " days.");
            fine = (daysDifference - period) * 10;
            System.out.println("The fine for the late return is: " + fine + " rs");
        }

        return fine;
    }

    // Calculate Day Difference Between Issued date and Returned Date
    public static long calculateDateDifference(Date startDate, Date endDate) {
        long millisecondsDifference = endDate.getTime() - startDate.getTime();
        return millisecondsDifference / (24 * 60 * 60 * 1000); // Convert milliseconds to days
    }

    // Update the book availability to Yes in books Table
    public static void updateBookAvailability(int bookId, String available) throws Exception {
        String sql = "UPDATE books SET book_available = ? WHERE book_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, available);
        pst.setInt(2, bookId);
        pst.executeUpdate();
    }

    // Update the returned column to Yes in Issued_books Table
    public static void updateIssuedBook(int iid) throws Exception {
        String sql = "UPDATE issued_books SET returned = 'Yes' WHERE issue_id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, iid);
        int rc = pst.executeUpdate();
        if (rc > 0) {
            System.out.println("Issued book record updated.");
        } else {
            System.out.println("Failed to update the issued book record.");
        }
    }

    // Check if user has issued the book
    public static boolean hasUserIssuedBooks(int userId) throws Exception {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE user_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0;
        }
        return false;
    }

    // Browse Books Which Are Available
    public static void browseBooks() throws Exception {
        String sql = "SELECT * FROM books WHERE book_available = 'Yes'";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        System.out.println("Available Books");
        System.out.println("--------------------------------------------------");
        while (rs.next()) {
            int bid = rs.getInt("book_id");
            String isbn = rs.getString("book_ISBN");
            String book_name = rs.getString("book_name");
            String author = rs.getString("book_author");
            double price = rs.getDouble("book_price");
            String available = rs.getString("book_available");

            System.out.println("Book ID           : " + bid);
            System.out.println("ISBN              : " + isbn);
            System.out.println("Book Name         : " + book_name);
            System.out.println("Author            : " + author);
            System.out.println("Price             : " + price);
            System.out.println("Book Availability : " + available);
            System.out.println("--------------------------------------------------");
        }
    }

    // Searching Books
    public static void searchBooks() throws Exception {
        inner: while (true) {
            System.out.println("From what input do you want to serach the book ??");
            System.out.println("1. By Book Name\n2. By Book Author\n3. By Book ISBN\n0. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Enter the name of the Book : ");
                    sc.nextLine();
                    String book_name = sc.nextLine();
                    String sql = "SELECT * FROM books WHERE book_name=?";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, book_name);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        int bid = rs.getInt("book_id");
                        String isbn = rs.getString("book_ISBN");
                        String name = rs.getString("book_name");
                        String author = rs.getString("book_author");
                        double price = rs.getDouble("book_price");
                        String available = rs.getString("book_available");

                        System.out.println("Book ID           : " + bid);
                        System.out.println("ISBN              : " + isbn);
                        System.out.println("Book Name         : " + name);
                        System.out.println("Book Author       : " + author);
                        System.out.println("Book Price        : " + price);
                        System.out.println("Book Availability : " + available);
                        System.out.println("--------------------------------------------------");
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter the author name : ");
                    sc.nextLine();
                    String author = sc.nextLine();
                    String sql = "SELECT * FROM books WHERE book_author=?";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, author);
                    ResultSet rs = pst.executeQuery();
                    System.out.println("Books author by " + author + " is : ");
                    while (rs.next()) {
                        int bid = rs.getInt("book_id");
                        String isbn = rs.getString("book_ISBN");
                        String name = rs.getString("book_name");
                        double price = rs.getDouble("book_price");
                        String available = rs.getString("book_available");

                        System.out.println("Book ID           : " + bid);
                        System.out.println("ISBN              : " + isbn);
                        System.out.println("Book Name         : " + name);
                        System.out.println("Author            : " + author);
                        System.out.println("Book Price        : " + price);
                        System.out.println("Book Availability : " + available);
                        System.out.println("--------------------------------------------------");
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter the ISBN of the Book : ");
                    sc.nextLine();
                    String isbn = sc.nextLine();
                    String sql = "SELECT * FROM books WHERE book_ISBN=?";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, isbn);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        int bid = rs.getInt("book_id");
                        String name = rs.getString("book_name");
                        String author = rs.getString("book_author");
                        double price = rs.getDouble("book_price");
                        String available = rs.getString("book_available");

                        System.out.println("Book ID           : " + bid);
                        System.out.println("ISBN              : " + isbn);
                        System.out.println("Book Name         : " + name);
                        System.out.println("Author            : " + author);
                        System.out.println("Book Price        : " + price);
                        System.out.println("Book Availability : " + available);
                        System.out.println("--------------------------------------------------");
                    }
                    break;
                }
                case 0:
                    break inner;

                default:
                    System.out.println("Invalid Choice!!");
            }
        }
    }

    // Display Issued Books By Particular User
    public static void viewIssuedBooksByUser() throws Exception {
        System.out.println("Enter your User id : ");
        int userId = sc.nextInt();

        if (hasUserIssuedBook(userId)) {
            System.out.println("User with ID " + userId + " has not isuued any book.");
            return;
        }
        String sql = "SELECT * FROM issued_books WHERE user_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        System.out.println("Issue Book By User " + userId);
        System.out.println("----------------------------------------");

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int issue_id = rs.getInt("issue_id");
            int user_id = rs.getInt("user_id");
            int book_id = rs.getInt("book_id");
            String issued_date = rs.getString("issued_date");
            int period = rs.getInt("period");
            String returned = rs.getString("returned");

            System.out.println("Issue ID    : " + issue_id);
            System.out.println("User ID     : " + user_id);
            System.out.println("Book ID     : " + book_id);
            System.out.println("Issued Date : " + issued_date);
            System.out.println("Period      : " + period);
            System.out.println("Returned    : " + returned);
            System.out.println("----------------------------------------");
        }
    }

    // Issue Book
    public static void issueBook() throws Exception {
        System.out.println("Enter User ID: ");
        int userId = sc.nextInt();
        System.out.println("Enter Book ID: ");
        int bookId = sc.nextInt();
        sc.nextLine();
        if (!Librarian.userExists(userId)) {
            System.out.println("User with ID " + userId + " does not exist");
            return;
        }
        if (!Librarian.bookExists(bookId)) {
            System.out.println("Book with ID " + bookId + " does not exist");
            return;
        }
        if (!isBookAvailable(bookId)) {
            System.out.println("This book is not available.");
            return;
        }

        System.out.println("Enter Issue Date (YYYY-MM-DD): ");
        String issueDate = sc.nextLine();
        System.out.println("Enter Period (in days): ");
        int period = sc.nextInt();

        String sql = "INSERT INTO issued_books(user_id,book_id,issued_date,period,returned) VALUES (?,?,?,?,'No')";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        pst.setInt(2, bookId);
        pst.setString(3, issueDate);
        pst.setInt(4, period);

        int rc = pst.executeUpdate();

        if (rc > 0) {
            System.out.println("Book issued successfully");
        } else {
            System.out.println("Failed to issued the Book");
        }

        String sql1 = "UPDATE books SET book_available = 'No' WHERE book_id = ?";
        PreparedStatement pst1 = connection.prepareStatement(sql1);
        pst1.setInt(1, bookId);
        pst1.executeUpdate();
    }

    public static void returnBook() throws Exception {
        System.out.println("Enter User ID: ");
        int userId = sc.nextInt();

        if (!Librarian.userExists(userId)) {
            System.out.println("User with ID " + userId + " does not exist");
            return;
        }

        System.out.println("Enter Book ID: ");
        int bookId = sc.nextInt();

        if (!Librarian.bookExists(bookId)) {
            System.out.println("Book with ID " + bookId + " does not exist");
            return;
        }

        // Check if the book was actually issued to the user
        if (!isBookIssuedToUser(userId, bookId)) {
            System.out.println("This book was not issued to this user.");
            return;
        }

        String sql = "SELECT * FROM issued_books WHERE user_id = ? AND book_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        pst.setInt(2, bookId);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            int iid = rs.getInt("issue_id");
            String issued_date = rs.getString("issued_date");
            System.out.println("Enter the date of returning (YYYY-MM-DD): ");
            sc.nextLine();
            String return_date = sc.nextLine();
            double fine = calculateFine(issued_date, return_date, iid);

            // Always insert into return_books table, regardless of fine value
            String sql1 = "INSERT INTO returned_books(book_id,user_id,return_date,fine) VALUES(?,?,?,?)";
            PreparedStatement pst1 = connection.prepareStatement(sql1);
            pst1.setInt(1, bookId);
            pst1.setInt(2, userId);
            pst1.setString(3, return_date);
            pst1.setDouble(4, fine);
            pst1.executeUpdate();

            if (fine >= 0) {
                System.out.println("Book returned successfully.");
                updateBookAvailability(bookId, "Yes");
                updateIssuedBook(iid);
            }
        } else {
            System.out.println("This book was not issued to this user.");
        }
    }

    // Updating User
    public static void updateUser() throws Exception {
        System.out.println("Enter your id : ");
        int id = sc.nextInt();
        if (!Librarian.userExists(id)) {
            System.out.println("User with ID " + id + " does not exist.");
            return;
        }

        System.out.println("Enter new Name : ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.println("Enter new Phone Number : ");
        String phone = sc.nextLine();

        System.out.println("Enter new Email : ");
        String email = sc.nextLine();

        String sql = "UPDATE users SET user_name=?, phone_no=?, email=? WHERE user_id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, name);
        pst.setString(2, phone);
        pst.setString(3, email);
        pst.setInt(4, id);

        int rs = pst.executeUpdate();
        if (rs > 0) {
            System.out.println("User data updated successfully.");
        } else {
            System.out.println("Failed to update user data.");
        }
    }

    // Generating Bill
    public static void generateBill() throws Exception {
        System.out.println("Enter User ID : ");
        int userId = sc.nextInt();

        if (!Librarian.userExists(userId)) {
            System.out.println("User with ID " + userId + " does not exist");
            return;
        }

        if (!hasUserIssuedBooks(userId)) {
            System.out.println("No books issued by this user.");
            return;
        }

        // Query to retrieve issued books for the user
        String sql = "SELECT books.book_name, books.book_price, returned_books.fine " +
                "FROM books " +
                "INNER JOIN returned_books ON books.book_id = returned_books.book_id " +
                "WHERE returned_books.user_id = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String bookName = rs.getString("book_name");
            double bookPrice = rs.getDouble("book_price");
            double fine = rs.getDouble("fine");

            if (fine >= 0) {
                try (FileWriter fw = new FileWriter("User-" + userId + ".txt")) {
                    fw.write("Bill for User ID: " + userId + "\n");
                    fw.write("--------------------------------------------------\n");
                    fw.write("Book Name     :" + bookName + "\n");
                    fw.write("Book Price    :" + bookPrice + "\n");
                    fw.write("Fine          :" + fine + "\n");
                    double totalCost = bookPrice + fine;
                    fw.write("--------------------------------------------------\n");
                    fw.write("Total Cost: " + totalCost + "\n");
                    fw.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
