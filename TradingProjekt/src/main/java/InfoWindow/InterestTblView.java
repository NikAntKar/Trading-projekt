package InfoWindow;
import DataBase.DatabaseController;
import com.example.javafxtest.ApiCall;
import com.example.javafxtest.FormatterClass;
import com.example.javafxtest.Validation;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InterestTblView extends TableView<PotentialPos> {

    public ArrayList<PotentialPos> getPotentialPos() {
        return potentialPos;
    }

    public InterestTblView() {}

    FormatterClass f = new FormatterClass();
    Validation validate = new Validation();
    DatabaseController database = new DatabaseController();
    OpenPos openPos = new OpenPos();

    private ArrayList<PotentialPos> potentialPos = new ArrayList<PotentialPos>();


    public void handleAddInterestPos(TextField lblTickerRT, ToggleButton tglLong, TableView<PotentialPos> tblInterest, Label lblAdjR) {

        String adjRString = lblAdjR.getText();
        adjRString = adjRString.replace("%", "");
        double adjR = Double.parseDouble(adjRString);
        double currentValue = database.getPortFolioCurrentValue();
        double dolRisk = f.formatDoubleXX(adjR *currentValue)/100;
        String symb = lblTickerRT.getText().toUpperCase();
        ApiCall apicall = new ApiCall(symb);
        double price = f.formatDoubleXX(apicall.GetCurrentPrice(symb));
        double hod = f.formatDoubleXX(apicall.getHod());
        double lod = f.formatDoubleXX(apicall.getLod());
        double adr = f.formatDoubleXX(apicall.getAdr());
        double adr5min = f.formatDoubleXX(apicall.Get5MinAdr(symb, 14));
        double atr = apicall.getAtr();
        double threeRtarget;
        double stop;
        int units = 0;
        char side;
        double risk =0;
        if (tglLong.isSelected()) {
            risk = f.formatDoubleXX((price - lod) + adr5min);
            units = (int) Math.round(dolRisk / risk);
            side = 'B';
            stop = f.formatDoubleXX(lod - adr5min);
            threeRtarget = f.formatDoubleXX(price + (risk * 3));
        } else {
            risk = f.formatDoubleXX((hod - price) + adr5min);
            units = (int) Math.round(dolRisk / risk);
            side = 'S';
            stop = f.formatDoubleXX(hod + adr5min);
            threeRtarget = f.formatDoubleXX(price - (risk * 3));
        }
        PotentialPos posse = new PotentialPos(symb, price, hod, lod, risk, units, adr, adr5min, atr, threeRtarget, side, stop);
        ObservableList<PotentialPos> items = tblInterest.getItems();
        items.add(posse);
        potentialPos.add(posse);
        lblTickerRT.clear();
    }

    public void handleEditInView(
            TableColumn<PotentialPos, Double> eventColumn,
            TableColumn.CellEditEvent<PotentialPos, Double> event,
            TableView<PotentialPos> tblInterest, Label lblAdjR
    ) {
        double newValue = event.getNewValue();
        TableColumn<PotentialPos, Double> editedColumn = eventColumn;
        PotentialPos item = event.getRowValue();
        String columnName = editedColumn.getText().toUpperCase();
         if (columnName.equals("PRICE")) {
                item.setPrice(newValue);
            } else if (columnName.equals("STOP")) {
                item.setStop(newValue);
            } else if (columnName.equals("HOD")) {
                item.setHod(newValue);
            } else if (columnName.equals("LOD")) {
                item.setLod(newValue);
            } else if (!columnName.equals("PRICE") && !columnName.equals("STOP")) {
                System.err.println("Can't figure out where this call comes from");
            }
            // Recalculate "risk" and "units" for the item
            double price = item.getPrice();
            double stop = item.getStop();
            double side = item.getSide();
            double risk;
            if (side == 'B') {
                risk = price - stop;
            } else {
                risk = stop - price;
            }
            double fRisk = f.formatDoubleXX(risk);
            item.setRisk(fRisk);
            String adjRString = lblAdjR.getText();
            adjRString = adjRString.replace("%", "");
            double adjR = Double.parseDouble(adjRString);
            double currentValue = database.getPortFolioCurrentValue();
            double dolRisk = f.formatDoubleXX(adjR *currentValue)/100;
            int units = (int) Math.floor(dolRisk / fRisk);
            item.setUnits(units);
            tblInterest.refresh();
    }

    public PotentialPos handleMoveToExecute(TableView<PotentialPos> tblInterest, TextField symbF, TextField priceF,
                                            TextField unitsF, TextField stopF, ToggleButton tglBuy, ToggleButton tglSell) {
        PotentialPos p = tblInterest.getSelectionModel().getSelectedItem();
        // Double-click detected
        PotentialPos selectedItem = tblInterest.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Handle the double-click action for the selected item
            symbF.setText(p.getSymb());
            priceF.setText(String.format("%.2f", p.getPrice()));
            stopF.setText(String.format("%.2f", p.getStop()));
            if (p.getSide() == 'B') {
                tglBuy.setSelected(true);
            } else {
                tglSell.setSelected((true));
            }
        }
        return p;
    }

    public void handleRemovePos(TableView<PotentialPos> tblInterest) {
        PotentialPos posse = tblInterest.getSelectionModel().getSelectedItem();
        if (posse != null) {
            int index = tblInterest.getItems().indexOf(posse);
            tblInterest.getItems().remove(index);
            potentialPos.remove(index);
            tblInterest.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected item");
            alert.setContentText("No item are selected");
            alert.setHeaderText(null); // Optional header text
            alert.showAndWait();
        }
    }
    public boolean tickerExists(String ticker, OpenPos openPos)
    {
        boolean exists = false;
            if (openPos.getSymb().equals(ticker))
                exists = true;
        return exists;
    }


    public void handleUnitsField(TableView<OpenPos> openPosTbl, TextField tickerField, TextField priceField,
                                 TextField stopField, TextField unitsField, Label lblNewRisk, ToggleButton tglBuy,
                                    Label lbl3RTarget, Label lblMaxUnits, Label lblMinUnits, Label lblExists, Circle iconRisk,
                                double risk)
    {
        if(validate.CheckNumberTextfields(stopField) && validate.CheckNumberTextfields(priceField))
        {
            String tickerF = tickerField.getText().toUpperCase();
            String priceF = priceField.getText().toUpperCase();
            String stopF = stopField.getText();
            String unitsF = unitsField.getText();
            boolean exists = false;
            if(!unitsF.isEmpty() && !tickerF.isEmpty()) {
                ArrayList<OpenPos> posList = new ArrayList<OpenPos>(openPosTbl.getItems());
                openPos = posList.stream().filter(pos -> pos.getSymb().equals(tickerF)).findAny().orElse(null);
            }
            if (!priceF.isEmpty() && !stopF.isEmpty()) {
                double price = Double.parseDouble(priceF);
                double stop = Double.parseDouble(stopF);
                int units = Integer.parseInt(unitsF);
                double riskPerUnit = 0;
                double totalRisk = 0;
                database.getPortFolioCurrentValue();
                double currentOneR = database.getPortFolioCurrentValue() * 0.01;
                double rForPos = 0;
                int maxUnits = 0;
                int minUnits = 0;
                if (openPos == null) {
                    char side;
                    if(tglBuy.isSelected())
                        side ='B';
                    else
                        side = 'S';
                    riskPerUnit = calcRiskPerUni(price, stop,side );
                    totalRisk = riskPerUnit * units;
                    rForPos = f.formatDoubleXX(totalRisk / currentOneR);
                    lblNewRisk.setText("Risk " + rForPos + "%");
                    maxUnits = calcMaxUnits(risk, riskPerUnit, tglBuy);
                    minUnits = calcMinUnits(risk, riskPerUnit, tglBuy);
                } else {

                    boolean profitStop = isInTheMoneyStop(openPos.getOpenPrice(), openPos.getStop(), openPos.getSide());
                    if(!profitStop) {
                        riskPerUnit = calcRiskPerUni(openPos.getOpenPrice(), openPos.getStop(), openPos.getSide());
                        maxUnits = calcMaxUnits(risk, riskPerUnit, tglBuy) - openPos.getUnitsLeft();
                        minUnits = calcMinUnits(risk, riskPerUnit, tglBuy) - openPos.getUnitsLeft();
                    }
                    else {
                        maxUnits = calcMaxUnits(risk, riskPerUnit, tglBuy);
                        minUnits = calcMinUnits(risk, riskPerUnit, tglBuy);
                    }
                    double openRisk = riskPerUnit * openPos.getUnitsLeft();
                    double oldRiskPerUni = calcRiskPerUni(openPos.getOpenPrice(), openPos.getStop(), openPos.getSide());
                    double newRiskPerUni = calcRiskPerUni(price, stop, openPos.getSide());
                    units = (int) ((currentOneR - openRisk) / newRiskPerUni);

                    double newRisk;
                    double currentRisk = 0;
                    double totalR = 0;
                    if (openPos.getOpenPrice() > openPos.getStop())
                        currentRisk = (openPos.getOpenPrice() - openPos.getStop()) * openPos.getUnitsLeft();
                    newRisk = (price - stop) * units;
                    totalRisk = newRisk + currentRisk;
                    totalR = f.formatDoubleXX(totalRisk / currentOneR);
                    lblNewRisk.setText("Risk " + totalR + "%");
                    maxUnits = calcMaxUnits(risk, riskPerUnit, tglBuy);
                    minUnits = calcMinUnits(risk, riskPerUnit, tglBuy);
                    lblNewRisk.setVisible(true);
                }
                if(units <= maxUnits && units >= minUnits)
                    iconRisk.setStyle("-fx-fill: #388e3c;");
                else
                    iconRisk.setStyle("-fx-fill: #e53935");
            }
        }
    }
    public void handleChangeInTickerField(TableView<OpenPos> openPosTbl, TextField ticker, Label lblExists, TextField stopField)
    {
        String tickerF = ticker.getText().toUpperCase();
        boolean exists = false;
        if(!tickerF.isEmpty()){
            ArrayList<OpenPos> openPosList = new ArrayList<>(openPosTbl.getItems());
            exists = openPosList.stream().anyMatch(pos -> pos.getSymb().equals(tickerF));
            if(!exists)
                lblExists.setVisible(false);
            else {
                lblExists.setVisible(true);
                Optional<Double> stopOptional = openPosList.stream().filter(pos -> pos.getSymb().equals(tickerF)).map(OpenPos::getStop).findAny();
                stopField.setText(String.valueOf(stopOptional.orElse(0.0)));
            }
        }
    }
    public double calcRiskPerUni(double price, double stop, char side)
    {
        if(side == 'B')
            return price - stop;
        else
            return stop - price;
    }
    public boolean isInTheMoneyStop(double openPrice, double stop, char side)
    {
        if(side == 'B')
            return openPrice < stop;
        else
            return openPrice > stop;
    }
    public void handlePriceAndStopFieldChanges(TableView<OpenPos> openPosTbl, TextField tickerField, TextField priceField,
                                               TextField stopField, TextField unitsField, ToggleButton tglBuy, double risk
                                              ,Label lblTreR, Label lblMaxUnits, Label lblMinUnits, Label lblRisk, Circle iconRisk)
    {
        String tickerF = tickerField.getText().toUpperCase();
        String priceF = priceField.getText().toUpperCase();
        String stopF = stopField.getText();
        if(!priceF.isEmpty() && !stopF.isEmpty()) {
            double currentValue = database.getPortFolioCurrentValue();
            double adjR = currentValue * (risk * 0.01);
            double calcUnitsDouble = 0;
            double price = Double.parseDouble(priceF);
            double stop = Double.parseDouble(stopF);
            double riskPerUnit = 0;
            char side;
            int unitsInt;
            if(tglBuy.isSelected())
                side = 'B';
            else
                side = 'S';
            ArrayList<OpenPos> openPosList = new ArrayList<>(openPosTbl.getItems());
            OpenPos o = openPosList.stream().filter(pos -> pos.getSymb().equalsIgnoreCase(tickerF)).findAny().orElse(null);
            int maxUnits;
            int minUnits;
            if(o == null){
                riskPerUnit = calcRiskPerUni(price, stop, side);
                calcUnitsDouble = adjR / (riskPerUnit);
                unitsInt = (int) Math.round(calcUnitsDouble);
                handleThreeRLabel(lblTreR, tglBuy, price, riskPerUnit);
                maxUnits = calcMaxUnits(risk, riskPerUnit, tglBuy);
                minUnits = calcMinUnits(risk, riskPerUnit, tglBuy);
                handleRiskLabel(lblRisk, unitsInt, riskPerUnit, iconRisk, maxUnits, minUnits);
            }
            else{
                double currentR = currentValue * (risk * 0.01);
                boolean profitStop = isInTheMoneyStop(o.getOpenPrice(), o.getStop(), o.getSide());
                if(!profitStop) {
                    riskPerUnit = calcRiskPerUni(o.getOpenPrice(), o.getStop(), o.getSide());
                    maxUnits = calcMaxUnits(risk, riskPerUnit, tglBuy) - o.getUnitsLeft();
                    minUnits = calcMinUnits(risk, riskPerUnit, tglBuy) - o.getUnitsLeft();
                }
                else {
                    maxUnits = calcMaxUnits(risk, riskPerUnit, tglBuy);
                    minUnits = calcMinUnits(risk, riskPerUnit, tglBuy);
                }
                double openRisk = riskPerUnit * o.getUnitsLeft();
                double oldRiskPerUni = calcRiskPerUni(o.getOpenPrice(), o.getStop(), o.getSide());
                double newRiskPerUni = calcRiskPerUni(price, stop, o.getSide());
                unitsInt = (int) ((currentR - openRisk) / newRiskPerUni);
                riskPerUnit = ((o.getUnitsLeft() * oldRiskPerUni) + (newRiskPerUni * unitsInt)) /(o.getUnitsLeft() + unitsInt);
                handleThreeRLabel(lblTreR, tglBuy, price, newRiskPerUni);
                handleRiskLabel(lblRisk, (unitsInt + o.getUnitsLeft()), riskPerUnit, iconRisk, maxUnits, minUnits);
            }
            lblMaxUnits.setText("Max " + maxUnits);
            lblMinUnits.setText("Min " + minUnits);
            unitsField.setText(Integer.toString(unitsInt));
            lblTreR.setVisible(true);
            lblMaxUnits.setVisible(true);
            lblMinUnits.setVisible(true);
            lblRisk.setVisible(true);
            iconRisk.setVisible(true);
        }
        else
        {
            lblMaxUnits.setVisible(false);
            lblMinUnits.setVisible(false);
            lblTreR.setVisible(false);
            lblRisk.setVisible(false);
            iconRisk.setVisible(false);
        }
    }
    public void handleMaxMinUnitsLabel(double risk, Label lblMaxUnits, Label lblMinUnits, ToggleButton tglBuy, double riskPerUnit)
    {
        double currentValue = database.getPortFolioCurrentValue();
        double maxAdjr = risk + 0.5;
        double minAdjr = risk;
        if(risk > 0.25 && risk < 1)
            minAdjr = risk - 0.25;
        else if (risk > 1)
            minAdjr = risk - 0.5;
        double maxRisk = currentValue * (maxAdjr * 0.01);
        double minRisk = currentValue * (minAdjr * 0.01);
        int maxUnits;
        int minUnits;
        if(tglBuy.isSelected())
        {
            maxUnits = (int) Math.round(maxRisk / (riskPerUnit));
            minUnits = (int) Math.round(minRisk / (riskPerUnit));
        }
        else
        {
            maxUnits = (int) Math.round(maxRisk / (riskPerUnit));
            minUnits = (int) Math.round(minRisk / (riskPerUnit));
        }
        System.out.println("maxRisk " + maxRisk);
        System.out.println("riskPerUnit " + riskPerUnit);

        lblMaxUnits.setText("Max " + maxUnits);
        lblMinUnits.setText("Min " + minUnits);
    }
    public int calcMaxUnits(double risk, double riskPerUnit, ToggleButton tglBuy){
        double currentValue = database.getPortFolioCurrentValue();
        double maxAdjr = risk + 0.5;
        double maxRisk = currentValue * (maxAdjr * 0.01);
        int maxUnits;
        maxUnits = (int) Math.round(maxRisk / (riskPerUnit));

        return maxUnits;
    }
    public int calcMinUnits(double risk, double riskPerUnit, ToggleButton tglBuy){
        double currentValue = database.getPortFolioCurrentValue();
        double minAdjr = risk;
        if(risk > 0.25 && risk < 1)
            minAdjr = risk - 0.25;
        else if (risk > 1)
            minAdjr = risk - 0.5;
        double minRisk = currentValue * (minAdjr * 0.01);
        int minUnits;
        minUnits = (int) Math.round(minRisk / (riskPerUnit));
        return minUnits;
    }

    public void handleRiskLabel(Label lblRisk, int units, double riskPerUnit, Circle iconRisk, int maxUnits, int minUnits){
        double currentValue = database.getPortFolioCurrentValue();
        double totalRisk = units * riskPerUnit;
        double risk = f.formatDoubleXX((totalRisk/currentValue) *100);
        lblRisk.setText("Risk " + risk + "%");
        if(units <= maxUnits && units >= minUnits)
            iconRisk.setStyle("-fx-fill: #388e3c;");
        else
            iconRisk.setStyle("-fx-fill: #e53935");

    }
    public void handleThreeRLabel(Label lblTreR, ToggleButton tglBuy, double price, double riskPerUnit) {
        double target = 0;
        if(tglBuy.isSelected())
            target =  f.formatDoubleXXX((riskPerUnit) * 3 + price);
        else
            target = f.formatDoubleXXX((price - (riskPerUnit) * 3));
        lblTreR.setText("3R Target " + target);
    }

    public void handleLabelsActions(TableView<OpenPos> openPosTbl, Label lbl3RTarget, Label lblMaxUnits, Label lblMinUnits,
                                    ToggleButton tglBuy, double risk, TextField tickerField, TextField priceField,
                                    TextField unitsField, TextField stopField, Label lblExists, Label lblNewRisk
    ) {
        double currentValue = database.getPortFolioCurrentValue();
        double adjR = currentValue * (risk * 0.01);
        double currentOneR = currentValue * 0.01;
        String tickerF = tickerField.getText().toUpperCase();
        String priceF = priceField.getText().toUpperCase();
        String stopF = stopField.getText();
        String unitsF = unitsField.getText();
        double calcUnitsDouble = 0;
        boolean exists = false;
        double price =0;
        double stop =0;
        boolean existsTest = false;
        double maxAdjr = risk + 0.5;
        double minAdjr = 0;
        if(risk > 0.25)
            minAdjr = risk -0.25;
        else
            minAdjr = risk;
        double maxRisk = (int) Math.round(currentValue * (maxAdjr * 0.01));
        double minRisk = (int) Math.round(currentValue * (minAdjr * 0.01));
        double riskPerUni =0;
        int maxUnits =0;
        int minUnits = 0;
        if(!tickerF.isEmpty() )
        {
            int index = 0;
            while ( index < openPosTbl.getItems().size() && !existsTest) {
                existsTest =  tickerExists(tickerF, openPosTbl.getItems().get(index));
                if (existsTest)
                {
                    lblExists.setVisible(true);
                    openPos=openPosTbl.getItems().get(index);
                    exists = true;
                }
                index++;
            }
            if(!exists)
            {
                lblExists.setVisible(false);
                lblNewRisk.setVisible(false);
            }
        }
        if(!priceF.isEmpty() && exists)
        {
            price = Double.parseDouble(priceF);
            stop = openPos.getStop();
        }
        if(!priceF.isEmpty() && !stopF.isEmpty())
        {
            price = Double.parseDouble(priceF);
            stop = Double.parseDouble(stopF);
        }
        if(exists &&!priceF.isEmpty())
        {
            lblNewRisk.setVisible(true);
            double currentStop = openPos.getStop();
            if(stopF.isEmpty())
            {
                stopField.setText(String.valueOf(currentStop));
            }
            double currentAvg = openPos.getOpenPrice();
            if(currentAvg < currentStop)
            {
                /// Work in progress
            }
            else
            {
                double newRisk = price-stop;
                if(openPos.getSide() == 'B')
                    newRisk = price-stop;
                else
                    newRisk = stop - price;
                double currentTotalRiskPerUnits = ( currentAvg - openPos.getStop());
                double riskOpen = (currentTotalRiskPerUnits * openPos.getUnitsLeft());
                double currentRiskOpen =  adjR - riskOpen;
                double additionalSharesToBuy = currentRiskOpen / newRisk;
                int addInt = (int) Math.round(additionalSharesToBuy);
                double newR = 0;
                if(unitsF.isEmpty())
                {
                    newR =  f.formatDoubleXX((riskOpen + (newRisk * addInt))/currentOneR);
                    unitsField.setText(String.valueOf(addInt));
                }
                else
                {
                    int intUnits = Integer.parseInt(unitsF);
                    newR =  f.formatDoubleXX((riskOpen + (newRisk * intUnits ))/currentOneR);
                }
                    double newTotalRisk = newRisk * addInt;
                    double totalUnits = openPos.getUnitsLeft() + addInt;
                    riskPerUni = (newTotalRisk + riskOpen) / totalUnits;
                    maxUnits = (int) (maxRisk / riskPerUni) - openPos.getUnitsLeft();
                     lblNewRisk.setText("Risk " + newR + "%");
            }
            lblMaxUnits.setText("Max " + maxUnits);
            lblMinUnits.setText("Min " + minUnits);
            lbl3RTarget.setVisible(false);
            lblMaxUnits.setVisible(true);
            lblMinUnits.setVisible(true);
            lblNewRisk.setVisible(true);
        }
        double currentR;
        double currentTotalRisk;
        if (!priceF.isEmpty() && !stopF.isEmpty() && !exists ) {
            double threeRTarget =0;
            if (tglBuy.isSelected()) {
                calcUnitsDouble = adjR / (price - stop);
                threeRTarget = f.formatDoubleXXX(((price - stop)) * 3 + price);
                currentTotalRisk = (price-stop) * calcUnitsDouble;
                riskPerUni = price - stop;
            } else {
                calcUnitsDouble = adjR / ( stop-price);
                threeRTarget = f.formatDoubleXXX((( stop-price)) * 3 - price);
                currentTotalRisk = ((stop-price) * calcUnitsDouble);
                riskPerUni = stop - price;
            }
            currentR = f.formatDoubleXX(currentTotalRisk / currentOneR);
            maxUnits = (int) (maxRisk/ riskPerUni);
            int unitsInt = (int) Math.round(calcUnitsDouble);
            unitsField.setText(Integer.toString(unitsInt));
            if(risk == 0.25)
                minUnits = unitsInt;
            else
                minUnits = (int) (minRisk / riskPerUni) - openPos.getUnitsLeft();
            lblMaxUnits.setText("Max " + maxUnits);
            lblMinUnits.setText("Min " + minUnits);
            lblNewRisk.setText("Risk " +currentR + "%");
            lbl3RTarget.setText("3R Target " + threeRTarget);
            lbl3RTarget.setVisible(true);
            lblMaxUnits.setVisible(true);
            lblMinUnits.setVisible(true);
            lblNewRisk.setVisible(true);
        }
        if(tickerF.isEmpty() && priceF.isEmpty() && unitsF.isEmpty() && stopF.isEmpty())
        {
            lbl3RTarget.setVisible(false);
            lblMaxUnits.setVisible(false);
            lblMinUnits.setVisible(false);
            lblNewRisk.setVisible(false);
        }
    }
    private int extractNumber(String input) {
        // Define a pattern to match digits in the string
        Pattern pattern = Pattern.compile("\\d+");

        // Create a matcher with the input string
        Matcher matcher = pattern.matcher(input);

        // Find the first match
        if (matcher.find()) {
            // Extract the matched digits and convert them to an integer
            return Integer.parseInt(matcher.group());
        } else {
            // No match found, handle this case accordingly (return a default value or throw an exception)
            throw new IllegalArgumentException("No number found in the input string");
        }
    }
}