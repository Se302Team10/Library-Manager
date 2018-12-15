package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import javax.swing.text.html.ImageView;

public class Controller {

    @FXML
    private TableView<?> tvTable;

    @FXML
    private Button btnAddTupple;

    @FXML
    private Button btnEditTupple;

    @FXML
    private ListView<?> lvCatalogList;

    @FXML
    private Button btnCreateCatalog;

    @FXML
    private ImageView btnEditCatalog;

    @FXML
    private Button btnDeleteCatalog;

}
