package sample;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.Optional;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;

import javafx.scene.control.TableView;


public class Controller {

    @FXML
    private BorderPane mainScreen;

    @FXML
    private Button btnAddInput;

    @FXML
    private Button btnEditInput;

    @FXML
    private MenuItem menuItemHelp;

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

    @FXML
    private TextField tfSearchCatalog;

    public String selectedListViewItem;
    public ObservableList selectedTableViewItem = FXCollections.observableArrayList();

    //listTempAttributes will be using to show added attributes in createCatalog/editCatalog dialogPane's listview
    public ObservableList<String> listTempAttributes = FXCollections.observableArrayList();


    public static ObservableList<String> listCatalogNames = FXCollections.observableArrayList(); // anapenceredeki listview'bilgileri burdan çekiliyor
    public TableView<ObservableList> tableView = new TableView<ObservableList>();

    //private TableView table;
    private ObservableList<ObservableList> at = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mainScreen.setCenter(tableView);
        if(listCatalogNames.size()!=0){
            listView.getSelectionModel().selectFirst();

        }


        databasemanagement dbcreate = new databasemanagement();
        listCatalogNames.setAll(dbcreate.metaTableSelect());


        FilteredList<String> filteredData = new FilteredList<>(listCatalogNames, s -> true);
        tfSearchCatalog.textProperty().addListener(obs->{
            String filter = tfSearchCatalog.getText().trim().toUpperCase();
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.toUpperCase().contains(filter));
            }
        });

        listView.setItems(filteredData);

        //catalog list listener codes
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                selectedListViewItem = newValue.toString(); //

                dbcreate.TableFiller(dbcreate.metaId(selectedListViewItem),tableView,at);
                //katalog tablosu methodu burdan çağırlacak
            }
        }
    });

    //tableview listener ! it returns selected row! ******************* TABLEVIEW SELECTED ITEM!!!!!
    tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            // Your action here
            if(tableView.getSelectionModel().getSelectedItem()!=null) {
                selectedTableViewItem = (ObservableList) newValue;

               // System.out.println("selected item of table"+ (ObservableList) newValue);
               // System.out.println("another way to get");
/*
                for (int i =0;i<((ObservableList) newValue).size();i++){
                    //0 dan başlatılırsa ID değeri de geliyor! id + diğer kolonlar!
                    System.out.println(((ObservableList) newValue).get(i).toString());
                }*/
            }
        }
    });
}

