package InfoWindow;

import Analyse.Portfolio;
import ConfirmWindow.ConfirmWindowController;
import ConfirmWindow.DataHolder;
import DataBase.DatabaseController;
import com.example.javafxtest.ApiCall;
import com.example.javafxtest.FormatterClass;
import com.example.javafxtest.JsonFixer;
import com.example.javafxtest.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OpenPosTblView extends TableView<OpenPos> {

    public OpenPosTblView(){
    FormatterClass f = new FormatterClass();
    Validation validation = new Validation();

    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static ArrayList<OpenPos> openPos = new ArrayList<OpenPos>();
    DataHolder data = DataHolder.getInstance();
    public ArrayList<OpenPos> getOpenPos() {
        return openPos;
    }
    FormatterClass f = new FormatterClass();
    Validation validation = new Validation();
    DatabaseController database = new DatabaseController();
    JsonFixer jsonFixer = new JsonFixer();
    public static void addPos(ArrayList<OpenPos> o)
    {
        openPos.addAll(o);
    }
    public void HandleAddNewPosExisting(TableView<OpenPos> tblOpenPos, PotentialPos posse, String priceF,
                                        String tickerF, String stopF, String unitsF, DatePicker dateColumn, char side, Label lblAdjR )
    {
        boolean exists = false;
        int foundAt =0;
        for(int i = 0; i<tblOpenPos.getItems().size();i++)
        {
            if(tblOpenPos.getItems().get(i).getSymb().equals(tickerF.toUpperCase()))
            {
                exists = true;
                foundAt = i;
            }
        }
        double price = Double.parseDouble(priceF);
        double stop = Double.parseDouble(stopF);
        double unitsP = Double.parseDouble(unitsF);
        int unitsI = (int) unitsP;
        double risk = 0;
        if(exists) {
            OpenPos o = tblOpenPos.getItems().get(foundAt);
            if (side == 'B')
            {
                risk = f.formatDoubleXX(price- stop);
            }
            else
            {
                risk = f.formatDoubleXX(stop - price);
            }
            double firstSum = o.getStartUnits() * o.getOpenPrice();
            double secondSum = price * unitsI;
            int sumUnits = o.getStartUnits() + unitsI;
            double result = (firstSum + secondSum) / sumUnits;
            o.setOpenPrice(result);
            o.setStartUnits(sumUnits);
            o.setUnitsLeft(o.getUnitsLeft() + unitsI);
            o.setStartUnits(unitsI + o.getStartUnits());
        }
        else
        {
            if (side == 'B')
            {
                risk = f.formatDoubleXX(price- stop);
            }
            else
            {
                risk = f.formatDoubleXX(stop - price);
            }
            double totalRisk = risk * unitsI;
            ObservableList<OpenPos> items = tblOpenPos.getItems();
            int id = items.size()+1;
            String adjRString = lblAdjR.getText();
            adjRString = adjRString.replace("%", "");
            double adjR = Double.parseDouble(adjRString);
            double currentValue = database.getPortFolioCurrentValue();
            double currentR = f.formatDouble(currentValue *0.01);
            double takenR = f.formatDoubleXX(totalRisk/currentR);
            OpenPos o = new OpenPos(id, posse.getSymb(), price, unitsI, posse.getLod(), risk,totalRisk,
                    posse.getHod(), stop, side, posse.getAdr(), posse.getRangeVAtr(), posse.getAtr(), dateColumn, currentR, takenR, adjR);
            openPos.add(o);
            items.add(o);
            jsonFixer.add(o);
        }
    }
    public void removeOpenPos(TableView<OpenPos> openPosTbl)
    {
        jsonFixer.remove(openPosTbl);
        openPosTbl.refresh();
    }
    public void HandleAddNewPosNew(TableView<OpenPos> tblOpenPos, String tickerF, String price, String stop,
                                   String units, DatePicker datePickere, char side, Label lblAdjR )
    {
        FormatterClass f = new FormatterClass();
        ApiCall apicallen = new ApiCall(tickerF);
        double lod = apicallen.getLod();
        double hod = apicallen.getHod();
        double adr = apicallen.getAdr();
        double atr = apicallen.getAtr();
        double priceP = Double.parseDouble(price);
        double stopP = Double.parseDouble(stop);
        double unitsP = Double.parseDouble(units);
        int unitsI = (int)unitsP;
        double risk = 0;
        double rangeVAtr;
        if(side=='B')
        {
            rangeVAtr = f.formatDoubleXX((priceP-lod)/atr);
            risk = f.formatDoubleXX(priceP - stopP);
        }
        else
        {
            rangeVAtr = f.formatDoubleXX((hod-priceP)/atr);
            risk = f.formatDoubleXX( stopP-priceP);
        }
        double totalRisk = risk * unitsI;
        String adjRString = lblAdjR.getText();
        adjRString = adjRString.replace("%", "");
        double adjR = Double.parseDouble(adjRString);
        double currentValue = database.getPortFolioCurrentValue();
        double currentR = f.formatDouble(currentValue *0.01);
        double takenR = f.formatDoubleXX(totalRisk/currentR);
        int index = tblOpenPos.getItems().size()+1;
        OpenPos o = new OpenPos(index, tickerF.toUpperCase(), priceP, unitsI, lod, risk,totalRisk,
                hod, stopP, side, adr, rangeVAtr,  atr, datePickere, currentR, takenR, adjR);
        openPos.add(o);
        ObservableList<OpenPos> items = tblOpenPos.getItems();
        items.add(o);
        jsonFixer.add(o);
    }
    public void HandleAddToOpenPos(TableView<OpenPos> tblOpenPos, OpenPos openPos, String priceF,
                                   String tickerF, String stopF, String unitsF, DatePicker dateColumn, char side )
    {
        int size = tblOpenPos.getItems().size();
        try {
            double price = Double.parseDouble(priceF);
            double stop = Double.parseDouble(stopF);
            double unitsP = Double.parseDouble(unitsF);
            int unitsI = (int) unitsP;
            openPos.addSide(side);
            openPos.addPrice(price);
            LocalDate localDate = dateColumn.getValue();
            String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            openPos.addDate(formattedDate);
            openPos.addUnits(unitsI);
            openPos.stops.add(stop);
            double currenOpenPrice = openPos.getOpenPrice();
            int currentUnits = openPos.getUnitsLeft();
            double newOpenPrice = ((currenOpenPrice*currentUnits) + (price*unitsP)) / (currentUnits + unitsI);
            openPos.setOpenPrice(f.formatDoubleXXX(newOpenPrice));
            double calcNewStop = stop;
            if(stop != openPos.getStop())
            {
                calcNewStop = ((openPos.getStop()*currentUnits) + (stop*unitsI)) / (currentUnits + unitsI);
                openPos.setStop(f.formatDoubleXXX(calcNewStop));
            }
            int newUnits = unitsI + openPos.getUnitsLeft();
            openPos.setStartUnits(newUnits);
            openPos.setThirdSell(newUnits/3);
            openPos.setCurrentPrice(price);
            openPos.setUnitsLeft(currentUnits + unitsI);
            if(openPos.getSide() == 'B')
            {
                double newRisk = f.formatDoubleXX(newOpenPrice - calcNewStop);
                openPos.setThreR(newOpenPrice + (newRisk*3));
                openPos.setSixR(newOpenPrice + (newRisk*6));
                openPos.setNineR(newOpenPrice + (newRisk*9));

            }
            else{
                double newRisk = f.formatDoubleXX(newOpenPrice - calcNewStop);
                openPos.setThreR(newOpenPrice + (newRisk*3-1));
                openPos.setSixR(newOpenPrice + (newRisk*6-1));
                openPos.setNineR(newOpenPrice + (newRisk*9-1));
            }

            for(int i = 0; i< size;i++)
            {
                tblOpenPos.getItems().remove(0);
            }
            jsonFixer.update(openPos);
            jsonFixer.loadToView(tblOpenPos);

        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + priceF);
        }
    }

    public void setRightId()
    {
        int id =0;
        for(OpenPos o: openPos)
        {
            id++;
            o.setId(id);
            jsonFixer.update(o);
        }
    }


    public void HandleExistingPos(TableView<OpenPos> tblOpenPos,  ChoiceBox<String> symbols,
                                  TextField priceF, TextField unitsF, DatePicker datePicker,
                                    Label symbTxt, Label unitsTxt, Label priceTxt, Label dateTxt,
                                    ToggleButton tglBuy, ToggleButton tglSell, Button btnExecute)
    {
        btnExecute.setVisible(true);
        tglBuy.setVisible(true);
        tglSell.setVisible(true);
        symbTxt.setVisible(true);
        unitsTxt.setVisible(true);
        priceTxt.setVisible(true);
        dateTxt.setVisible(true);
        symbols.setVisible(true);
        priceF.setVisible(true);
        unitsF.setVisible(true);
        datePicker.setVisible(true);
        LocalDate today = LocalDate.now();
        datePicker.setValue(today);
        OpenPos o = tblOpenPos.getSelectionModel().getSelectedItem();
        symbols.getItems().clear();
        for(int i =0; i<tblOpenPos.getItems().size();i++)
            symbols.getItems().add(tblOpenPos.getItems().get(i).getSymb());
        symbols.setValue(o.symb);
        priceF.setText(String.valueOf(o.getOpenPrice()));
        unitsF.setText(String.valueOf(o.getThirdSell()));
    }



        // WHY USE ?????
//    public void updateTbl(TableView<OpenPos> tblOpenPos )
//    {
//        double holdStop =0;
//        double holdPrice =0;
//        for(int i = 0; i< tblOpenPos.getItems().size(); i++){
//            OpenPos o = tblOpenPos.getItems().get(i);
//
//            holdStop= o.getStop();
//            holdPrice = o.getOpenPrice();
//            double risk = 0;
//            if(openPos.get(0).getSide()== 'B')
//            {
//                risk = holdPrice - holdStop;
//                o.setThreR(f.formatDoubleXX(o.getOpenPrice() + (risk) * 3));
//                o.setSixR(f.formatDoubleXX(o.getOpenPrice()  +(risk) * 6));
//                o.setNineR(f.formatDoubleXX(o.getOpenPrice()  +(risk) * 9));
//                openPos.get(i).setThreR(f.formatDoubleXX(o.getOpenPrice()  + (risk) * 3));
//                openPos.get(i).setSixR(f.formatDoubleXX(o.getOpenPrice()  + (risk) * 6));
//                openPos.get(i).setNineR(f.formatDoubleXX(o.getOpenPrice()  + (risk) * 9));
//                tblOpenPos.refresh();
//            }
//            else
//            {
//                risk = holdStop - holdPrice;
//                o.setThreR(f.formatDoubleXX(o.getOpenPrice()  + (risk) * 3));
//                o.setSixR(f.formatDoubleXX(o.getOpenPrice()  +(risk) * 6));
//                o.setNineR(f.formatDoubleXX(o.getOpenPrice()  +(risk) * 9));
//                openPos.get(i).setThreR(f.formatDoubleXX(o.getOpenPrice()  + (risk) * 3));
//                openPos.get(i).setSixR(f.formatDoubleXX(o.getOpenPrice()  + (risk) * 6));
//                openPos.get(i).setNineR(f.formatDoubleXX(o.getOpenPrice()  + (risk) * 9));
//            }
//        }
//
//    }
    public void setUpOpenPosLabels(TableView<OpenPos> openPostbl, Label lblWorstCase, Label lblOpenR)
    {
        double sumOpenR =0;
        double worstCase =0;
        for(OpenPos items: openPostbl.getItems())
        {
            sumOpenR += items.getOpenR();
            worstCase += items.worstCase;
        }
        worstCase = f.formatDoubleXX(worstCase);
        sumOpenR = f.formatDoubleXX(sumOpenR);
        lblOpenR.setText(f.formatDoubleXX(sumOpenR) + "R");
        lblWorstCase.setText(worstCase +"R");
    }

    // work in progress
    public void handleEditStop(
            TableColumn<OpenPos, Double> eventColumn,
            TableColumn.CellEditEvent<OpenPos, Double> event,
            TableView <OpenPos> tblOpenPos
    )
    {
        OpenPos o = null;
        OpenPos item = event.getRowValue();
        String symb = item.getSymb();
        for (OpenPos openPos : openPos) {
            if (openPos.getSymb().equals(symb)) {
                o = openPos;
                break;
            }
        }
        double newStop = event.getNewValue();
        item.setStop(newStop);
        o.setStop(newStop);
        double worstCase = f.formatDoubleXX(handleWorstCase(o));
        o.setWorstCase(worstCase);
        item.setWorstCase(worstCase);
        jsonFixer.update(o);
        tblOpenPos.refresh();
    }
    public double handleOpenR(OpenPos o)
    {
        return 0;
    }
    public double handleWorstCase(OpenPos o)
    {
        Portfolio p = new Portfolio();
        double currentOneR = p.getOneR();
        double currentRisk;
        if(o.getSide() =='B')
            currentRisk = (o.getCurrentPrice()-o.getStop()) * o.getUnitsLeft();
        else
            currentRisk= (o.getStop()-o.getCurrentPrice()) * o.getUnitsLeft();
        return currentRisk/currentOneR;
    }
    public void refresh(TableView<OpenPos> tblOpenPos)
    {
        ApiCall apiCall = new ApiCall();
        Portfolio p = new Portfolio();
        double currentPrice;
        String symb;
        int size = tblOpenPos.getItems().size();
        double openR = 0;
        for(int i = 0; i< size;i++)
        {
            OpenPos o = null;
            symb = tblOpenPos.getItems().get(i).getSymb();
            for (OpenPos openPos : openPos) {
                if (openPos.getSymb().equals(symb)) {
                    o = openPos;
                    break;
                }
            }
            currentPrice = apiCall.GetCurrentPrice(symb);
            if(o.getSide() == 'B')
            {
                openR = f.formatDoubleXX((currentPrice - o.getOpenPrice()) / o.getRisk());
            }
            else
                openR = f.formatDoubleXX((o.getOpenPrice() - currentPrice) / o.getRisk());
            o.setCurrentPrice(f.formatDoubleXXX(currentPrice));
            o.setOpenR(openR);
            jsonFixer.update(openPos.get(i));
        }
        for(int i = 0; i< size;i++)
        {
            tblOpenPos.getItems().remove(0);
        }
        jsonFixer.loadToView(tblOpenPos);
    }


    public void setMarketInfo(Label lblQQQ, Label lblSPY, Label lblIWM,
                              Circle iconQqqma10, Circle iconQqqma20,Circle iconQqq10Over20,
                              Circle iconSpyma10, Circle iconSpyma20, Circle iconSpy10Over20,
                              Circle iconIWMma10, Circle iconIwmma20, Circle iconIwm10Over20 )
    {
       ApiCall apicall = new ApiCall();
        LocalDate date = LocalDate.now();
        String stringDate = date.toString();
        apicall.getSetAtrMa("qqq", stringDate);
        double qqqMa10 = apicall.getMa10();
        double qqqMa20 = apicall.getMa20();
        double qqqPrice = apicall.GetCurrentPrice("qqq");
        double qqqAtr = apicall.getAtr();
        if(qqqPrice < qqqMa10)
            iconQqqma10.setStyle("-fx-fill: #ffc233");
        else
            iconQqqma10.setStyle("-fx-fill: #388e3c;");
        if(qqqPrice < qqqMa20)
            iconQqqma20.setStyle("-fx-fill: #ffc233");
        else
            iconQqqma20.setStyle("-fx-fill: #388e3c;");
        if(qqqMa10 < qqqMa20)
            iconQqq10Over20.setStyle("-fx-fill: #ffc233");
        else
            iconQqq10Over20.setStyle("-fx-fill: #388e3c;");

        apicall.getSetAtrMa("SPY", stringDate);
        double spyMa10 = apicall.getMa10();
        double spyMa20 = apicall.getMa20();
        double spyPrice = apicall.GetCurrentPrice("spy");
        double spyAtr = apicall.getAtr();
        if(spyPrice < spyMa10)
            iconSpyma10.setStyle("-fx-fill: #ffc233");
        else
            iconSpyma10.setStyle("-fx-fill: #388e3c;");
        if(spyPrice < spyMa20)
            iconSpyma20.setStyle("-fx-fill: #ffc233");
        else
            iconSpyma20.setStyle("-fx-fill: #388e3c;");
        if(spyMa10 < spyMa20)
            iconSpy10Over20.setStyle("-fx-fill: #ffc233");
        else
            iconSpy10Over20.setStyle("-fx-fill: #388e3c;");

        apicall.getSetAtrMa("iwm", stringDate);
        double iwmMa10 = apicall.getMa10();
        double iwmMa20 = apicall.getMa20();
        double IWMPrice = apicall.GetCurrentPrice("iwm");
        double iwmAtr = apicall.getAtr();
        if(IWMPrice < iwmMa10)
            iconIWMma10.setStyle("-fx-fill: #ffc233");
        else
            iconIWMma10.setStyle("-fx-fill: #388e3c;");
        if(IWMPrice < iwmMa20)
            iconIwmma20.setStyle("-fx-fill: #ffc233");
        else
            iconIwmma20.setStyle("-fx-fill: #388e3c;");
        if(iwmMa10 < iwmMa20)
            iconIwm10Over20.setStyle("-fx-fill: #ffc233");
        else
            iconIwm10Over20.setStyle("-fx-fill: #388e3c;");

        double QQQdist = f.formatDoubleXX(((qqqPrice-qqqMa10)/qqqAtr));
        double SPYdist = f.formatDoubleXX(((spyPrice-spyMa10)/spyAtr));
        double IWMdist = f.formatDoubleXX(((IWMPrice-iwmMa10)/iwmAtr));
        lblQQQ.setText(String.valueOf(QQQdist)+" ATR");
        lblSPY.setText(String.valueOf(SPYdist) +" ATR");
        lblIWM.setText(String.valueOf(IWMdist) + " ATR");
    }

    public void handleOpenConfirmWindow( TableView<OpenPos> tblOpen, ActiveController controller)throws IOException
    {
        OpenPos o = tblOpen.getSelectionModel().getSelectedItem();
        if(o == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected item");
            alert.setContentText("No item are selected");
            alert.setHeaderText(null); // Optional header text
            alert.showAndWait();        }
        else
        {
            data.setOpenPos(tblOpen.getSelectionModel().getSelectedItem());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxtest/confirmWindow.fxml"));
            Parent root = loader.load();
            ConfirmWindowController c = loader.getController();
            c.setParentController(controller);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
    public void openPositionExecution(TableView<OpenPos> tblOpenPos, ChoiceBox<String> symbols,
                                      TextField priceF, TextField unitsF, DatePicker datePicker,
                                      Label symbTxt, Label unitsTxt, Label priceTxt, Label dateTxt,
                                      ToggleButton tglBuy, ToggleButton tglSell, Button btnExecute, ActiveController controller) throws IOException {
        jsonFixer.read();
        LocalDate localDate = datePicker.getValue();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        int foundAt = 0;
        boolean found = false;
        int index = 0;
        while( index <tblOpenPos.getItems().size() || !found) {
            String symLoop ="";
            symLoop = tblOpenPos.getItems().get(index).getSymb();
            if(symLoop.equals(symbols.getValue())){
                foundAt = index;
            }
            found = true;
            index ++;
        }
        String unitsS = unitsF.getText();
        int unitsI = Integer.parseInt(unitsS);
        OpenPos openPos = tblOpenPos.getItems().get(foundAt);
        int newUnits = 0;
        Double price = Double.parseDouble(priceF.getText());
        if(openPos.getSide() =='B' && tglSell.isSelected())
        {
            newUnits = openPos.getUnitsLeft() - unitsI;
            if(validation.notAMinusValue(newUnits)) {
                openPos.setUnitsLeft(newUnits);
                openPos.addDate(formattedDate);
                openPos.addUnits(unitsI);
                openPos.addPrice(price);
                openPos.addSide('S');
                openPos.setCurrentPrice(price);
            }
        }
        if(openPos.getSide() =='B' && tglBuy.isSelected())
        {
            double avgPrice = openPos.getOpenPrice();
            int oldUnits =openPos.getUnitsLeft();
            double oldAvg =avgPrice * oldUnits;
            double avgIn = price * unitsI;
            newUnits = openPos.getUnitsLeft()+unitsI;
            double avg = (oldAvg + avgIn) /newUnits;
            openPos.setOpenPrice(f.formatDoubleXX(avg));
            newUnits = unitsI + openPos.getUnitsLeft();
            openPos.setUnitsLeft(newUnits);
            openPos.addDate(formattedDate);
            openPos.addUnits(unitsI);
            openPos.addPrice(price);
            openPos.addSide('B');
            openPos.setCurrentPrice(price);
            openPos.setStartUnits(newUnits);

        }
        if(openPos.getSide() == 'S' && tglBuy.isSelected())
        {
            newUnits = openPos.getUnitsLeft() - unitsI;
            if(validation.notAMinusValue(newUnits)) {
            openPos.setUnitsLeft(newUnits);
            openPos.addDate(formattedDate);
            openPos.addUnits(unitsI);
            openPos.addPrice(price);
            openPos.addSide('B');
            openPos.setCurrentPrice(price);
            }
        }
        if(openPos.getSide() == 'S' && tglSell.isSelected())
        {
            double avgPrice = openPos.getOpenPrice();
            int oldUnits =openPos.getUnitsLeft();
            double oldAvg =avgPrice * oldUnits;
            double avgIn = price * unitsI;
            newUnits = openPos.getUnitsLeft()+unitsI;
            double avg = (oldAvg + avgIn) /newUnits;
            openPos.setOpenPrice(f.formatDoubleXX(avg));
            newUnits = unitsI + openPos.getUnitsLeft();
            openPos.setUnitsLeft(newUnits);
            openPos.setUnitsLeft(newUnits);
            openPos.addDate(formattedDate);
            openPos.addUnits(unitsI);
            openPos.addPrice(price);
            openPos.addSide('S');
            openPos.setCurrentPrice(price);
            openPos.setStartUnits(newUnits);
        }
        ArrayList<Character> holdSide = openPos.getSides();
        double avg = 0;
        double totalUnits = 0;
        if(openPos.getSide()== 'B')
        {
            for(int i = 0; i<openPos.getSides().size();i++)
            {
                if(holdSide.get(i) == 'S')
                {
                    int holdUnits;
                    double holdPrice;
                    holdPrice= openPos.getPriceAtIndex(i);
                    holdUnits= openPos.getUnitsAtIndex(i);
                    totalUnits = totalUnits+ holdUnits;
                    avg = avg + (holdPrice * holdUnits);
                }
            }
            avg = avg/totalUnits;
            openPos.setClosePrice(f.formatDoubleXXX(avg));
        }

        if(openPos.getSide() == 'B')
            openPos.setOpenR(f.formatDoubleX((openPos.getCurrentPrice() - openPos.getOpenPrice())/openPos.getRisk()));
        else
            openPos.setOpenR(f.formatDoubleX((openPos.getOpenPrice()-openPos.getCurrentPrice()) / openPos.getRisk()));
        System.out.println(openPos.getOpenR());
        btnExecute.setVisible(false);
        tglBuy.setVisible(false);
        tglSell.setVisible(false);
        symbTxt.setVisible(false);
        unitsTxt.setVisible(false);
        priceTxt.setVisible(false);
        dateTxt.setVisible(false);
        symbols.setVisible(false);
        priceF.setVisible(false);
        unitsF.setVisible(false);
        datePicker.setVisible(false);
        tblOpenPos.refresh();
        jsonFixer.update(openPos);
        if(openPos.getUnitsLeft() == 0)
        {
            data.setOpenPos(openPos);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxtest/confirmWindow.fxml"));
            Parent root = loader.load();
            ConfirmWindowController c = loader.getController();
            c.setParentController(controller);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}

