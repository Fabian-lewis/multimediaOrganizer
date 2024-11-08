import java.sql.Statement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:multimedia.db";

    public static void createDatabase(){
        String createFileTable = """
                CREATE TABLE IF NOT EXISTS files(
                fileId INTEGER PRIMARY KEY AUTOINCREMENT,
                fileName TEXT NOT NULL,
                type TEXT NOT NULL,
                path TEXT NOT NULL);
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {

                stmt.execute(createFileTable);
                System.out.println("Table Created Successfully");
            
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }

    public static void insertFile(String name, String type, String path){
        String insertfileString = "INSERT INTO files(name,type,path) VALUES (?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(insertfileString)){
                pstmt.setString(1, name);
                pstmt.setString(2, type);
                pstmt.setString(3, path);
                pstmt.executeUpdate();

                System.out.println("File added to database: "+name);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
