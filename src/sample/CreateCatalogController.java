package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Locale;

public class CreateCatalogController {
    @FXML
    private DialogPane dialogPaneCreateCatalog;

    @FXML
    private TextField tfCatalogName;

    @FXML
    private TextField tfAttribute;

    @FXML
    private ComboBox<String> cbDataType;

    @FXML
    private Button btnAddAttribute;

    @FXML
    private Button btnDeleteAttribute;

    @FXML
    private ListView<String> lvAttributeList;

    @FXML
    private Button btnCreateMyCatalog;

    ObservableList<String> dataTypesList= FXCollections.observableArrayList();
    ObservableList<String> tempAttributeList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){

        Scene scene = new Scene(dialogPaneCreateCatalog);


        TableClass tableClass = new TableClass();
        dataTypesList.addAll("TEXT","INTEGER","DOUBLE","DATE");
        cbDataType.setItems(dataTypesList);
        cbDataType.getSelectionModel().selectFirst();
        lvAttributeList.setItems(tempAttributeList);

        btnAddAttribute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!tfAttribute.getText().trim().isEmpty() && (!tempAttributeList.contains(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim()+","+cbDataType.getValue().toString()) || (!tempAttributeList.contains(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim()+","+cbDataType.getValue().toString())) )){

                    tableClass.setColumnNames(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim());
                    tableClass.setColumnDataTypes(cbDataType.getValue().toString());
                    tempAttributeList.add(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim()+","+cbDataType.getValue().toString());
                    tfAttribute.clear();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Ooops, Wrong Attempt!");
                    alert.setContentText("Make sure you filled attribute name correctly!\nAlso you cannot enter existing attribute.");
                    Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                }
            }
        });


        btnDeleteAttribute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lvAttributeList.getSelectionModel().getSelectedItem()!=null){
                    int getIndex = lvAttributeList.getSelectionModel().getSelectedIndex();
                    tempAttributeList.remove(lvAttributeList.getSelectionModel().getSelectedItem());
                    tableClass.getColumnDataTypes().remove(getIndex);
                    tableClass.getColumnNames().remove(getIndex);
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Ooops, Wrong Attempt!");
                    alert.setContentText("No attribute selected!\n");
                    Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                }
            }
        });

        btnCreateMyCatalog.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tfCatalogName.getText().trim().isEmpty() && tempAttributeList.size()!=0) {
                    tableClass.setCatalogName(tfCatalogName.getText().trim().toUpperCase(Locale.ENGLISH).toString());
                    System.out.println("CATALOG İSMİ" + tableClass.getCatalogName().toString());
                    Controller controller = new Controller();
                    controller.listCatalogNames.add(tfCatalogName.getText().trim().toUpperCase(Locale.ENGLISH).toString());
                    System.out.println("basıldı");

                    //theMethodWillCreateCatalogTable(tableClass);
                    //table class objesi tablonun Katalog adı (tableClass.getCatalogName
                    //kolon isimleri ve veri tiplerini içeriyor tableClass.getColumnNames , tableClass.getDataTypes

                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Ooops, Wrong Attempt!");
                    alert.setContentText("Careful with the next step! \n-Catalog name cannot be empty!\n-At least one attribute needed!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                }
            }
        });


    }




}


