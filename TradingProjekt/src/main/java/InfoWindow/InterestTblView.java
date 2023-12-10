package InfoWindow;
import DataBase.DatabaseController;
import com.example.javafxtest.ApiCall;
import com.example.javafxtest.FormatterClass;
import com.example.javafxtest.Validation;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.ArrayList;


public class InterestTblView extends TableView<PotentialPos> {
    // ... Code for configuring the table and handling its behavior

    public ArrayList<PotentialPos> getPotentialPos() {
        return potentialPos;
    }

    public InterestTblView() {
        FormatterClass f = new FormatterClass();
    }

    FormatterClass f = new FormatterClass();
    Validation validate = new Validation();
    private ArrayList<PotentialPos> potentialPos = new ArrayList<PotentialPos>();

    public void handleAddInterestPos(TextField lblTickerRT, ToggleButton tglLong, TableView<PotentialPos> tblInterest, Label lblAdjR) {

        String adjRString = lblAdjR.getText();
        adjRString = adjRString.replace("%", "");
        double adjR = Double.parseDouble(adjRString);
        DatabaseController database = new DatabaseController();
        double currentValue = database.getPortFolioCurrentValue();
        double dolRisk = f.formatDoubleXX(adjR *currentValue)/100;
        String symb = lblTickerRT.getText().toUpperCase();
        ApiCall apicall = new ApiCall(symb);
        FormatterClass f = new FormatterClass();
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
            units = (int) Math.floor(dolRisk / risk);
            side = 'B';
            stop = f.formatDoubleXX(lod - adr5min);
            threeRtarget = f.formatDoubleXX(price + (risk * 3));
        } else {
            risk = f.formatDoubleXX((hod - price) + adr5min);
            units = (int) Math.floor(dolRisk / risk);
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
        FormatterClass f = new FormatterClass();
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
            DatabaseController database = new DatabaseController();
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
            unitsF.setText(String.format("%.0f", p.getUnits()));
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
            {
                exists = true;
            }
        return exists;
    }
    public void handleUnitsField(TableView<OpenPos> openPos, TextField tickerField, TextField priceField,
                                 TextField stopField, TextField unitsField, Label lblNewRisk, ToggleButton tglBuy,
                                    Label lbl3RTarget, Label lblMaxUnits, Label lblMinUnits, Label lblExists )
    {
        if(validate.CheckNumberTextfields(stopField) && validate.CheckNumberTextfields(priceField) && validate.CheckNumberTextfields(priceField))
        {
            String tickerF = tickerField.getText().toUpperCase();
            String priceF = priceField.getText().toUpperCase();
            String stopF = stopField.getText();
            String unitsF = unitsField.getText();
            if(!unitsF.isEmpty()) {
                OpenPos o = new OpenPos();
                boolean exists = false;
                int index = 0;
                while (index < openPos.getItems().size() && !exists) {
                    exists = tickerExists(tickerF, openPos.getItems().get(index));
                    if (exists) {
                        o = openPos.getItems().get(index);
                        exists = true;
                    }
                    index++;
                }
                if (!priceF.isEmpty() && !stopF.isEmpty()) {
                    double price = Double.parseDouble(priceF);
                    double stop = Double.parseDouble(stopF);
                    int units = Integer.parseInt(unitsF);
                    double riskPerUni = 0;
                    double totalRisk = 0;
                    DatabaseController database = new DatabaseController();
                    database.getPortFolioCurrentValue();
                    double currentOneR = database.getPortFolioCurrentValue() * 0.01;
                    double rForPos = 0;

                    if (!exists) {
                        if (tglBuy.isSelected()) {
                            riskPerUni = price - stop;
                            totalRisk = riskPerUni * units;
                            rForPos = f.formatDoubleXX(totalRisk / currentOneR);
                            lblNewRisk.setText("Risk " + rForPos + "%");
                        } else {
                            riskPerUni = stop - price;
                            totalRisk = riskPerUni * units;
                            rForPos = f.formatDoubleXX(totalRisk / currentOneR);
                            lblNewRisk.setText("Risk " + rForPos + "%");
                        }
                    } else {
                        double oldStop = o.getStop();
                        double oldAvg = o.getOpenPrice();
                        double unitsOpen = o.getUnitsLeft();
                        double newRisk = 0;
                        double currentRisk = 0;
                        double totalR = 0;
                        if (tglBuy.isSelected()) {
                            if (oldAvg > oldStop)
                                currentRisk = (oldAvg - oldStop) * unitsOpen;
                            newRisk = (price - stop) * units;
                            totalRisk = newRisk + currentRisk;
                            totalR = f.formatDoubleXX(totalRisk / currentOneR);
                            lblNewRisk.setText("Risk " + totalR + "%");
                            lblNewRisk.setVisible(true);
                        }
                    }
                } else {
                    lbl3RTarget.setVisible(false);
                    lblMaxUnits.setVisible(false);
                    lblMinUnits.setVisible(false);
                    lblNewRisk.setVisible(false);
                    lblExists.setVisible(false);            }
            }

        }
    }

