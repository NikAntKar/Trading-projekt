package ConfirmWindow;

import InfoWindow.OpenPos;
import com.example.javafxtest.JsonFixer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.CharacterStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Trade {
    private char side;
    private double price;
    private int units;
    private String date;

    // Constructors, getters, and setters for the fields
    public Trade()
    {

    }
    public Trade(char side, double price, int units, String date) {
        this.side = side;
        this.price = price;
        this.units = units;
        this.date = date;
    }

    public char getSide() {
        return side;
    }

    public double getPrice() {
        return price;
    }

    public int getUnits() {
        return units;
    }

    public String getDate() {
        return date;
    }

    public void setSide(char side) {
        this.side = side;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Add getters and setters for each field here

    public static class ConfirmWindowController  implements Initializable {

     public ConfirmWindowController()
     {

     }


      OpenPos openPos;
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
     private Button btnBack;

      @FXML
      private Label txtR;

     private Stage stage;

     ArrayList<Trade> tradeList=new ArrayList<>();
     DataHolder data = DataHolder.getInstance();


     public void setOpenPos(OpenPos openPos) {
      this.openPos = openPos;
     }

      public void setLabel()
      {
       lblSymbID.setText(data.getOpenPos().getSymb());
      }
     public void setUpTradesTbl()
     {
      ObservableList<Trade> items = tblAllTrades.getItems();
      for(int i=0;i<openPos.getSides().size();i++)
      {
       tradeList.add(new Trade(openPos.getSides().get(i),openPos.getPrice().get(i),
               openPos.getUnits().get(i),openPos.getDate().get(i)));
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

      public void back(ActionEvent event) throws IOException
      {
       ArrayList<Character> holdSide = new ArrayList<>();
       ArrayList<Integer> holdUnits = new ArrayList<>();
       ArrayList<Double> holdPrice = new ArrayList<>();
       ArrayList<String> holdDate = new ArrayList<>();
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
       openPos.setOpenPrice(openPos.getPriceAtIndex(0));
       data.setOpenPos(openPos);
       JsonFixer json = new JsonFixer();
       json.update(openPos);
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxtest/openpositions-view.fxml"));
       Object root = loader.load();
       stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       Scene scene = new Scene((Parent) root, 860, 680);
       stage.setScene(scene);
       stage.show();
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
      setOpenPos(data.getOpenPos());

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
      tblTotClosePrice.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("avgPrice"));
      tblTotOpenPrice.setCellValueFactory(new PropertyValueFactory<OpenPos, Double>("closePrice"));
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

       // Refresh the table to reflect the changes
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
       // Refresh the table to reflect the changes
       tblAllTrades.refresh();
      });



      tblTradesPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
      tblTradesPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
      tblTradesPrice.setOnEditCommit(event -> {
       // Get the edited item
       Trade editedTrade = event.getRowValue();
       // Get the new value from the event
       double newUnits = event.getNewValue();
       // Update the Trade object with the new value
       editedTrade.setPrice(newUnits);
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
     }
    }
}
