package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.sql.ResultSet;
public class databasemanagement {
    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() { //main connection to database originates from here
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/bulut/IdeaProjects/Library-Manager/src/db/librarymanager.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void InsertMetatable(String name) { //insert in to meta table is here
        String sql = "INSERT INTO metatable(name) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

// KEY'İN OTO INCREMENT DOĞASI GEREĞİ BÜTÜN rowların silinmesi oto increment'de hala daha en son verdiği numaradan devam ettiriyor.
    // UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='metatable' komutu ile bu durumu düzeltmek mümkün

    /**
     * Create a new table in the test database
     *
     */
    public  void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/bulut/IdeaProjects/Library-Manager/src/db/librarymanager.db";
        String userdefined1 = "this will be acuired from the user";
        String userdefined2 = "this will be acuired from the user";
        String userdefined3 = "this will be acuired from the user";//This will be in a proper loop structure after we include it to the front end

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS "+userdefined1+" (\n"
                + "	id integer PRIMARY KEY,\n" //this will always be here, and this ID will = id from the metatable
                + "	"+userdefined2+" varchar NOT NULL,\n"
                + "	"+userdefined3+" varchar NOT NULL,\n"
                + ");"; // will be in a loop

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement())
        {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
