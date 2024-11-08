import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.core.DB;

import java.io.File;
import java.io.ObjectInputFilter.FilterInfo;
import java.sql.Connection;
//import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        String insertfileString = "INSERT INTO files(fileName,type,path) VALUES (?,?,?)";

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
    public static List<FileInfo> retrieve(String type){
        String retrieveSQL = "SELECT fileName, path from files where type = ?";
        List<FileInfo> files = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(retrieveSQL)){

                pstmt.setString(1, type);
                try(ResultSet resultSet = pstmt.executeQuery()){

                    while(resultSet.next()){
                        String fileName = resultSet.getString("fileName");
                        String filePath = resultSet.getString("path");
                        //String filePath = resultSet.getString("path");

                        //System.out.println("File name: "+fileName);
                        files.add(new FileInfo(fileName, filePath));
                    }
                }
                
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
                return files;
    }

}