    public void handleLabelsActions(TableView<OpenPos> openPos, Label lbl3RTarget, Label lblMaxUnits, Label lblMinUnits,
                                    ToggleButton tglBuy, double risk, TextField tickerField, TextField priceField,
                                    TextField unitsField, TextField stopField, Label lblExists, Label lblNewRisk
    ) {

        DatabaseController database = new DatabaseController();
        double currentValue = database.getPortFolioCurrentValue();
        double adjR = currentValue * (risk * 0.01);
        double currentOneR = currentValue * 0.01;
        String tickerF = tickerField.getText().toUpperCase();
        String priceF = priceField.getText().toUpperCase();
        String stopF = stopField.getText();
        String unitsF = unitsField.getText();
        double calcUnitsDouble = 0;
        boolean exists = false;
        OpenPos o = new OpenPos();
        double price =0;
        double stop =0;
        boolean existsTest = false;

        double maxAdjr = risk + 0.5;
        double minAdjr =0;
        if(risk > 0.25)
        {
            minAdjr = risk -0.25;
        }
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
            while ( index < openPos.getItems().size() && !existsTest) {
                existsTest =  tickerExists(tickerF, openPos.getItems().get(index));
                if (existsTest)
                {
                    lblExists.setVisible(true);
                    o=openPos.getItems().get(index);
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
            stop = o.getStop();
        }
        if(!priceF.isEmpty() && !stopF.isEmpty())
        {
            price = Double.parseDouble(priceF);
            stop = Double.parseDouble(stopF);
        }
        if(exists &&!priceF.isEmpty())
        {
            lblNewRisk.setVisible(true);
            double currentStop = o.getStop();
            if(stopF.isEmpty())
            {
                stopField.setText(String.valueOf(currentStop));
            }
            double currentAvg = o.getOpenPrice();
            if(currentAvg < currentStop)
            {
                /// Work in progress
            }
            else
            {
                double newRisk = price-stop;
                if(o.getSide() == 'B') {
                    newRisk = price-stop;
                }
                else
                    newRisk = stop - price;
                double currentTotalRiskPerUnits = ( currentAvg - o.getStop());
                double riskOpen = (currentTotalRiskPerUnits * o.getUnitsLeft());
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
                    double totalUnits = o.getUnitsLeft() + addInt;
                    riskPerUni = (newTotalRisk + riskOpen) / totalUnits;
                    maxUnits = (int) (maxRisk / riskPerUni) - o.getUnitsLeft();
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
        if (!tickerF.isEmpty() && !priceF.isEmpty() && !stopF.isEmpty() && !exists ) {
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
                minUnits = (int) (minRisk / riskPerUni) - o.getUnitsLeft();
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
}