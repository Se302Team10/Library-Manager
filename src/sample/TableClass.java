package sample;

import java.util.ArrayList;

public class TableClass {

    /** This class implemented to handle with dynamic data
     * GUI and Database communicate each other effectively by using this class object  **/

    private String catalogName;
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList<String> columnDataTypes = new ArrayList<>();
    private ArrayList<String> userInputs = new ArrayList<>();

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnName) {
        this.columnNames.add(columnName);
    }

    public ArrayList<String> getColumnDataTypes() {
        return columnDataTypes;
    }

    public void setColumnDataTypes(String columnDataType) {
        this.columnDataTypes.add(columnDataType);
    }

    public ArrayList<String> getUserInputs() {
        return userInputs;
    }

    public void setUserInputs(String userInput) {
        this.userInputs.add(userInput);
    }
}


