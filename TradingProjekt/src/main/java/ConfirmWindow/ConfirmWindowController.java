package ConfirmWindow;
import ClosedTrades.ClosedTradesView;
import com.example.javafxtest.FormatterClass;
import com.example.javafxtest.JsonFixer;
import InfoWindow.OpenPos;
import com.example.javafxtest.TopPaneActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.CharacterStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConfirmWindowController  implements Initializable{

 public ConfirmWindowController()
 {
   f= new FormatterClass();
 }

 String symb;

  @FXML
  private Button btnConfirm;

  @FXML
  private Label lblSymbID;

  @FXML
  private TableView<Trade> tblAllTrades;
  @FXML
  private TableView<OpenPos> tblSum;
  @FXML
  private TableView<OpenPos> tblSymbStats;
  @FXML
  private Button testKnappen()
  {
      return null;
  }
  @FXML
  private TableColumn<OpenPos, Integer> Units;

  @FXML
  private TableColumn<Trade, String> tblTradesDate;
  @FXML
  private TableColumn<Trade, Double> tblTradesPrice;
  @FXML
  private TableColumn<Trade, Character> tblTradesSide;
  @FXML
  private TableColumn<Trade, Integer> tblTradesUnits;
  @FXML
  private TableColumn<Trade, Integer> tblTradesCUnits;

  @FXML
  private Label lblStatus;

  @FXML
  private TableColumn<OpenPos, Double> tblTotOpenPrice;
  @FXML
  private TableColumn<OpenPos, Double> tblTotClosePrice;
  @FXML
  private TableColumn<OpenPos, String> tblDate;

  @FXML
  private TableColumn<OpenPos, Double> tblHOD;

  @FXML
  private TableColumn<OpenPos, ?> tblIDPriour;

  @FXML
  private TableColumn<OpenPos, Double> tblLOD;

 @FXML
  private TableColumn<OpenPos, Double> tblPrice;

  @FXML
  private TableColumn<OpenPos, Character> tblTotSide;

  @FXML
  private TableColumn<OpenPos, Integer> tblTotUnits;
  @FXML
  private Label lblTest;


  @FXML
  private Label txtAdjR;
 @FXML
 private Label lblUnitsLeft;
 @FXML
 private Button btnBack;
 @FXML
 private Button remove;
 @FXML
  private Label txtR;

 TopPaneActions topPaneActions = new TopPaneActions();
 private InfoWindow.ActiveController controller;
 private Stage stage;
 FormatterClass f;
 OpenPos openPos;
 ArrayList<Trade>tradeList=new ArrayList<>();
 DataHolder data = DataHolder.getInstance();
 JsonFixer json = new JsonFixer();


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

 public void readJsonObject()
 {
   openPos = json.getOneItem(symb);
 }

  public void setSymb(String symbol)
{
  symb = symbol;
}
  public void setLabel()
  {
   lblSymbID.setText(openPos.getSymb());
  }
  public void setUpTradesTbl()
  {
  ObservableList<Trade> items = tblAllTrades.getItems();

  OpenPos o = json.getOneItem(openPos.getSymb());
  for(int i = 0; i< openPos.getSides().size(); i++)
   {
     tradeList.add(new Trade(openPos.getSides().get(i), openPos.getPrice().get(i),
             openPos.getUnits().get(i), openPos.getDate().get(i)));
     items.add(tradeList.get(i));
   }
  }
 public void setUpSumTbl()
 {
  ObservableList<OpenPos> items = tblSum.getItems();
  items.add(openPos);
 }
 public void setUpSymbTbl()
 {
  ObservableList<OpenPos> items = tblSymbStats.getItems();
  items.add(openPos);
 }
 public void remove()
 {
    Trade t = tblAllTrades.getSelectionModel().getSelectedItem();
    if(t != null)
    {
      int index = tblAllTrades.getItems().indexOf(t);
      tblAllTrades.getItems().remove(index);
      changesInTblAllTrades();
      openPos.removeTransaction(index);
     tradeList.remove(index);
      json.update(openPos);
     System.out.println(openPos.getSides());

    }
    else
    {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("No selected item");
      alert.setContentText("No item are selected");
      alert.setHeaderText(null); // Optional header text
      alert.showAndWait();
    }
 }



 public void changesInTblAllTrades()
 {
  ArrayList<Double> holdBuys = new ArrayList<Double>();
  ArrayList<Double> holdSells = new ArrayList<Double>();
  ArrayList<Integer> holdUnitsBuy = new ArrayList<Integer>();
  ArrayList<Integer> holdUnitSell = new ArrayList<Integer>();
  int holdTotalUnitsBuy = 0;
  int holdTotalUnitsSell = 0;
  for(int i = 0; i<tblAllTrades.getItems().size();i++)
   {
      if(tblAllTrades.getItems().get(i).getSide() == 'B')
      {
         holdBuys.add(tblAllTrades.getItems().get(i).getPrice());
         holdUnitsBuy.add(tblAllTrades.getItems().get(i).getUnits());
         holdTotalUnitsBuy = holdTotalUnitsBuy + tblAllTrades.getItems().get(i).getUnits();
      }
      else {
       holdSells.add(tblAllTrades.getItems().get(i).getPrice());
       holdUnitSell.add(tblAllTrades.getItems().get(i).getUnits());
       holdTotalUnitsSell = holdTotalUnitsSell + tblAllTrades.getItems().get(i).getUnits();
      }
   }
  double averageBuy =0;
  double averageSell=0;
  int indexBuys=0;
  int indexSells = 0;
  for(int i = 0; i<holdBuys.size() + holdSells.size();i++)
  {
     if(tblAllTrades.getItems().get(i).getSide() == 'B')
     {
         averageBuy = averageBuy+(holdBuys.get(indexBuys) * holdUnitsBuy.get(indexBuys));
         indexBuys++;
     }
     else
     {
           averageSell = averageSell+ (holdSells.get(indexSells) * holdUnitSell.get(indexSells));
           indexSells++;
      }
  }
  averageBuy = f.formatDoubleXXX(averageBuy /holdTotalUnitsBuy);
  averageSell = f.formatDoubleXXX(averageSell/holdTotalUnitsSell);
  tblSum.getItems().get(0).setOpenPrice(averageBuy);
  tblSum.getItems().get(0).setClosePrice(averageSell);

  if(tblAllTrades.getItems().get(0).getSide() == 'B')
    {
     tblSum.getItems().get(0).setStartUnits(holdTotalUnitsBuy);
     openPos.setOpenPrice(averageBuy);
     openPos.setUnitsLeft(holdTotalUnitsBuy-holdTotalUnitsSell);
    }
  else
  {
   tblSum.getItems().get(0).setStartUnits(holdTotalUnitsSell);
   openPos.setOpenPrice(averageSell);
   openPos.setUnitsLeft(holdTotalUnitsSell-holdTotalUnitsBuy);
  }
  setStatusLabels();
  tblSum.refresh();
 }
 public void setParentController(InfoWindow.ActiveController controller)
 {
     this.controller = controller;
 }

  public void confirm(ActionEvent actionEvent) throws IOException
  {
   ArrayList<Character> holdSide = new ArrayList<>();
   ArrayList<Integer> holdUnits = new ArrayList<>();
   ArrayList<Double> holdPrice = new ArrayList<>();
   ArrayList<String> holdDate = new ArrayList<>();
   System.out.println("sides 3 " + openPos.getSides());
   int newUnits =0;
   for (int i = 0; i<tradeList.size();i++)
   {
    holdSide.add(tradeList.get(i).getSide());
    holdUnits.add(tradeList.get(i).getUnits());
    holdPrice.add((tradeList.get(i).getPrice()));
    holdDate.add((tradeList.get(i).getDate()));
    if(holdSide.get(i).equals('B'))
      newUnits = newUnits + tradeList.get(i).getUnits();
    else
      newUnits = newUnits - tradeList.get(i).getUnits();
   }
   openPos.setUnitsLeft(newUnits);
   openPos.setSides(holdSide);
   openPos.setUnits(holdUnits);
   openPos.setPrice(holdPrice);
   openPos.setDate(holdDate);
   openPos.setThirdSell(newUnits/3);
   double risk = 0;
   if(openPos.getSide() =='B')
   {
    if(openPos.getOpenPrice() > openPos.getStop()) {
     risk = openPos.getOpenPrice() - openPos.getStop();
     openPos.setThreR(f.formatDoubleXX((risk * 3) + openPos.getOpenPrice()));
     openPos.setSixR(f.formatDoubleXX((risk * 6) + openPos.getOpenPrice()));
     openPos.setNineR(f.formatDoubleXX((risk * 9) + openPos.getOpenPrice()));
    }
   }
   else
   {
    if(openPos.getOpenPrice() < openPos.getStop())
      {
       risk = openPos.getStop() - openPos.getOpenPrice() ;
       openPos.setThreR(f.formatDoubleXX(openPos.getOpenPrice() - (risk * 3)));
       openPos.setSixR(f.formatDoubleXX(openPos.getOpenPrice() -(risk * 6)));
       openPos.setNineR(f.formatDoubleXX(openPos.getOpenPrice() -(risk * 9)));
      }
   }
   json.update(openPos);
   data.setOpenPos(openPos);
   //  Back to main
   stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
   stage.close();

   if(openPos.getUnitsLeft() == 0)
   {
     json.remove(openPos);
     ClosedTradesView closedTrade = new ClosedTradesView();
     closedTrade.setUpAll();
     closedTrade.addNewItems(openPos);
   }
   controller.setBackResult();
   //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxtest/openpositions-view.fxml"));
   //Object root = loader.load();
   //stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
   //Scene scene = new Scene((Parent) root, 860, 680);
   //stage.setScene(scene);
   //stage.show();
   }

  public void addToTables()
  {
     setUpSumTbl();
     setUpTradesTbl();
  }

 private ObservableList<OpenPos>tblSum(){
  return FXCollections.observableArrayList();
 }
 private ObservableList<Trade>tblAllTrades(){
  return FXCollections.observableArrayList();
 }
 private ObservableList<OpenPos>tblSymbStats(){
  return FXCollections.observableArrayList();
 }


 @Override
 public void initialize(URL url, ResourceBundle resourceBundle) {

  setSymb(data.getOpenPos().getSymb());
  readJsonObject();

  tblHOD.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("hod"));
  tblHOD.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
  tblHOD.setOnEditCommit(event -> {
   OpenPos item = event.getTableView().getItems().get(event.getTablePosition().getRow());
   item.setHod(event.getNewValue());
   openPos.setHod(event.getNewValue());
  });
  tblLOD.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("lod"));
  tblLOD.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
  tblLOD.setOnEditCommit(event -> {
   OpenPos item = event.getTableView().getItems().get(event.getTablePosition().getRow());
   item.setLod(event.getNewValue());
   openPos.setLod(event.getNewValue());
  });
  tblSymbStats.setEditable(true);

  tblTotSide.setCellValueFactory(new PropertyValueFactory<OpenPos, Character>("side"));
  tblTotUnits.setCellValueFactory(new PropertyValueFactory<OpenPos, Integer>("startUnits"));
  tblTotOpenPrice.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("openPrice"));
  tblTotClosePrice.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("closePrice"));
  tblAllTrades.setEditable(true);

  tblTradesSide.setCellValueFactory(new PropertyValueFactory<Trade, Character>("side"));
  tblTradesSide.setCellFactory(TextFieldTableCell.forTableColumn(new CharacterStringConverter()));
  tblTradesSide.setOnEditCommit(event -> {
   // Get the edited item
   Trade editedTrade = event.getRowValue();
   // Get the new value from the event
   char newSide = event.getNewValue();
   // Update the Trade object with the new value
   editedTrade.setSide(Character.toUpperCase(newSide));
   changesInTblAllTrades();
   tblAllTrades.refresh();
  });


  tblTradesUnits.setCellValueFactory(new PropertyValueFactory<>("units"));
  tblTradesUnits.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
  tblTradesUnits.setOnEditCommit(event -> {
   // Get the edited item
   Trade editedTrade = event.getRowValue();
   // Get the new value from the event
   int newUnits = event.getNewValue();
   // Update the Trade object with the new value
   editedTrade.setUnits(newUnits);
   changesInTblAllTrades();
   // Refresh the table to reflect the changes
   tblAllTrades.refresh();
  });

  tblTradesPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
  tblTradesPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
  tblTradesPrice.setOnEditCommit(event -> {
   // Get the edited item
   Trade editedTrade = event.getRowValue();
   // Get the new value from the event
   double newPrice = event.getNewValue();
   // Update the Trade object with the new value
   editedTrade.setPrice(newPrice);
   changesInTblAllTrades();

   // Refresh the table to reflect the changes
   tblAllTrades.refresh();
  });

  tblTradesDate.setCellValueFactory(new PropertyValueFactory<>("date"));
  tblTradesDate.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
  tblTradesDate.setOnEditCommit(event -> {
   // Get the edited item
   Trade editedTrade = event.getRowValue();
   // Get the new value from the event as a String
   String newDate = event.getNewValue();
   // Update the Trade object with the new String value
   editedTrade.setDate(newDate);
   // Refresh the table to reflect the changes
   tblAllTrades.refresh();
  });

  tblAllTrades.setItems(tblAllTrades());

  tblSum.setEditable(true);
  tblSymbStats.setItems(tblSymbStats());
  tblSum.setItems(tblSum());
  setLabel();
  addToTables();
  setUpSymbTbl();
  setStatusLabels();

 }

 public void setStatusLabels()
 {
     for(int i = 0; i< openPos.getSide();i++)
      if(openPos.getUnitsLeft()==0)
      {
       lblStatus.setText("CLOSED");
       lblUnitsLeft.setVisible(false);
      }
     else
      {
       lblStatus.setText ("OPEN");
       lblUnitsLeft.setText("Units left: "+ openPos.getUnitsLeft());
       lblUnitsLeft.setVisible(true);
      }
 }
}

