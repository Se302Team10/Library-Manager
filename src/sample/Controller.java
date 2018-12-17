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
    public ObservableList selectedTableViewItem = FXCollections.observableArrayList();

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
            if (listView.getSelectionModel().getSelectedItem() != null) {
                selectedListViewItem = newValue.toString();
            }
        }
    });
    tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            // Your action here
            if(tableView.getSelectionModel().getSelectedItem()!=null) {
                selectedTableViewItem = (ObservableList) newValue;
            }
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
        // TableClass returnedObject = returnHereTableClassObject(selectedListViewItem.toString()); ******** IMPORTANT *******
        //it is going to use also in the editInput() method.
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
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Ooops, Wrong Attempt!");
                    alert.setContentText("Attribute cannot be empty!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
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
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Ooops, Wrong Attempt!");
                    alert.setContentText("Careful with the next step! \n-Catalog name cannot be empty!\n-At least one attribute needed!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
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
          Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
        alert.showAndWait();

    }
}

@FXML
    public void btnEditInput(){

        if(tableView.getSelectionModel().getSelectedItem()!=null){

            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(mainScreen.getScene().getWindow()); //Asssinging parrent of dialog pane

            DialogPane dialogPane=new DialogPane();
            dialog.setTitle("Update Record");
            VBox vBoxMainFrame = new VBox();

         /**
          * returnHereTableClassObject(selectedListViewItem);
          * the method  will return the same daha like btnEditCatalog
          *
          * according to return value btnEditInput method will create input textfields to let the user update old input.**/
       //  TableClass returnedObject=returnHereTableClassObject(selectedListViewItem.toString()); ///**** IMPORTANT it will be activated

            TableClass ReturnedObject= new TableClass();

            ReturnedObject.setColumnNames("Name");
            ReturnedObject.setColumnNames("Year");
            ReturnedObject.setColumnNames("Genree");

            TextField[] textFields = new TextField[ReturnedObject.getColumnNames().size()];
            for (int i = 0; i < ReturnedObject.getColumnNames().size();i++){

              //  Label label = new Label(sampleReturnedObject.getColumnNames().get(i).toString().toUpperCase());
                textFields[i]=new TextField();
                textFields[i].setPromptText(ReturnedObject.getColumnNames().get(i).toString().toUpperCase());
                HBox hBox =new HBox();
                hBox.getChildren().addAll(textFields[i]);
                vBoxMainFrame.getChildren().add(hBox);

            }

            Button buttonUpdate =new Button("Update My Input");
            vBoxMainFrame.getChildren().add(buttonUpdate);
            vBoxMainFrame.setSpacing(5);

            buttonUpdate.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                /** callTheUpdateRowSql();
                 * firstly, the method must find the exact same row from the database according to user selection on tablview
                 * (first paramater will provide those existing/old information)
                 *
                 *then
                 *
                 *
                 * it must execute update sql and set new inputs into database from a list which is in second paramater
                 * Assume sending list in as parameters as ObservableList;
                 *
                 * you can get items of observablelist like this
                 *
                 *ObservableList myList = FXCollections.observableArrayList();
                 *  for (int i = 0; i<myList.size();i++){
                 System.out.println(i+".item: " + myList.get(i));
                 }
                 * **/
                    ObservableList selectedItem = FXCollections.observableArrayList();
                    ObservableList newInputs = FXCollections.observableArrayList();

                    for (int i = 0; i<ReturnedObject.getColumnNames().size();i++){
                        //tableview will contains observableList objects therefore we can get selectedItem as an instance of ObservableList
                        selectedItem.add(tableView.getSelectionModel().getSelectedItem());
                        newInputs.add(textFields[i].getText().trim().toUpperCase().toString());

                    }

                  // callTheUpdateRowSql(selectedItem,newInputs);

                }
            });
            dialogPane.setContent(vBoxMainFrame);
            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            //alt satırda, dialog gösterildikten sonra bir sonuç için bekle (showAndWait) komutulya kullanıcının hangi butona bastığını geri döndürüyoruz.
            Optional<ButtonType> result =dialog.showAndWait();
            dialogPane.setPrefWidth(Region.USE_COMPUTED_SIZE); //dialog penceresinin genişliği içindeki nesnelerin kullanacağı kadar belirleniyor.
            dialogPane.setPrefHeight(Region.USE_COMPUTED_SIZE); //dialog penceresinin yüksekliği içindeki nesnelerin kullanacağı kadar belirleniyor.
            dialog.getDialogPane().setContent(dialogPane);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Ooops, Wrong Attempt!");
            alert.setContentText("No row selected from the table");
              Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
             stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }




}


}

