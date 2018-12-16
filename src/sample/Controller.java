package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.util.Locale;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane mainScreen;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button btnAddInput;

    @FXML
    private Button btnEditInput;

    @FXML
    private Button btnDeleteInput;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private MenuItem menuItemManual;

    @FXML
    private Pane btnMenuAdd;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button btnCatalogAdd;

    @FXML
    private Button btnCatalogEdit;

    @FXML
    private Button btnCatalogDelete;

    public String selectedListViewItem;
    public Object selectedTableViewItem;

    //listTempAttributes will be using to show added attributes in createCatalog/editCatalog dialogPane's listview
    public ObservableList<String> listTempAttributes = FXCollections.observableArrayList();

    public ObservableList<String> listCatalogNames = FXCollections.observableArrayList(); // anapenceredeki listview'bilgileri burdan çekiliyor



    @FXML
    public void initialize(){

        listView.setItems(listCatalogNames);
        listCatalogNames.add("SampleCatalog");
    listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            selectedListViewItem=newValue.toString();
        }
    });
    tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            // Your action here
            selectedTableViewItem=newValue;
        }
    });
}

@FXML
    public void editCatalog(){

    //if any item(catalogName) selected on Listview
    if(listView.getSelectionModel().getSelectedItem()!=null){

        /**
         * this method will call a method that return TableClass Object**/

        /** This line will be activated after the called method impelemented**/
        // TableClass returnedObject = returnHereTableClassObject(selectedListViewItem); ******** IMPORTANT *******

        TableClass returnedObject = new TableClass(); // this line will delete after that method implemented.

        /**  EXAMPLE TABLECLASS OBJECT **/

        returnedObject.setCatalogName("Books");

        returnedObject.setColumnNames("Author's Name");
        returnedObject.setColumnDataTypes("TEXT");
        returnedObject.setUserInputs("Victor Hugo");

        returnedObject.setColumnNames("Book's Name");
        returnedObject.setColumnDataTypes("TEXT");
        returnedObject.setUserInputs("Les Miserables");

        returnedObject.setColumnNames("Number of Page");
        returnedObject.setUserInputs("423");
        returnedObject.setColumnDataTypes("INTEGER");

        returnedObject.setColumnNames("Bought Date");
        returnedObject.setColumnDataTypes("DATE");

        /** that example will be deleted later **/


        Dialog<ButtonType> editDialogPane = new Dialog<>();
        editDialogPane.initOwner(mainScreen.getScene().getWindow()); // assaigning parrent screen of new DialogPane

        DialogPane editCatalogDialogPane = new DialogPane();
        editDialogPane.setTitle("Edit Catalog");
        Stage stage = (Stage) editDialogPane.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/edit.png").toString()));


        VBox vBoxMain = new VBox();
        VBox vBoxLeft  = new VBox();
        VBox vBoxRight = new VBox();
        HBox hBoxFirstRow = new HBox();
        HBox hBoxButton = new HBox();
        HBox hBoxLastRow = new HBox();

        Label labelCatalogName = new Label("Catalog Name");
        Label labelAttribute = new Label("Attribute");
        Label labelDataType = new Label("Data Type");

        TextField tfCatalogName = new TextField();

      /** existing catalogName written here**/
        tfCatalogName.setText(returnedObject.getCatalogName());

        TextField tfAttribute   = new TextField();
        ComboBox cbDataType = new ComboBox();
        ObservableList<String> dataTypes= FXCollections.observableArrayList();

        dataTypes.addAll("TEXT","INTEGER","DOUBLE","DATE");
        cbDataType.setItems(dataTypes);
        cbDataType.getSelectionModel().selectFirst();

        cbDataType.setPrefWidth(130);

        Button btaddAttribute=new Button("Add Attribute");
        Button btdeleteAttribute=new Button("Delete Attribute");
        Button btUpdateCatalog = new Button("Update My Catalog");

        ListView<String> lvCatalogInfo = new ListView<>();

        hBoxFirstRow.getChildren().addAll(vBoxLeft,vBoxRight);
        vBoxLeft.getChildren().addAll(labelCatalogName,labelAttribute,labelDataType);
        vBoxRight.getChildren().addAll(tfCatalogName,tfAttribute,cbDataType);
        hBoxButton.getChildren().addAll(btaddAttribute,btdeleteAttribute);
        hBoxLastRow.getChildren().add(btUpdateCatalog);

        vBoxMain.getChildren().addAll(hBoxFirstRow,hBoxButton,lvCatalogInfo,hBoxLastRow);
        vBoxMain.setSpacing(10);
        vBoxLeft.setSpacing(20);
        vBoxRight.setSpacing(10);
        hBoxFirstRow.setSpacing(10);
        vBoxMain.setPrefWidth(Region.USE_COMPUTED_SIZE);
        vBoxMain.setPrefHeight(Region.USE_COMPUTED_SIZE);
        lvCatalogInfo.setPrefHeight(200);
        lvCatalogInfo.setItems(listTempAttributes);

        for(int i = 0; i<returnedObject.getColumnNames().size();i++){

            listTempAttributes.add(returnedObject.getColumnNames().get(i).trim().toString().toUpperCase() + "," + returnedObject.getColumnDataTypes().get(i).trim().toString().toUpperCase());
        }

        hBoxButton.setSpacing(10);
        hBoxButton.nodeOrientationProperty().setValue(NodeOrientation.RIGHT_TO_LEFT);

        editCatalogDialogPane.setContent(vBoxMain); // dialogPane açılan sayfanın ana penceresidir(Layout). ve bu anaPencerenin içine tüm hboxlarımızı içine koyduğumuz Vboxımızı yerleştiriyoruz.

        editCatalogDialogPane.setPrefWidth(Region.USE_COMPUTED_SIZE); //dialog penceresinin genişliği içindeki nesnelerin kullanacağı kadar belirleniyor.
        editCatalogDialogPane.setPrefHeight(Region.USE_COMPUTED_SIZE); //dialog penceresinin yüksekliği içindeki nesnelerin kullanacağı kadar belirleniyor.

        editDialogPane.getDialogPane().setContent(editCatalogDialogPane); //dialogpane layoutumuzun içeriği sayfanın dış çerçevesiyle burada birleştiriliyor gibi düşünebilirsiniz.


        btaddAttribute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!tfAttribute.getText().trim().isEmpty() && (!listTempAttributes.contains(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim()+","+cbDataType.getValue().toString()) || (!listTempAttributes.contains(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim()+","+cbDataType.getValue().toString())) )){
                    listTempAttributes.add(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim()+","+cbDataType.getValue().toString());
                    returnedObject.setColumnNames(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim());
                    returnedObject.setColumnDataTypes(cbDataType.getValue().toString());


                    tfAttribute.clear();
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Attribute field cannot be empty");
                    alert.setContentText("Careful with the next step!");

                    alert.showAndWait();

                   // Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    // stage.getIcons().add(new Image(this.getClass().getResource("/icons/warning.png").toString()));

                }
            }
        });  //addAttribute butonuna tıklanıldığında çalışacak kodlar

        btdeleteAttribute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lvCatalogInfo.getSelectionModel().getSelectedItem()!=null){
                    int getIndex = lvCatalogInfo.getSelectionModel().getSelectedIndex();
                    listTempAttributes.remove(lvCatalogInfo.getSelectionModel().getSelectedItem());
                    returnedObject.getColumnDataTypes().remove(getIndex);
                    returnedObject.getColumnNames().remove(getIndex);

                }
            }
        });  //deleteAttribute butonuna tıklanıldığında çalışacak kodlar


        btUpdateCatalog.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tfCatalogName.getText().trim().isEmpty() && listTempAttributes.size()!=0){
                    tfAttribute.clear();
                    tfCatalogName.clear();

                        /** CALL THE UPDATER SQL METHOD
                         *
                         * the called method must compare first TableClass Object list(columNames,columnDataTypes) and its paramater TableClass object
                         * and if those list isnt equal each other then do needed updates on database
                         *
                         * firstObject.catalogName =? parameterObject.catalogName;
                         * firstObject.columnNames.get(i) =? parameterObject.columnNames.get(i);
                         * firstObject.columnDataTypes.get(i) =? parameterObject.columnDataTypes.get(i);
                         *
                         *
                         * **/
                        //  callTheSQLUpdateMethod(returnedObject); **************** IMPORTANT ******************


                       editDialogPane.close();
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Fill All the Required Fields");
                    alert.setContentText("Careful with the next step! \n-Catalog name cannot be empty!\n-At least one attribute needed");

                    alert.showAndWait();

                    // Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    // stage.getIcons().add(new Image(this.getClass().getResource("/icons/warning.png").toString()));
                }
            }
        }); //Create Catalog Butonuna basıldığında çalışacak kodlar

        editDialogPane.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        //alt satırda, dialog gösterildikten sonra bir sonuç için bekle (showAndWait) komutulya kullanıcının hangi butona bastığını geri döndürüyoruz.
        Optional<ButtonType> result =editDialogPane.showAndWait();
        if (result.get() == ButtonType.CLOSE) {
            listTempAttributes.clear();
        }

        }else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Ooops, Wrong Attempt!");
        alert.setContentText("No Catalog Selected");

        //  Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
       // stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));


        alert.showAndWait();

    }
}
}

