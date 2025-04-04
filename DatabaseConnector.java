import java.sql.*;
import java.util.*;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/calculator_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456"; // Replace with your MySQL password

    // Save operation to database
    public void saveOperation(String expression, double result) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO operations (operation, result) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, expression + " = " + result);
            pstmt.setDouble(2, result);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load history from database
    public List<String> loadHistory() {
        List<String> history = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT operation FROM operations ORDER BY timestamp DESC LIMIT 5";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                history.add(rs.getString("operation"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    // Clear history from database
    public void clearHistory() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM operations";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}