@FXML
    public void editCatalog(){

    //if any item(catalogName) selected on Listview
    if(listView.getSelectionModel().getSelectedItem()!=null){
        databasemanagement db = new databasemanagement();

        int metaID=db.metaIdInt(selectedListViewItem);
        db.getAllColumns(metaID);
        /**
         * this method will call a method that return TableClass Object**/
        /** This line will be activated after the called method impelemented**/
        // it is going to use also in the editInput() method.

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
        tfCatalogName.setText(selectedListViewItem);

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

        ObservableList<String> list= FXCollections.observableArrayList();
        list.setAll(db.getAllColumns(metaID).getColumnNames());
        lvCatalogInfo.setItems(list);

        tfCatalogName.setText(selectedListViewItem);

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
                   // returnedObject.setColumnNames(tfAttribute.getText().toUpperCase(Locale.ENGLISH).toString().trim());
                  //  returnedObject.setColumnDataTypes(cbDataType.getValue().toString());


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
                   // returnedObject.getColumnDataTypes().remove(getIndex);
                   // returnedObject.getColumnNames().remove(getIndex);

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
            TableClass willSend= new TableClass();
            databasemanagement db = new databasemanagement();
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
            int rowID=Integer.parseInt(selectedTableViewItem.get(0).toString());

            System.out.println(" ilk seçilen user inputs>>>>"); // seçilen input bilgileri  tableclass.userinputs listeye atılıyor
            for (int i=1;i<selectedTableViewItem.size();i++){
                System.out.println(selectedTableViewItem.get(i).toString());

                ReturnedObject.setUserInputs(selectedTableViewItem.get(i).toString());
            }


            TextField[] textFields = new TextField[ReturnedObject.getUserInputs().size()]; /// ESKİ BİLGİLER TEXTFİELDA YAZDIRILIYOR!
            for (int i = 0; i < ReturnedObject.getUserInputs().size();i++){

              //  Label label = new Label(sampleReturnedObject.getColumnNames().get(i).toString().toUpperCase());
                textFields[i]=new TextField();
                textFields[i].setText(ReturnedObject.getUserInputs().get(i).toString().toUpperCase());
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

                    System.out.println("__________BUTTON____ACTION");

                    System.out.println("tw size" + selectedTableViewItem.size());

                    for (int i=1;i<selectedTableViewItem.size();i++){

                        willSend.setUserInputs(textFields[i-1].getText().toUpperCase(Locale.ENGLISH).trim());

                    }

                    int metaID = db.metaIdInt(selectedListViewItem);

                    for (int i=0;i<db.getAllColumns(metaID).getColumnNames().size();i++){
                        willSend.setUserInputs(db.getAllColumns(metaID).getColumnNames().get(i));
                        System.out.println("eklendi _? " +db.getAllColumns(metaID).getColumnNames().get(i)  );
                    }





                    db.getAllColumns(metaID);
                    willSend.getUserInputs().setAll(db.getAllColumns(metaID).getColumnNames());

                    System.out.println("user inputsize"+ willSend.getUserInputs().size());
                    System.out.println("column namess ize"+ willSend.getColumnNames().size());



                    String tablename=db.metaId(selectedListViewItem);
                    db.deleteRow(rowID,tablename);
                    db.insertIntoSelectedCatalog(metaID,willSend);








                    /*

                    for(int i =0;i<ReturnedObject.getUserInputs().size();i++){
                        System.out.println("getting user input " + ReturnedObject.getUserInputs().get(i));
                    }

*/





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

@FXML
    public void btnCatalogAdd(){
    try {
        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(mainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("createCatalog.fxml"));
        dialog2.setTitle("Create a New Catalog");
        Stage stage2 = (Stage) dialog2.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(new Image(this.getClass().getResource("/icons/add.png").toString()));
        Parent dialogContent =fxmlLoader.load();
        dialog2.getDialogPane().setContent(dialogContent);
        dialog2.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        //sonuc nesnesini gönderir
        Optional<ButtonType> result =dialog2.showAndWait();
        if(result.get()==ButtonType.CLOSE) {
        }
    } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Ooops, Page Could NOT Found!");
        alert.setContentText("Something went wrong!");
        Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
        alert.showAndWait();
    }



    }

@FXML
   public void btnAddInput() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            databasemanagement dbInsert = new databasemanagement();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainScreen.getScene().getWindow()); //Asssinging parrent of dialog pane
            DialogPane dialogPane = new DialogPane();
            dialog.setTitle("Add New Input");
            VBox vBox = new VBox();
            //Kayıt eklenmek istenen tablodaki kolon değerlerinin database'den tableClass.getColumnNames listesine geri döndürülmesi gerekiyor
            //

            int metaID=dbInsert.metaIdInt(selectedListViewItem);
            TableClass returnObject=dbInsert.getAllColumns(metaID); /// //edit kısmında çağırılan methodla aynı olabilir.


            TextField[] fields = new TextField[returnObject.getColumnNames().size()];
            for (int i = 0; i < returnObject.getColumnNames().size(); i++) {
                fields[i] = new TextField();
                fields[i].setPromptText(returnObject.getColumnNames().get(i).toString().toUpperCase());

                vBox.getChildren().add(fields[i]);
            }
            Button buttonInsert = new Button("Add My Input");
            vBox.getChildren().add(buttonInsert);
            vBox.setSpacing(5);



            buttonInsert.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("butona basıldı");
                    /*
                     * Kullanıcın girdiği veriler TableClasstan yaratılan nesnenin sahip olduğu userInputs listesine eklenecektir.
                     * Bu liste sql Methodunu yazan sınıf tarafından parametre olarak alınacak.
                     * */
                    //createCatalogTable(tableClass.getUserInputs())

                    returnObject.getUserInputs().clear();


                    for (int i = 0; i < returnObject.getColumnNames().size(); i++) {
                        //tableview will contains observableList objects therefore we can get selectedItem as an instance of ObservableList
                        returnObject.setUserInputs(fields[i].getText().trim().toUpperCase(Locale.ENGLISH).trim());
                    }


                    dbInsert.insertIntoSelectedCatalog(metaID,returnObject);



                    for (int i = 0; i < returnObject.getColumnNames().size(); i++) {
                        //tableview will contains observableList objects therefore we can get selectedItem as an instance of ObservableList
                        System.out.println("alınan inputs  " + returnObject.getUserInputs().get(i));
                    }
                    // callTheUpdateRowSql(selectedItem,newInputs);
                }
            });

            dialogPane.setContent(vBox);

            dialogPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            dialogPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            dialog.getDialogPane().setContent(dialogPane);

            dialogPane.setContent(vBox);
            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.getDialogPane().setContent(dialogPane);
            dialog.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid Attempt");
            alert.setContentText("No Catalog Item Selected");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/warning.png").toString()));

            alert.showAndWait();
        }
        }

