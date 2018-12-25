package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.lang.*;
import java.util.ArrayList;

import java.sql.ResultSet;
public class databasemanagement {


    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() { //main connection to database originates from here
        // SQLite connection string
        String url = "jdbc:sqlite:src/librarymanager.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public int InsertMetatable(String name) { //insert in to meta table is here
        String sql = "INSERT INTO metatable(name) VALUES(?)";


        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return pstmt.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       return -1;
    }

// KEY'İN OTO INCREMENT DOĞASI GEREĞİ BÜTÜN rowların silinmesi oto increment'de hala daha en son verdiği numaradan devam ettiriyor.
    // UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='metatable' komutu ile bu durumu düzeltmek mümkün

    /**
     * Create a new table in the test database
     *
     */
    public  void createNewTable(TableClass tableClass) {
        int key =InsertMetatable(tableClass.getCatalogName());
        String str = String.valueOf(key);
        StringBuilder dynamicString = new StringBuilder("CREATE TABLE '"+str+"' ("   +"id integer PRIMARY KEY AUTOINCREMENT, ");

        int loopCtrl = tableClass.getColumnNames().size();
         for(int i = 0; i<loopCtrl ; i++ ) {
             final int index = i;
             dynamicString.append(tableClass.getSpesificColumnName(index) + " text");
             if(i!=loopCtrl-1) dynamicString.append(", ");
//              if (i != 0 && loopCtrl == i -1 ) { //spesifik column name yerine non spesifik olarak bakıp burda çevirmeye çalışacağız
//                  dynamicString.append(" "+tableClass.getSpesificColumnName(index).toString().substring(1,tableClass.getSpesificColumnName(index).toString().length() - 1)+"  text");
//              } else {
//                  dynamicString.append(" " + tableClass.getSpesificColumnName(index).toString().substring(1, tableClass.getSpesificColumnName(index).toString().length() - 1) + "  text,");
//              }
        }
         dynamicString.append(");");


        System.out.println(dynamicString.toString());

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement())
        {
            // create a new table
            stmt.execute(dynamicString.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteRow(){
        int key = 0;
        String str = String.valueOf(key);
        String sql = "DELETE FROM metatable WHERE id ='"+str+"' " ;
        try (Connection conn = this.connect();)
        {
            PreparedStatement st = conn.prepareStatement(sql);

            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


    public void editRow()
    {    int key = 0;
        String sql = "update metatable set name = ? where id = 1  ";


        try (Connection conn = this.connect();)
        {

            PreparedStatement preparedStmt = conn.prepareStatement(sql);

            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

 public ArrayList<String> metaTableSelect () {
        TableClass metaQuery = new TableClass();
        String sql = "Select name From metatable";
        try ( Connection conn = this.connect();
              Statement  stmt = conn.createStatement();
              ResultSet  rs   = stmt.executeQuery(sql);  ) {
            while(rs.next()) {
                    metaQuery.setColumnNames(rs.getString("name"));
            }
            return metaQuery.getColumnNames();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
 }
    public String metaId (String clicked) {
        String sql = "select id from metatable where name = '"+clicked+"' ";
        String str ;
        try ( Connection conn = this.connect();
              Statement  stmt = conn.createStatement();
              ResultSet  rs   = stmt.executeQuery(sql);  ) {
            str = String.valueOf(rs.getInt("id"));
            return str;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public TableClass TableFiller (String metaID) {
        TableClass metaQuery = new TableClass();
        String sql = "Select * From '"+metaID+"' ";
        try ( Connection conn = this.connect();
              Statement  stmt = conn.createStatement();
              ResultSet  rs   = stmt.executeQuery(sql);  ) {
            while(rs.next()) {
                metaQuery.setColumnNames(rs.getString("name"));
            }
            return metaQuery;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


 }
