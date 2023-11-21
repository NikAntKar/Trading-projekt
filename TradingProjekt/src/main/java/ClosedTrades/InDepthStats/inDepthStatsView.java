package ClosedTrades.InDepthStats;


import Analyse.Stats;
import Analyse.Trades;
import DataBase.DatabaseController;
import com.example.javafxtest.TopPaneActions;
import com.example.javafxtest.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;


public class inDepthStatsView implements Initializable {
    public inDepthStatsView()  {

    }

    //Date picker
    @FXML
    private DatePicker datePickEndDate;
    @FXML
    private DatePicker datePickStartDate;
    @FXML
    private TreeTableColumn<?, ?> tbwPrice;

    @FXML
    private TreeTableColumn<?, ?> tbwSymbol;

    @FXML
    private TreeTableColumn<?, ?> tbwUnits;

    //Labels
    @FXML
    private Label lblAdjrPnl, lblBestClosePrice, lblBestCloseR, lblOpenValue, lblCloseDate,
            lblDolPnl, lblOpenDate, lblPercentPnl, lblRTaken, lblCloseValue,
            lblSymbol, lblTimeHeld, lblTransactions, lblInsideRisk, lblClosePrice, lblOpenPrice,
            lblQQQ, lblSPY, lblIWM, lblIWMAtrdist,lblSPYAtrdist,lblQQQAtrdist, lblResultOfSearch,
            lblAtrTo10MAQQQ, lblAtrTo10MASPY, lblAtrTo10MAIWM;


    //Table
    @FXML
    private TableColumn<Trades, String> tblDate;

    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private CheckBox cbWinners, cbLosers;
    int currentValueInSpinner;
    @FXML
    private TableView<Trades> tblInDepthStats;

    @FXML
    private TableColumn<Trades, Double> tblPrice;

    @FXML
    private TableColumn<Stats, Double> tblResult;

    @FXML
    private TableColumn<Trades, Character> tblSide;

    @FXML
    private TableColumn<Stats, String> tblSymbol;

    @FXML
    private TableView<Stats> tblSymbols;

    @FXML
    private TableColumn<Trades, Integer> tblUnits;

    //title bar
    @FXML
    private Text txtMinize, txtCloseWindow;
    @FXML
    private Rectangle rectangleOpenWindow;
    @FXML
    private Pane topPane;
    TopPaneActions topPaneActions = new TopPaneActions();
    @FXML
    private void minimize(MouseEvent event)
    {
        topPaneActions.minimize(event);
    }
    @FXML
    private void maximize(MouseEvent event)
    {
        topPaneActions.maximize(event);
    }
    @FXML
    private void close(MouseEvent event)
    {
        topPaneActions.close(event);
    }
    @FXML
    private void handleMoveWindowAction(MouseEvent event)
    {
        topPaneActions.handleMoveWindowAction(event);
    }
    @FXML
    private void handleMovement(MouseEvent event)
    {
        topPaneActions.handleMovement(event);
    }

    InDepthStatsController controller = new InDepthStatsController();
    DatabaseController database = new DatabaseController();

    Validation validation = new Validation();
    public void insertToTbl()
    {
        controller.setUpSymbolsTableView(tblSymbols);
        //setUpSymbolsTableView(tblSymbols);
    }
    public void insertTrades()
    {
        controller.setUpTradesTableView(tblInDepthStats, tblSymbols);
    }
    public void setUpLabelsSymbol()
    {
        controller.setUpLabelsSymbol(tblInDepthStats, tblSymbols, lblAdjrPnl, lblBestClosePrice,
                lblBestCloseR, lblOpenValue, lblCloseDate,
                lblDolPnl, lblOpenDate, lblPercentPnl, lblRTaken, lblCloseValue,
                lblSymbol, lblTimeHeld, lblTransactions,lblInsideRisk, lblOpenPrice, lblClosePrice);

    }
    public void setUpMarketAtrDist() throws ParseException {
        controller.setUpMarketAtrDist(lblQQQAtrdist, lblSPYAtrdist,lblIWMAtrdist, tblInDepthStats.getItems().get(0).getDate());
    }
    public void handleSearch()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        boolean beginDate = validation.dateSelected(datePickStartDate);
        boolean endDate = false;
        if(beginDate)
        {
            endDate = validation.dateSelected(datePickEndDate);
        }
        if(beginDate && endDate)
        {
            if(cbLosers.isSelected() || cbWinners.isSelected())
            {
                controller.handleDateSearch(tblSymbols, datePickStartDate.getValue().toString(),
                        datePickEndDate.getValue().toString(), cbWinners, cbLosers, spinner);
                controller.setUpMarketPerformance(lblQQQ, lblSPY, lblIWM, datePickStartDate, datePickEndDate);
                controller.setUpLabelForResult(lblResultOfSearch, cbWinners, cbLosers, spinner);
            }
            else
            {
                alert.setTitle("Make a choice");
                alert.setHeaderText(null);
                alert.setContentText("You have to select losers or winners");
                alert.showAndWait();
            }
        }
    }
    ObservableList<Stats> tblSymbols (){
        return FXCollections.observableArrayList();
    };
    ObservableList<Trades> tblInDepthStats (){
        return FXCollections.observableArrayList();
    };
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblSymbol.setVisible(false);
        lblAtrTo10MAQQQ.setVisible(false);
        lblAtrTo10MAIWM.setVisible(false);
        lblAtrTo10MASPY.setVisible(false);

        rectangleOpenWindow.getStyleClass().add("rectangle"); // Add the CSS style class

        tblSymbol.setCellValueFactory(new PropertyValueFactory<Stats, String>("symb"));
        tblResult.setCellValueFactory(new PropertyValueFactory<Stats, Double>("result"));
        tblSymbols.setItems(tblSymbols());

        tblDate.setCellValueFactory(new PropertyValueFactory<Trades, String>("date"));
        tblSide.setCellValueFactory(new PropertyValueFactory<Trades, Character>("side"));
        tblUnits.setCellValueFactory(new PropertyValueFactory<Trades, Integer>("units"));
        tblPrice.setCellValueFactory(new PropertyValueFactory<Trades, Double>("price"));
        tblInDepthStats.setItems(tblInDepthStats());
        //insertToTbl();

        tblSymbols.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double-click
                // Double-click action
                // You can access the selected item like this:
                Stats selectedItem = tblSymbols.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Perform the action you want on double-click
                    // For example, you can display details of the selected item
                    insertTrades();
                    setUpLabelsSymbol();
                    lblSymbol.setVisible(true);
                    lblAtrTo10MAQQQ.setVisible(true);
                    lblAtrTo10MAIWM.setVisible(true);
                    lblAtrTo10MASPY.setVisible(true);

                    try {
                        setUpMarketAtrDist();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1000);
        valueFactory.setValue(10);
        spinner.setValueFactory(valueFactory);

    }
}