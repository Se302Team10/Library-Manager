package sample;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;

import java.util.List;

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
        StringBuilder dynamicString = new StringBuilder("CREATE TABLE '"+str.toString()+"' ("   +"id integer PRIMARY KEY AUTOINCREMENT, ");

        int loopCtrl = tableClass.getColumnNames().size();
         for(int i = 0; i<loopCtrl ; i++ ) {
             final int index = i;
             dynamicString.append(tableClass.getSpesificColumnName(index) + " text");
             if(i!=loopCtrl-1) dynamicString.append(", ");

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

    public int metaIdInt (String clicked) {
        String sql = "select id from metatable where name = '"+clicked+"' ";

        try ( Connection conn = this.connect();
              Statement  stmt = conn.createStatement();
              ResultSet  rs   = stmt.executeQuery(sql);  ) {

            return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public TableClass getAllColumns(int metaID){

        String sql = "Select * From '"+metaID+"' ";
        try ( Connection conn = this.connect();
              Statement  stmt = conn.createStatement();
              ResultSet  rs   = stmt.executeQuery(sql);  ) {
            TableClass object = new TableClass();


            for (int i = 2; i < rs.getMetaData().getColumnCount()+1; i++) {
                //Iterate Column
                object.setColumnNames(rs.getMetaData().getColumnName(i));
            }

          return object;

        }catch (SQLException e){


        return null;
        }


    }

    public void   TableFiller (String metaID,TableView table,ObservableList<ObservableList> tableList ) {

        table.getColumns().clear();
        tableList.clear();
        String sql = "Select * From '"+metaID+"' ";
        try ( Connection conn = this.connect();
              Statement  stmt = conn.createStatement();
              ResultSet  rs   = stmt.executeQuery(sql);  ) {


            for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table

                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                table.getColumns().addAll(col);
            //    System.out.println("Column [" + i + "] ");
            }
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
             //   System.out.println("Row [1] added " + row);
                tableList.add(row);

            }

            table.setItems(tableList);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error on Building Data");
        }

    }

    public void deleteCatalogfromMeta (int id ) {

          try {
            Statement statement = this.connect().createStatement();
            statement.execute("DELETE FROM metatable  WHERE id=" +id );


        } catch (SQLException e) {
            e.printStackTrace();

        }



    }

    public void dropCatalog (int id) {
        String catalogName = String.valueOf(id);
        System.out.println("silinmek istenen CATALOG DB ID>>>"+catalogName );
        String sqlDrop = " Drop TABLE '"+catalogName+"' ";
        try ( Connection conn = this.connect();
              Statement  stmt = conn.createStatement();) {
            stmt.executeUpdate(sqlDrop);
        } catch (SQLException e) {
            System.out.println(e.getMessage());


        }
    }

    public  ArrayList<String> getColumns(String tableName, String schemaName) {

        ResultSet rs=null;

        ResultSetMetaData rsmd=null;
        PreparedStatement stmt=null;
        ArrayList<String> columnNames =null;
        String qualifiedName = (schemaName!=null&&!schemaName.isEmpty())?(schemaName+"."+tableName):tableName;
        try{
            Connection conn = this.connect();
            stmt=conn.prepareStatement("select * from "+qualifiedName+" where 0=1");
            rs=stmt.executeQuery();//you'll get an empty ResultSet but you'll still get the metadata
            rsmd=rs.getMetaData();
            columnNames = new ArrayList<String>();

            System.out.println("column count = >> "+ rsmd.getColumnCount());
            for(int i=1;i<=rsmd.getColumnCount();i++)
                columnNames.add(rsmd.getColumnLabel(i));
        }catch(SQLException e){

        }
        finally{
            if(rs!=null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block

                }
            if(stmt!=null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block

                }



                for (int i = 0;i<columnNames.size();i++){

                    System.out.println("returned column==>>" + columnNames.get(i));
                }

        }
        return columnNames;
    }

    public StringBuilder sbInsert(String catalogID, String columnName, String value){

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append("'"+catalogID.toString()+"'");
        sb.append(" ( ");
        sb.append(columnName);
        sb.append(" ) VALUES ('");
        sb.append(value);
        sb.append("' ) ");
        return  sb;
    }

    public void insertIntoSelectedCatalog(int tableID,TableClass tableClass){

        String stringTableID= String.valueOf(tableID);
        String sql = "Insert INTO '"+tableClass.getCatalogName()+"'";
        String sql2 = "( )";
        String sql3 = "VALUES ()";
        for(int i =0;i<tableClass.getColumnNames().size();i++){

        }


    }

    public void insertCatalog (List<String> columnNames) { /*User'ın girdiğin şeyleri 2.parametre olarak al abi buraya */

       //  List<Object> data = new ArrayList<>();
         //you will populate this list

         //getting the column names
          columnNames = getColumns("MyTable", "MyDB");

         String insertColumns = "";
         String insertValues = "";

         if(columnNames != null && columnNames.size() > 0){
             insertColumns += columnNames.get(0);
             insertValues += "?";
         }

         for(int i = 1; i < columnNames.size();i++){
             insertColumns += ", " + columnNames.get(i) ;
             insertValues += "?";
         }

         String insertSql = "INSERT INTO MyDB.MyTable (" + insertColumns + ") values(" + insertValues + ")";

         try{ Connection conn = this.connect();

             PreparedStatement  ps = conn.prepareStatement(insertSql);


             ps.execute(); //this inserts your data
         }catch(SQLException sqle){
             //do something with it
         }

     }

    public void deleteRow(int id, String tableName) { //ön taraftan iki parametre alacak, tablo ismi ve seçilen row'un id'si
        String str = String.valueOf(id);
        String sql = "DELETE From '"+tableName+"' Where id ='"+str+"' ";
       try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

           // set the corresponding param
           pstmt.setInt(1, id);
           // execute the delete statement
           pstmt.executeUpdate();

       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }

   }

 }