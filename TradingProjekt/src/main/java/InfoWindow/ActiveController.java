package InfoWindow;

import com.example.javafxtest.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ActiveController implements Initializable {

    public ActiveController() {
    }

    @FXML
    private Parent rootNode;
    @FXML
    private AnchorPane rootPane;

    // Toggle buttons and choicebox
    @FXML
    private ToggleButton tglBuy, tglSell, tglLong, tglBuyOP, tglSellOP;
    @FXML
    private ToggleGroup tglSellBuy;
    @FXML
    private ToggleGroup tglBuySellOpenPos;
    @FXML
    private ChoiceBox<String>  cbSymb;

    //TextFields &
    @FXML
    private DatePicker dateColumn, dateColumnOPos;
    @FXML
    private TextField stopField, lblTickerRT, priceFieldOPos, unitsFieldsOpenP, unitsField, priceField, tickerField;


    //title bar
    @FXML
    private Text txtMinize, txtCloseWindow;
    @FXML
    private Rectangle rectangleOpenWindow;
    @FXML
    private Pane topPane;
    private Stage stage;


    // Market internals and portfolio status
    @FXML
    private Circle iconIWMma10, iconIwm10Over20,iconIwmma20, iconQqq10Over20, iconQqqma10, iconQqqma20,iconSpy10Over20, iconSpyma10,iconSpyma20;
    @FXML
    private Circle iconPortfolioO10,iconPortfolioO20, iconPortfolio10O20;
    @FXML
    private Circle iconRisk;
    @FXML
    private Label lblWorstCase, lblAdjr, lblOpenR, lblDrawdown;
    @FXML
    private Label lblAtrDistIWM, lblAtrDistSPY, lblAtrDistQQQ, lblNewRisk;

    // Labels for target, units and exists
    @FXML
    private Label lbl3RTarget,  lblMaxUnits, lblMinUnits, lblExists;

    // Labels for open position executions
    @FXML
    private Label lblSymbOp, lblPriceOp, lblUnitsOp, lblDateOp;


    // Open Pos TBL
    @FXML
    private TableView<OpenPos> tblOpen;
    @FXML
    private TableColumn<OpenPos, Double> tblOpen3Atr, tblOpen3R, tblOpen6R, tblOpen9R, tblOpenR, tblWorstCase,
                                            tblOpenPrice, tblOpenStop,tblOpenThird, tblCurrentPrice, tblClosePrice;
    @FXML
    private TableColumn<OpenPos, String> tblOpenSymb;
    @FXML
    private TableColumn<OpenPos, Integer> tblOpenULeft, tblOpenUnits;


    // Tbl Interest TBL
    @FXML
    private TableView<PotentialPos> tblInterest;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestADR;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestUnits;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestHod;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestLod;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestPrice;
    @FXML
    private TableColumn<PotentialPos, String> tblInterestSymb;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestStop;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestRisk;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterest5mADR;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterestRange;
    @FXML
    private TableColumn<PotentialPos, Double> tblInterest3R;

    // Buttons
    @FXML
    private Button btnExecuteNP;
    @FXML
    private Button btnExecuteOP;
    @FXML
    private Button btnAddInterestTbl;


    TopPaneActions topPaneActions = new TopPaneActions();
    OpenPosTblView openPosTblView = new OpenPosTblView();
    InterestTblView interestTblView = new InterestTblView();
    Validation validation = new Validation();
    RiskCalculator riskCalculator = new RiskCalculator();
    ObservableList<OpenPos> tblOpen (){
        return FXCollections.observableArrayList();
    };
    ObservableList<PotentialPos> interestList (){
        return FXCollections.observableArrayList();
    };
    @FXML
    void enterExecute(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER)
        {
            ActionEvent a = new ActionEvent();
            openPosAdd();
        }
    }
    // Event handler for Enter key in lblTickerRT field
    @FXML
    void enterInterestPosEnter(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER)
        {
            addPotentialPosToTbl();
        }
    }


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


    public void changeTblOpenPos(ActionEvent event) throws IOException
    {
        openPosTblView.handleOpenConfirmWindow(tblOpen, this);
    }


    //Handle add of the pos in lblTickerRT field
    private void addPotentialPosToTbl()
    {
            if (interestTblView.getPotentialPos().stream()
                    .anyMatch(potentialPos -> potentialPos.getSymb().equals(lblTickerRT.getText().toUpperCase())))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate of symbol");
                alert.setHeaderText(null);
                alert.setContentText("You cant insert a symbol twice");
                alert.showAndWait();
                lblTickerRT.clear();
            } else {
                interestTblView.handleAddInterestPos(lblTickerRT, tglLong, tblInterest, lblAdjr);
            }
    }

    public void switchScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxtest/closedTrades-view.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene( root, 860, 680);
        stage.setScene(scene);
        stage.show();
    }


    public void setUpOpenPositionsInfo()
    {
        openPosTblView.setUpOpenPosLabels(tblOpen, lblWorstCase, lblOpenR);
    }

    public void refreshTableOpenPos()
    {
        openPosTblView.refresh(tblOpen);
        tblOpen.getSortOrder().add(tblOpenSymb);
        setUpOpenPositionsInfo();
    }

    public void setId()
    {
        openPosTblView.setRightId();
    }
    public void removeFromOpenPosTbl()
    {
        openPosTblView.removeOpenPos(tblOpen);
        tblOpen.getSortOrder().add(tblOpenSymb);
        setUpOpenPositionsInfo();
    }
    public void removeFromInterestPosTbl()
    {
        interestTblView.handleRemovePos(tblInterest);
        interestTblView.refresh();
    }
    public void openPosAdd()
    {
        System.out.println("Click on add");
        boolean numbersTestPrice = validation.CheckNumberTextfields(priceField);
        boolean numbersTestStop = validation.CheckNumberTextfields(stopField);
        boolean numbersTestUnits = validation.CheckNumberTextfields(unitsField);
        boolean dateSelected = validation.dateSelected(dateColumn);
        if(numbersTestStop && numbersTestPrice && numbersTestUnits && dateSelected) {
            String tickerF = tickerField.getText();
            String priceF = priceField.getText();
            String stopF = stopField.getText();
            String unitsF = unitsField.getText();
            boolean existsInTblInterest = false;
            boolean existsInTblOpenPos = false;
            int foundAtInterestTbl = 0;
            int foundAtOpenPosTbl = 0;
            char side;
            if (tglBuy.isSelected()) {
                side = 'B';
            } else {
                side = 'S';
            }
            if (!interestTblView.getPotentialPos().isEmpty()) {
                int i = 0;
                while (i < interestTblView.getPotentialPos().size() && !existsInTblInterest) {
                    if (tickerF.toUpperCase().equals(interestTblView.getPotentialPos().get(i).getSymb())) {
                        existsInTblInterest = true;
                        foundAtInterestTbl = i;
                    }
                    i++;
                }
            }
            if (!openPosTblView.getOpenPos().isEmpty()) {
                int i = 0;
                while (i < openPosTblView.getOpenPos().size() && !existsInTblOpenPos) {
                    if (tickerF.toUpperCase().equals(openPosTblView.getOpenPos().get(i).getSymb())) {
                        existsInTblOpenPos = true;
                        foundAtOpenPosTbl = i;
                    }
                    i++;
                }
            }
                if (existsInTblInterest && !existsInTblOpenPos) {
                openPosTblView.HandleAddNewPosExisting(tblOpen, interestTblView.getPotentialPos().get(foundAtInterestTbl),
                        priceF, tickerF, stopF, unitsF, dateColumn, side, lblAdjr);
            } else if(!existsInTblInterest && !existsInTblOpenPos){
                    openPosTblView.HandleAddNewPosNew(tblOpen, tickerF, priceF, stopF, unitsF, dateColumn, side, lblAdjr);
            }
            if(existsInTblOpenPos)
            {
                openPosTblView.HandleAddToOpenPos(tblOpen, openPosTblView.getOpenPos().get(foundAtOpenPosTbl), priceF,tickerF, stopF, unitsF, dateColumn, side);
            }
            setUpOpenPositionsInfo();
            tickerField.clear();
            priceField.clear();
            stopField.clear();
            unitsField.clear();
            lblExists.setVisible(false);
            lblNewRisk.setVisible(false);
            lbl3RTarget.setVisible(false);
            lblMaxUnits.setVisible(false);
            lblMinUnits.setVisible(false);
            iconRisk.setVisible(false);
        }
        tblOpen.getSortOrder().add(tblOpenSymb);
    }
    public void moveInterestToExecutePosition()
    {
        tblInterest.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                interestTblView.handleMoveToExecute(tblInterest, tickerField, priceField, unitsField, stopField, tglBuy, tglSell);
                manageChangePriceField();
            }
    });
    }
    public void addToExecuteOpenPosition() {
        tblOpen.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
            {
                openPosTblView.HandleExistingPos(tblOpen, cbSymb, priceFieldOPos, unitsFieldsOpenP, dateColumnOPos,
                        lblSymbOp, lblUnitsOp, lblPriceOp, lblDateOp, tglBuyOP, tglSellOP, btnExecuteOP);
        }});
    }
    public void setUpRiskInfo()
    {
        riskCalculator.setUpMarketScore(lblAtrDistQQQ, lblAtrDistSPY, lblAtrDistIWM,
                iconQqqma10, iconQqqma20, iconQqq10Over20,
                iconSpyma10, iconSpyma20, iconSpy10Over20,
                iconIWMma10, iconIwmma20, iconIwm10Over20);
        riskCalculator.setUpOpenPosScore(tblOpen);
        riskCalculator.setUpDrawDownScore(lblDrawdown );
        riskCalculator.setUpPortfolioIcons(iconPortfolioO10, iconPortfolioO20, iconPortfolio10O20);
        riskCalculator.setUpPortfolioScore();
        riskCalculator.setUpScore();
        riskCalculator.calcAdjR(lblAdjr);
    }

    public void executeOpenPos() throws IOException {
        boolean numbersTestPrice = validation.CheckNumberTextfields(priceFieldOPos);
        boolean numbersTestUnits = validation.CheckNumberTextfields(unitsFieldsOpenP);
        if(numbersTestUnits && numbersTestPrice)
        {
        openPosTblView.openPositionExecution(tblOpen, cbSymb, priceFieldOPos, unitsFieldsOpenP, dateColumnOPos,
                lblSymbOp, lblUnitsOp, lblPriceOp, lblDateOp, tglBuyOP, tglSellOP, btnExecuteOP, this);
            tblOpen.getSortOrder().add(tblOpenSymb);
            setUpOpenPositionsInfo();
        }
    }


    public void changeColorButton()
    {
        if(!tickerField.getText().isEmpty() || !priceField.getText().isEmpty()||!stopField.getText().isEmpty()||!unitsField.getText().isEmpty())
        {
            if(tglBuy.isSelected())
            {
                btnExecuteNP.setStyle("-fx-background-color: #388e3c;");
                btnExecuteNP.setText("Buy");
            }
            else if(tglSell.isSelected())
            {
                btnExecuteNP.setStyle("-fx-background-color: #e53935");
                btnExecuteNP.setText("Sell");
            }
        }
        if(tglBuyOP.isSelected())
        {
            btnExecuteOP.setStyle("-fx-background-color: #388e3c;");
            btnExecuteOP.setText("Buy");
        }
        else if(tglSellOP.isSelected())
        {
            btnExecuteOP.setStyle("-fx-background-color: #e53935;");
            btnExecuteOP.setText("Sell");
        }
    }
    public void manageChangeInTickerField(){
        interestTblView.handleChangeInTickerField(tblOpen, tickerField, lblExists);
    }

    public void manageChangePriceField()
    {
        interestTblView.handlePriceAndStopFieldChanges(tblOpen, tickerField, priceField, stopField, unitsField, tglBuy
                                                    ,riskCalculator.getRisk(), lbl3RTarget, lblMaxUnits, lblMinUnits
                                                    ,lblNewRisk, iconRisk);
    }
    private void handleEditInInterestTbl(TableColumn.CellEditEvent<PotentialPos, Double> event) {
        if(validation.CheckNumberString(String.valueOf(event.getNewValue())))
        {
            TableColumn<PotentialPos, Double> eventColumn = event.getTableColumn();
            InterestTblView i = new InterestTblView();
            i.handleEditInView(eventColumn, event, tblInterest, lblAdjr);
        }
    }
    public void handleEditInOpenPosTbl(TableColumn.CellEditEvent<OpenPos, Double> event)
    {
        TableColumn<OpenPos, Double> eventColumn = event.getTableColumn();
        openPosTblView.handleEditStop(event, tblOpen);
        setUpOpenPositionsInfo();
    }

    public void handleLabels()
    {
        //interestTblView.handleLabelsActions(tblOpen, lbl3RTarget, lblMaxUnits, lblMinUnits, tglBuy,
          //      riskCalculator.getRisk(), tickerField, priceField, unitsField, stopField, lblExists, lblNewRisk);
    }
    public void setBackResult()
    {
        JsonFixer json = new JsonFixer();
        int size = tblOpen.getItems().size();
        for(int i =0; i<size; i++)
        {
            tblOpen.getItems().remove(0);
        }
        json.loadToView(tblOpen);
        tblOpen.refresh();
        tblOpen.getSortOrder().add(tblOpenSymb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //rectangleOpenWindow.getStyleClass().add("rectangle"); // Add the CSS style class
        btnAddInterestTbl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPotentialPosToTbl();
            }
        });

        ChangeListener<Boolean> focusListener = (observable, oldValue, newValue) -> {
            if (!newValue) {
                // At least one of the text fields lost focus, perform the logic
                manageChangePriceField();
                handleLabels();
                changeColorButton();
            }
        };
        ChangeListener<Boolean> focusListenerTicker = (observable, oldValue, newValue) -> {
            if (!newValue) {
                // At least one of the text fields lost focus, perform the logic
                //handleLabels();
                //changeColorButton();
                manageChangeInTickerField();
            }
        };
        ChangeListener<Boolean> focusListenerUnits = (observable, oldValue, newValue) -> {
            if (!newValue) {
                // At least one of the text fields lost focus, perform the logic
                interestTblView.handleUnitsField(tblOpen, tickerField, priceField, stopField, unitsField, lblNewRisk, tglBuy,
                                                 lbl3RTarget, lblMaxUnits, lblMinUnits, lblExists, iconRisk);
            }
        };


        // Focuslistener
        tickerField.focusedProperty().addListener(focusListenerTicker);
        priceField.focusedProperty().addListener(focusListener);
        stopField.focusedProperty().addListener(focusListener);
        unitsField.focusedProperty().addListener(focusListenerUnits);
        tglSellBuy.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            handleLabels();
        });
        tglBuySellOpenPos.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                changeColorButton();
            }
        });

        // Toggle buttons
        tglBuy.selectedProperty().setValue(true);
        tglLong.selectedProperty().setValue(true);
        tglSellOP.selectedProperty().setValue(true);

        //Styling
        tblInterest.setStyle("-fx-font-size: 10px;");
        tblOpen.setStyle("-fx-font-size: 13px;");

        // Table with interest in maybe buying
        tblInterestSymb.setCellValueFactory(new PropertyValueFactory<PotentialPos, String>("symb"));
        tblInterestPrice.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("price"));
        tblInterestPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tblInterestPrice.setOnEditCommit(event -> {
            PotentialPos item = event.getTableView().getItems().get(event.getTablePosition().getRow());
            item.setStop(event.getNewValue());
        });
        tblInterestHod.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("hod"));
        tblInterestHod.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tblInterestHod.setOnEditCommit(event -> {
            PotentialPos item = event.getTableView().getItems().get(event.getTablePosition().getRow());
            item.setStop(event.getNewValue());
        });

        tblInterestLod.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("lod"));
        tblInterestLod.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tblInterestLod.setOnEditCommit(event -> {
            PotentialPos item = event.getTableView().getItems().get(event.getTablePosition().getRow());
            item.setStop(event.getNewValue());
        });
        tblInterestRisk.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("risk"));
        tblInterestUnits.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("units"));
        tblInterestADR.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("adr"));
        tblInterest5mADR.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("adr5min"));

        tblInterestStop.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("stop"));
        tblInterestStop.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tblInterestStop.setOnEditCommit(event -> {
            PotentialPos item = event.getTableView().getItems().get(event.getTablePosition().getRow());
            item.setStop(event.getNewValue());
        });
        tblInterestRange.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("rangeVAtr"));
        tblInterest3R.setCellValueFactory(new PropertyValueFactory<PotentialPos, Double>("threeRtarget"));
        tblInterestPrice.setOnEditCommit(this::handleEditInInterestTbl);
        tblInterestStop.setOnEditCommit(this::handleEditInInterestTbl);
        tblInterestHod.setOnEditCommit(this::handleEditInInterestTbl);
        tblInterestHod.setOnEditCommit(this::handleEditInInterestTbl);
        tblInterestLod.setOnEditCommit(this::handleEditInInterestTbl);
        tblInterest.setItems(interestList());
        tblInterest.setEditable(true);

        // Set date to todays date
        LocalDate today = LocalDate.now();
        dateColumn.setValue(today);
        dateColumnOPos.setValue(today);


        // Table with open Pos
        tblOpenSymb.setCellValueFactory(new PropertyValueFactory<OpenPos, String>("symb"));
        tblOpenPrice.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("openPrice"));
        tblOpenUnits.setCellValueFactory(new PropertyValueFactory<OpenPos, Integer>("startUnits"));
        tblOpenStop.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("stop"));
        tblOpenStop.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tblOpenStop.setOnEditCommit(event -> {
            OpenPos item = event.getTableView().getItems().get(event.getTablePosition().getRow());
            item.setStop(event.getNewValue());
        });
        tblOpenStop.setOnEditCommit(this::handleEditInOpenPosTbl);
        tblOpenULeft.setCellValueFactory(new PropertyValueFactory<OpenPos, Integer>("unitsLeft"));
        tblOpenThird.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("thirdSell"));
        tblOpen3Atr.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("atrTarget"));
        tblOpen3R.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("threR"));
        tblOpen6R.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("sixR"));
        tblOpen9R.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("nineR"));
        tblCurrentPrice.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("currentPrice"));
        tblClosePrice.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("closePrice"));
        tblOpenR.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("openR"));
        tblWorstCase.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("worstCase"));
        tblOpen.setItems(tblOpen());
        tblOpen.setEditable(true);

        // All text info to fields sets to false at the start
        lbl3RTarget.setVisible(false);
        lblMaxUnits.setVisible(false);
        lblMinUnits.setVisible(false);
        lblExists.setVisible(false);
        lblNewRisk.setVisible(false);

        // Open pos manage
        btnExecuteOP.setVisible(false);
        lblPriceOp.setVisible(false);
        lblSymbOp.setVisible(false);
        lblDateOp.setVisible(false);
        lblUnitsOp.setVisible(false);
        cbSymb.setVisible(false);
        unitsFieldsOpenP.setVisible(false);
        priceFieldOPos.setVisible(false);
        dateColumnOPos.setVisible(false);
        tglBuyOP.setVisible(false);
        tglSellOP.setVisible(false);
        iconRisk.setVisible(false);



        // Reduce the ".00" from the column units
        tblInterestUnits.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Format the double value as an integer with no decimal places
                        setText(Integer.toString(item.intValue()));
                    }
                }
            };
        });
//
//        dateColumn.getStyleClass().add("date-picker");
//        dateColumnOPos.getStyleClass().add("date-picker");


        // load Open pos to the tableView
        JsonFixer jsonFixer = new JsonFixer();
        jsonFixer.loadToView(tblOpen);
        ArrayList<OpenPos> openPosAdd = jsonFixer.addToList();

        OpenPosTblView.addPos(openPosAdd);
        setId();
        setUpOpenPositionsInfo();
        setUpRiskInfo();
        tblOpen.getSortOrder().add(tblOpenSymb);

    }


}