@FXML
    public void btnCatalogDelete(){
        databasemanagement dbDelete = new databasemanagement();

        if(listView.getSelectionModel().getSelectedItem()!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Hey! You Are Doing Something Risky !!");
            alert.setContentText("Are you sure to delete " +selectedListViewItem+ " ?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                //dropSelectedCatalog(listView.getSelectionModel().getSelectedItem().toString());

                int metaID=dbDelete.metaIdInt(selectedListViewItem);
                System.out.println("META ID RETURNED = " + metaID);

                dbDelete.dropCatalog(metaID);

                dbDelete.deleteCatalogfromMeta(metaID);

                listCatalogNames.remove(selectedListViewItem);





              //  listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
                
                //ilgili method önce seçilen itemin metaTabledaki IDsini alacak
                // sonra metaTable'dan seçilen katalog ismini silecek
                //sonra alınan ID ye ait olan katalog tablosunu silecek.

            } else {
              //kullanıcı katalog silme işleminden vazgeçti
            }



        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid Attempt");
            alert.setContentText("No Catalog Item Selected");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/warning.png").toString()));

            alert.showAndWait();

        }

    }

@FXML
    public void btnDeleteInput(){
       if(tableView.getSelectionModel().getSelectedItem()!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Hey! You Are Doing Something Risky !!");
            alert.setContentText("Are you sure about to delete selected input?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                ObservableList selectedItem = FXCollections.observableArrayList();
                selectedItem = (ObservableList) tableView.getSelectionModel().getSelectedItem();
                //database methodu 2. parametresinde aldığı observableListi 1. parametresinde aldığı ilgili catalog tablosundan bulup silecek
                // deleteSelectedTupleFromCatalogTable(listView.getSelectionModel().getSelectedItem(),selectedItem);

                databasemanagement dbdelete=new databasemanagement();
                String metaID=dbdelete.metaId(selectedListViewItem);

                System.out.println("row ID => ? " + selectedTableViewItem.get(0));

                int id=Integer.parseInt(selectedTableViewItem.get(0).toString());

                System.out.println("DELETION A GÖNDERİLEN PARAMETRELER >>> "+ id+ "   " + metaID);
                dbdelete.deleteRow(id,metaID);
            }else{
                //input silme işleminden vazgeçildi
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid Attempt");
            alert.setContentText("No Catalog Item Selected");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/warning.png").toString()));
            alert.showAndWait();
        }

}

@FXML
    public void showHelpDialog(){

    try {
        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(mainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("helpScreen.fxml"));
        dialog2.setTitle("Manual");
        Stage stage2 = (Stage) dialog2.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(new Image(this.getClass().getResource("/icons/add.png").toString()));
        Parent dialogContent =fxmlLoader.load();
        dialog2.getDialogPane().setContent(dialogContent);
        dialog2.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        //sonuc nesnesini gönderir
        Optional<ButtonType> result =dialog2.showAndWait();
        if(result.get()==ButtonType.CLOSE) {
        }
    } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Ooops, Page Could NOT Found!");
        alert.setContentText("Something went wrong!");
        Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
        alert.showAndWait();
    }

    }







}

