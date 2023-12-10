package ClosedTrades.InDepthStats;

import Analyse.MarketInfo;
import Analyse.Risk;
import Analyse.Stats;
import Analyse.Trades;
import DataBase.DatabaseController;
import com.example.javafxtest.ApiCall;
import com.example.javafxtest.FormatterClass;
import com.example.javafxtest.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class InDepthStatsController {

    public InDepthStatsController(){

    }
    FormatterClass f = new FormatterClass();
    DatabaseController database = new DatabaseController();
    public void setUpSymbolsTableView(TableView<Stats> tbl)
    {
        ArrayList<Stats> s = database.getTopFromStats(10);
        ObservableList<Stats> items = FXCollections.observableArrayList(s);
        tbl.setItems(items);
    }

    public void handleDateSearch(TableView<Stats> tbl, String startDate, String endDate, CheckBox cbWinners, CheckBox cbLosers, Spinner<Integer> spinner)
    {

        int top = spinner.getValue();
        ArrayList<Stats> stats = new ArrayList<Stats>();
        if (cbWinners.isSelected() )
        {
             stats = database.topForATimePeriod(startDate, endDate, top, "desc");
        }
        if(cbLosers.isSelected() )
        {
            if(stats.isEmpty())
            {
                stats = database.topForATimePeriod(startDate, endDate, top, "asc");
            }
            else
            {

                ArrayList<Stats> losers = database.topForATimePeriod(startDate, endDate, top, "asc");
                for(int i = 0; i < losers.size(); i++)
                {
                    boolean exists = false;
                    int index =0;
                    while (!exists && stats.size() > index )
                    {
                        int idLosers = losers.get(i).getId();
                        if(idLosers == stats.get(index).getId())
                        {
                            exists = true;
                        }
                        index++;
                    }
                    if(!exists)
                    {
                        stats.add(losers.get(i));
                    }
                }
            }
        }

        ObservableList<Stats> items = FXCollections.observableArrayList(stats);
        tbl.setItems(items);
    }
    public void setUpLabelForResult(Label lblResultOfSearch, CheckBox cbWinners, CheckBox cbLosers, Spinner<Integer> spinner)
    {
        int spinnerValue = spinner.getValue();
        if(cbWinners.isSelected() && !cbLosers.isSelected())
        {
            lblResultOfSearch.setText("Best " + spinnerValue + " trades");
        }
        else if(cbLosers.isSelected() && !cbWinners.isSelected())
        {
            lblResultOfSearch.setText("Worst " + spinnerValue + " trades");
        }
        else if(cbLosers.isSelected() && cbWinners.isSelected())
        {
            lblResultOfSearch.setText("Best & worst " + spinnerValue + " trades");
        }
    }
    public void setUpTradesTableView(TableView<Trades> tblTrades, TableView<Stats> tblStats)
    {
        int idStats = tblStats.getSelectionModel().getSelectedItem().getId();
        ArrayList<Trades> trades = database.readTradesWithId(idStats);
        ObservableList<Trades> items = FXCollections.observableArrayList(trades);
        tblTrades.setItems(items);
    }
    public void setUpLabelsSymbol(TableView<Trades> tblTrades, TableView<Stats> tblSymb, Label lblAdjrPnl,
                                  Label lblBestClosePrice,Label lblBestCloseR,
                                  Label lblOpenValue, Label lblCloseDate,Label lblDolPnl, Label lblOpenDate,
                                  Label lblPercentPnl, Label lblRTaken,Label lblCloseValue, Label lblSymbol,
                                  Label lblTimeHeld, Label lblTransactions, Label lblInsideRisk,
                                  Label lblOpenPrice, Label lblCLosePrice)
    {

        ArrayList<Trades> trades = new ArrayList<Trades>(tblTrades.getItems());
        int totalUnits =0;
        for(int i =0; i< trades.size(); i++)
        {
            if (trades.get(i).getSide() =='B')
            {
                totalUnits += trades.get(i).getUnits();
            }
        }
        Stats stats = tblSymb.getSelectionModel().getSelectedItem();
        int id = stats.getId();
        Risk risk = database.getRiskForStats(id);
        lblRTaken.setText(String.valueOf(risk.getRTaken()));
        lblSymbol.setText(stats.getSymb());
        lblDolPnl.setText("$"+(stats.getResult()));
        lblPercentPnl.setText(String.valueOf(stats.getPercentResult()));
        lblAdjrPnl.setText(String.valueOf(stats.getResultAdjR()));
        lblOpenValue.setText("$"+(f.formatDoubleXX(stats.getOpenPrice()*totalUnits)));
        lblCloseValue.setText("$"+(f.formatDoubleXX(stats.getClosePrice() * totalUnits)));
        lblOpenPrice.setText("$"+(stats.getOpenPrice()));
        lblCLosePrice.setText("$"+(stats.getClosePrice()));
        int transactions = trades.size();
        lblTransactions.setText(String.valueOf(transactions));
        lblOpenDate.setText(String.valueOf(trades.get(0).getDate()));
        lblCloseDate.setText(String.valueOf(trades.get(transactions-1).getDate()));
        double bestClose =0;
        boolean firstSell = true;
        if(stats.getSide() == 'B')
        {
            for(int i =0; i<trades.size(); i++)
            {
                if(trades.get(i).getSide() == 'S')
                {
                    if(firstSell)
                    {
                        bestClose =trades.get(0).getPrice();
                        firstSell = false;
                    }
                    if(bestClose< trades.get(i).getPrice())
                    {
                        bestClose = trades.get(i).getPrice();
                    }
                }
            }
            double bestCloseResult = bestClose - stats.getOpenPrice();
            double bestRClose = f.formatDoubleXX(bestCloseResult / risk.getRiskPerUnits());
            lblBestCloseR.setText(String.valueOf(bestRClose));
        }
        else
        {
            for(int i =0; i<trades.size(); i++)
            {
                if(trades.get(i).getSide() == 'B')
                {
                    if(firstSell)
                    {
                        bestClose =trades.get(0).getPrice();
                        firstSell = false;
                    }
                    if(bestClose > trades.get(i).getPrice())
                        bestClose = trades.get(i).getPrice();
                }
            }
            double bestCloseResult =  stats.getOpenPrice()- bestClose;
            double bestRClose = f.formatDoubleXX(bestCloseResult / risk.getRiskPerUnits());
            lblBestCloseR.setText(String.valueOf(bestRClose));
        }

        if(risk.isInsideR())
            lblInsideRisk.setText("YES");
        else
            lblInsideRisk.setText("NO");

        LocalDate openDate = LocalDate.parse(lblOpenDate.getText());
        LocalDate closeDate = LocalDate.parse(lblCloseDate.getText());
        int holdDays = -1;
        while (!openDate.isAfter(closeDate)) {
            if (openDate.getDayOfWeek() != DayOfWeek.SATURDAY && openDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                holdDays++;
            }
            openDate = openDate.plusDays(1); // Move to the next day
        }
        lblTimeHeld.setText(String.valueOf(holdDays));
        double bestClosePrice = 0;
        for(Trades item: trades)
        {
            if(stats.getSide()=='B')
            {
                if(item.getSide()=='S')
                {
                    if(item.getPrice() > bestClosePrice)
                        bestClosePrice =item.getPrice();
                }
            }
            else
            {
                if(item.getSide()=='B')
                {
                    if(item.getPrice() < bestClosePrice)
                        bestClosePrice =item.getPrice();
                }
            }
        }
        lblBestClosePrice.setText(String.valueOf(bestClosePrice));

    }

    public void setUpMarketAtrDist(Label lblQQQ, Label lblSpy, Label lblIwm, String date) throws ParseException {
        DatabaseController database = new DatabaseController();
        MarketInfo m = database.getMarketInfo(date);
        lblQQQ.setText(String.valueOf(m.getQqqAtrDist()));
        lblSpy.setText(String.valueOf(m.getSpyAtrDist()));
        lblIwm.setText(String.valueOf(m.getIwmAtrDistk()));
    }
    public void setUpMarketPerformance(Label lblQQQ, Label lblSPY, Label lblIWM, DatePicker startDate, DatePicker endDate)
    {
        String stringStartDate = startDate.getValue().toString();
        String stringEndDate = endDate.getValue().toString();
        ApiCall apicall = new ApiCall();
        double QQQPerformance = f.formatDoubleXX(apicall.getHistoricalPerformance("QQQ", stringStartDate, stringEndDate));
        double SPYPerformance = f.formatDoubleXX(apicall.getHistoricalPerformance("SPY", stringStartDate, stringEndDate));
        double IWMPerformance = f.formatDoubleXX(apicall.getHistoricalPerformance("IWM", stringStartDate, stringEndDate));
        lblQQQ.setText(QQQPerformance +"%");
        lblSPY.setText(SPYPerformance+"%");
        lblIWM.setText(IWMPerformance+"%");

    }
    public static LocalDate calculateWorkingDaysInPast(LocalDate startDate, int workingDays) {
        LocalDate currentDate = startDate.minusDays(1); // Start from one day before the given date
        int i = 0 ;
        while (workingDays > 0) {
            currentDate = currentDate.minusDays(1);
            // Check if the current date is a working day (e.g., excluding weekends)
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays--;
            }
        }
        return currentDate;
    }






}
