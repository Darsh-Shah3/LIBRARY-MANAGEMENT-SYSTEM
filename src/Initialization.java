import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class Initialization {
    static Scanner sc = new Scanner(System.in);
    static Statement st;
    static Connection connection;

    public static void creationOfTable() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
        System.out.println((connection != null) ? "Connection successful" : "Connection failed");

        String booksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT(20) PRIMARY KEY AUTO_INCREMENT," +
                "book_ISBN VARCHAR(100)," +
                "book_name VARCHAR(100)," +
                "book_author VARCHAR(100)," +
                "book_price DOUBLE," +
                "book_available VARCHAR(100)" +
                ")";

        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT(20) PRIMARY KEY AUTO_INCREMENT," +
                "user_name VARCHAR(100)," +
                "phone_no BIGINT," +
                "email VARCHAR(100)" +
                ")";

        String issuedBooksTable = "CREATE TABLE IF NOT EXISTS issued_books (" +
                "issue_id INT(20) PRIMARY KEY AUTO_INCREMENT," +
                "user_id INT(20)," +
                "book_id INT(20)," +
                "issued_date DATE," +
                "period INT(10)," +
                "returned varchar(100)," +
                "FOREIGN KEY (user_id) REFERENCES users (user_id)," +
                "FOREIGN KEY (book_id) REFERENCES books (book_id)" +
                ")";

        String returnedBooksTable = "CREATE TABLE IF NOT EXISTS returned_books (" +
                "return_id INT(20) PRIMARY KEY AUTO_INCREMENT," +
                "book_id INT(20)," +
                "user_id INT(20)," +
                "return_date DATE," +
                "fine DOUBLE DEFAULT '0'," +
                "FOREIGN KEY (book_id) REFERENCES books (book_id)," +
                "FOREIGN KEY (user_id) REFERENCES users (user_id)" +
                ")";

        st = connection.createStatement();
        st.executeUpdate(booksTable);
        st.executeUpdate(usersTable);
        st.executeUpdate(issuedBooksTable);
        st.executeUpdate(returnedBooksTable);

        String insertIntoBooksTable = "INSERT INTO books (book_id,book_isbn, book_name, book_author, book_price, book_available) VALUES\r\n"
                + //
                "(1001,'ISBN1001', 'The Great Gatsby', 'F. Scott Fitzgerald', 250.00, 'Yes'),\r\n" + //
                "(1002,'ISBN1002','To Kill a Mockingbird','Harper Lee',199.00,'Yes'),\r\n" + //
                "(1003,'ISBN1003','1984','George Orwell',299.00,'Yes'),\r\n" + //
                "(1004,'ISBN1004','Pride and Prejudice','Harper Lee',199.00,'Yes'),\r\n" + //
                "(1005,'ISBN1005','The Catcher in the Rye','J.D. Salinger',399.00,'Yes'),\r\n" + //
                "(1006,'ISBN1006','The Hobbit','J.R.R. Tolkien',99.00,'Yes'),\r\n" + //
                "(1007,'ISBN1007','The Fault in Our Stars','John Green',350.00,'Yes'),\r\n" + //
                "(1008,'ISBN1008','The Lord of the Rings','J.R.R. Tolkien',150.00,'Yes'),\r\n" + //
                "(1009,'ISBN1009','Brave New World','Aldous Huxley',199.00,'Yes'),\r\n" + //
                "(1010,'ISBN1010','The Hunger Games','Suzanne Collins',149.00,'Yes'),\r\n" + //
                "(1011,'ISBN1011','The Da Vinci Code','Dan Brown',350.00,'Yes'),\r\n" + //
                "(1012,'ISBN1012','The Alchemist','Paulo Coelho',250.00,'Yes'),\r\n" + //
                "(1013,'ISBN1013','The Chronicles of Narnia','C.S. Lewis',400.00,'Yes'),\r\n" + //
                "(1014,'ISBN1014','Gone with the Wind','Margaret Mitchell',199.00,'Yes'),\r\n" + //
                "(1015,'ISBN1015', 'The Shining', 'Stephen King',299.00,'Yes');";

        String insertIntoUsersTable = "INSERT INTO users (user_id, user_name, phone_no, email) VALUES\r\n" + //
                "(1, 'John Doe', '7600852311', 'john@gmail.com'),\r\n" + //
                "(2, 'Jane Smith', '9898002896', 'jane@gmail.com'),\r\n" + //
                "(3, 'Alice Johnson', '9601262896', 'alice@gmail.com'),\r\n" + //
                "(4, 'Bob Brown', '9974806506', 'bob@gmail.com'),\r\n" + //
                "(5, 'Ella Davis', '9510219506', 'ella@gmail.com'),\r\n" + //
                "(6, 'Mike Wilson', '8511082001', 'mike@gmail.com'),\r\n" + //
                "(7, 'Sarah Lee', '9099368070', 'sarah@gmail.com'),\r\n" + //
                "(8, 'David Clark', '8866471171', 'david@gmail.com'),\r\n" + //
                "(9, 'Linda Hall', '9601804353', 'linda@gmail.com'),\r\n" + //
                "(10, 'Kevin Anderson', '9016808774', 'kevin@gmail.com'),\r\n" + //
                "(11, 'David Wilson', '9974534670', 'david@gmail.com'),\r\n" + //
                "(12, 'Robert Williams', '9427958305', 'robert@gmail.com'),\r\n" + //
                "(13, 'Emily Davis', '9428734893', 'emily@gmail.com'),\r\n" + //
                "(14, 'Jane Smith', '8511112792', 'jane@gmail.com'),\r\n" + //
                "(15, 'Tom White', '9173000007', 'tom@gmail.com');";

        st.executeUpdate(insertIntoBooksTable);
        st.executeUpdate(insertIntoUsersTable);
    }
}
