package Analyse;

import DataBase.DatabaseController;
import InfoWindow.OpenPos;
import com.example.javafxtest.ApiCall;
import com.example.javafxtest.FormatterClass;
import com.example.javafxtest.Validation;
import javafx.scene.chart.*;
import javafx.scene.control.Label;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

public class CloseTradesController {

    public CloseTradesController() {
        this.openPos = openPos;
        f = new FormatterClass();
        validation = new Validation();
    }

    Validation validation;
    FormatterClass f;
    OpenPos openPos;
    ArrayList<MarketInfo> marketInfo = new ArrayList<>();
    ArrayList<Stats> stats = new ArrayList<>();
    ArrayList<SymbStats> symbStats = new ArrayList<>();
    ArrayList<Trades> trades = new ArrayList<>();
    ArrayList<Portfolio> portfolios = new ArrayList<>();
    ArrayList<Risk> risk = new ArrayList<>();
    DatabaseController controller = new DatabaseController();

    public void setUpPortfolioFromDb(){portfolios = controller.readPortfolioInfo();}

    public void setUpStatsFromDb() {
        stats = controller.readStats();
    }

    public void setUpTradesFromDb() {
        trades = controller.readTrades();
    }

    public void setUpSymbStatsFromDb() {
        symbStats = controller.readSymbStat();
    }

    public void setUpMarketInfoFromDb() {
        marketInfo = controller.readMarketInfo();
    }
    public void setUpRiskInfoFromDb(){ risk = controller.readRiskInfo();}


    public void setUpStops(OpenPos o) {
        SymbStats s = new SymbStats();
        s.setAdr(o.getAdr());
    }

    public void addMarketInfoToDb(OpenPos o) {
        ApiCall apiCallen = new ApiCall();
        MarketInfo m = new MarketInfo();
        int size = marketInfo.size();
        int lastValue;
        String date = o.getDateAtIndex(0);
        m.setDate(date);
        LocalDate parseDate = LocalDate.parse(date);
        LocalDate currentDate = LocalDate.now();
        if (size != 0) {
            lastValue = symbStats.get(size - 1).getId()+1;
            m.setId(lastValue);
        } else {
            m.setId(1);
        }
        double qPrice = 0;
        double qTen = 0;
        double qTwenty;
        double sPrice;
        double sTen;
        double sTwenty;
        double iPrice;
        double iTen;
        double iTwenty;
        apiCallen.getSetAtrMa("qqq", date);
        qTen = apiCallen.getMa10();
        qTwenty = apiCallen.getMa20();
        if (currentDate.equals(parseDate)) {
            qPrice = apiCallen.GetCurrentPrice("qqq");
        } else {
            qPrice = apiCallen.getHistoricalPrice();
        }
        if (qTen > qTwenty)
            m.setQqq10Over20(true);
        if (qPrice > qTen)
            m.setQqqOver10(true);
        if (qPrice > qTwenty)
            m.setQqqOver20(true);
        m.setQqqAtrDist(f.formatDoubleXX(((qPrice - qTen) / qTen) * 100));
        apiCallen.getSetAtrMa("spy", date);
        sTen = apiCallen.getMa10();
        sTwenty = apiCallen.getMa20();
        if (currentDate.equals(parseDate))
            sPrice = apiCallen.GetCurrentPrice("spy");
        else
            sPrice = apiCallen.getHistoricalPrice();
        if (sTen > sTwenty)
            m.setSpy10Over20(true);
        if (sPrice > sTen)
            m.setSpyOver10(true);
        if (sPrice > sTwenty)
            m.setSpyOver20(true);
        m.setSpyAtrDist(f.formatDoubleXX(((sPrice - sTen) / sTen) * 100));
        apiCallen.getSetAtrMa("iwm", date);
        iTen = apiCallen.getMa10();
        iTwenty = apiCallen.getMa20();
        if (currentDate.equals(parseDate)) {
            iPrice = apiCallen.GetCurrentPrice("iwm");
        } else {
            iPrice = apiCallen.getHistoricalPrice();
        }
        if (iTen > iTwenty)
            m.setIwm10Over20(true);
        if (iPrice > iTen)
            m.setIwmOver10(true);
        if (iPrice > iTwenty)
            m.setIwmOver20(true);
        m.setIwmAtrDistk(f.formatDoubleXX(((iPrice - iTen) / iTen) * 100));
        marketInfo.add(m);
        controller.insertMarketInfo(m);
    }
    public void addStatsToTb(OpenPos o) {
        Stats s = new Stats();
        int size = stats.size();
        int lastValue;
        if (size != 0) {
            lastValue = stats.get(size - 1).getId()+1;
            s.setId(lastValue);
        } else {
            s.setId(1);
        }
        char wl = 0;
        double result = 0;
        int totalUnits = 0;
        String openDate = o.getDate().get(0);
        String closeDate = o.getDate().get(o.getDate().size() - 1);
        String dayOfWeek = getDayOfWeek(openDate);
        String month = getMonth(openDate);
        int holdDays = calcHoldTime(openDate, closeDate) - 1;
        for (int i = 0; i < o.getUnits().size(); i++) {
            if(o.getSideAtindex(i)=='B')
                totalUnits += o.getUnitsAtIndex(i);
        }
        if (o.getSide() == 'B') {
            if (o.getOpenPrice() < o.getClosePrice())
                wl = 'W';
            else {
                wl = 'L';
            }
            result = f.formatDoubleXX((o.getClosePrice() - o.getOpenPrice()) * totalUnits);
        } else {
            if (o.getOpenPrice() > o.getClosePrice()) {
                wl = 'W';
            } else {
                wl = 'S';
            }
            result = f.formatDoubleXX((o.getOpenPrice() - o.getClosePrice()) * totalUnits);
        }
        double adjDolRik = o.getCurrentR()* o.getCalcAdjR();
        double adjRResult = f.formatDoubleXX((result / adjDolRik));
        double rResult = f.formatDoubleXX(result / o.getCurrentR());
        double percentResult = f.formatDoubleXXX(((o.getClosePrice() - o.getOpenPrice()) / o.getOpenPrice()) * 100);
        Stats st = new Stats(s.getId(), o.getSymb(), wl, o.getSide(), o.getOpenPrice(), o.getClosePrice(), result, percentResult, rResult, adjRResult,
                dayOfWeek, month, holdDays);
        stats.add(st);
        controller.insertStats(st, openDate);
    }

    public void addStatsToDb(OpenPos o) {
        String openDate = o.getDateAtIndex(0);
        double openPrice = o.getPriceAtIndex(0);
        ApiCall apicall = new ApiCall();
        apicall.setYDActions(o.getSymb(), openDate);
        SymbStats s = new SymbStats();
        s.setRangeVatr(o.getRange());
        apicall.getSetAtrMa(o.getSymb(), openDate);
        double ma10 = apicall.getMa10();
        double ma20 = apicall.getMa20();
        int size = symbStats.size();
        if (size > 0) {
            size = symbStats.get(size - 1).getId() + 1;
        } else {
            size = 1;
        }
        s.setId(size);
        s.setAdr(f.formatDoubleXXX(o.getAdr()));
        s.setRangeVatr(f.formatDoubleXXX(s.getRangeVatr()));
        s.setInsideDayPrior(apicall.isInsideDayPrior());
        s.setRangevsAtrPriorDay(f.formatDoubleXXX(apicall.getYdRange() / o.getAtr()));
        s.setAtrToMa10(f.formatDoubleXX((ma10 - openPrice) / o.getAtr()));
        s.setAtrToMa20(f.formatDoubleXX((ma20 - openPrice) / o.getAtr()));
        s.setAvgDolVol(f.formatDoubleXXXX(apicall.GetAvgDolVol(o.getSymb(), openDate)));
        symbStats.add(s);
        controller.insertSymbStats(s);
    }

    public void addTradesToDb(OpenPos o) {
        for (int i = 0; i < o.getPrice().size(); i++) {
            Trades t = new Trades();
            int size = trades.size();
            int lastValue =0;
            if (size != 0) {
                lastValue = trades.get(size - 1).getId()+1;
                t.setId(lastValue);
            } else {
                t.setId(1);
            }
            t.setSide(o.getSideAtindex(i));
            t.setDate(o.getDateAtIndex(i));
            t.setPrice(o.getPriceAtIndex(i));
            t.setUnits(o.getUnitsAtIndex(i));
            t.setStatsID(stats.get(stats.size() - 1).getId());
            trades.add(t);
            controller.insertTrade(t);
        }
    }


    public void addPortFolioToDb(OpenPos openPos)
    {
        Portfolio p = new Portfolio();
        int size = portfolios.size();
        int lastValue ;
        if (size != 0) {
            lastValue = portfolios.get(size - 1).getId()+1;
            p.setId(lastValue);
        } else {
            p.setId(1);
        }
        int dateSize = openPos.getDate().size();
        int statsSize = stats.size();
        String date = openPos.getDateAtIndex(dateSize-1);
        double value;
        if(!portfolios.isEmpty())
        {
            value = f.formatDoubleXX((portfolios.get(size-1).getValue()) + (stats.get(statsSize-1).getResult()));
        }
        else
            value = 3000 + stats.get(statsSize-1).getResult();

        double result = stats.get(statsSize-1).getResult();
        double drawdown =0;
        double tenAvg = f.formatDoubleXX(controller.getMa10());
        double twentyAvg = f.formatDoubleXX(controller.getMa20());
        double maxValue = controller.getPortfolioMaxValue();
        if(maxValue> value )
            drawdown = f.formatDoubleXX(((maxValue- value)/maxValue)*100);
        p.setDate(date);
        p.setDrawdown(drawdown);
        p.setValue(value);
        p.setResult(result);
        p.setTenAvg(tenAvg);
        p.setTwentyAvg(twentyAvg);
        p.setIdStats(controller.getIdStats());
        portfolios.add(p);
        controller.insertPortfolio(p);
    }

    public void addRiskToDb(OpenPos openpos)
    {
        Risk r = new Risk();
        int size = risk.size();
        int lastValue;
        if (size != 0) {
            lastValue = risk.get(size - 1).getIdRisk()+1;
            r.setIdRisk(lastValue);
        } else {
            r.setIdRisk(1);
        }
        double oneR =f.formatDoubleXX(openpos.getCurrentR());
        double dolRisk = openpos.getTotalRisk();
        double riskPerUnits = openpos.getRisk();
        double rTaken = openpos.getTakenR();
        double adjR =openpos.getCalcAdjR();
        boolean insideR = true;
        double idStats = 0;
        if(rTaken > adjR+0.5 || rTaken < adjR -0.25)
        {
            insideR = false;
        }
        r.setAdjR(adjR);
        r.setDolRisk(dolRisk);
        r.setOneR(oneR);
        r.setInsideR(insideR);
        r.setRiskPerUnits(riskPerUnits);
        r.setRTaken(rTaken);
        r.setIdStats(controller.getIdStats());
        controller.insertRisk(r);
    }


    public String getDayOfWeek(String theDate) {
        String dayOfWeek = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // Parse the date string into a LocalDate object
            LocalDate date = LocalDate.parse(theDate, formatter);
            // Get the day of the week as a string (e.g., "MONDAY")
            dayOfWeek = date.getDayOfWeek().toString();
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
        return dayOfWeek;
    }

    public String getMonth(String theDate) {
        String month = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // Parse the date string into a LocalDate object
            LocalDate date = LocalDate.parse(theDate, formatter);
            // Get the day of the week as a string (e.g., "MONDAY")
            month = date.getMonth().toString();
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
        return month;
    }

    public int calcHoldTime(String openDate, String closeDate) {
        int holdTime = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the date string into a LocalDate object
        LocalDate parseOpenDate = LocalDate.parse(openDate, formatter);
        LocalDate parsecloseDate = LocalDate.parse(closeDate, formatter);
        while (!parseOpenDate.isAfter(parsecloseDate)) {
            if (parseOpenDate.getDayOfWeek() != DayOfWeek.SATURDAY && parseOpenDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                holdTime++;
            }
            parseOpenDate = parseOpenDate.plus(1, ChronoUnit.DAYS);
        }
        return holdTime;
    }

    public void setUpChart(LineChart<String, Double> chart)
    {
        chart.setCreateSymbols(false);
        double result =0;
        XYChart.Series mainSerie = new XYChart.Series<>();
        for(int i = 0; i<stats.size(); i++)
        {
            result = result + stats.get(i).getResult();
            String value = String.valueOf(i);
            mainSerie.getData().add(new XYChart.Data<>(value, result));
        }
        chart.getData().add(mainSerie);
//        XYChart.Series fiveDayMA = new XYChart.Series<>();
//        for(int i = 0; i<stats.size(); i++)
//        {
//            if(i==0)
//                result = result + stats.get(i).getResult();
//            result = (result + stats.get(i).getResult())/i;
//            String value = String.valueOf(i);
//            fiveDayMA.getData().add(new XYChart.Data<>(value, result));
//            i++;
//        }
//        chart.getData().add(fiveDayMA);
    }

    public void setUpBarChart(BarChart<String, Integer> barChart) {
        String[] r = {"<-5R", "-4R","-3R","-2R","-1R","0R","1R","2R","3R","4R","5R","6R","7R","8R","9R",">10R"};
        int[] values= new int[16];
        for(int i = 0; i<stats.size(); i++)
        {
            int value = (int) Math.round(stats.get(i).getResultR());
            switch (value) {
                case -5 -> values[0] = values[0] + 1;
                case -4 -> values[1] = values[1] + 1;
                case -3 -> values[2] = values[2] + 1;
                case -2 -> values[3] = values[3] + 1;
                case -1 -> values[4] = values[4] + 1;
                case 0 -> values[5] = values[5] + 1;
                case 1 -> values[6] = values[6] + 1;
                case 2 -> values[7] = values[7] + 1;
                case 3 -> values[8] = values[8] + 1;
                case 4 -> values[9] = values[9] + 1;
                case 5 -> values[10] = values[10] + 1;
                case 6 -> values[11] = values[11] + 1;
                case 7 -> values[12] = values[12] + 1;
                case 8 -> values[13] = values[13] + 1;
                case 9 -> values[14] = values[14] + 1;
                default -> {
                    if (value >= 10)
                        values[15] = values[15] + 1;
                    else values[0] = values[0] + 1;
                }
            }
        }
        XYChart.Series<String, Integer> serie = new XYChart.Series<>();
        for(int i = 0; i<values.length; i++)
        {
            serie.getData().add(new XYChart.Data<>(r[i], values[i]));
        }
        barChart.getData().addAll(serie);
    }
    public void setUpWinLossLabels(Label lblAveragePercentWin,Label lblAveragePercentLoss,Label lblAverageRWin,
                                   Label lblAverageRLoss,Label lblAverageAdjRWin, Label lblAverageAdjRLoss,
                                   Label lblBiggestRWin, Label lblBiggestRLoss){
        lblAveragePercentWin.setText(calcAveragePercent('W') +"%");
        lblAveragePercentLoss.setText(calcAveragePercent('L') +"%");
        lblAverageRWin.setText(calcAverageR('W')+"R");
        lblAverageRLoss.setText(calcAverageR('L')+"R");
        lblAverageAdjRWin.setText(calcAverageAdjR('W')+"R");
        lblAverageAdjRLoss.setText(calcAverageAdjR('L')+"R");

    }

    public void setUpResultLabels(Label profFactor,Label winLoss, Label averagePercent, Label averageR,Label averageAdjR, Label winFrequency, Label result)
    {
        String r = (Double.toString(calcResult()));
        result.setText(r + "$");
        profFactor.setText(Double.toString(calcProfitFactor()));
        winLoss.setText(Double.toString(calcWinLossRatio()));
        double aPercent = calcAveragePercent('A'); //A = all
        averagePercent.setText(aPercent+"%");
        averageR.setText(calcAverageR('A')+"R");
        averageAdjR.setText(calcAverageAdjR('A')+"R");
        double wFrequency = calcWinFrequency();
        winFrequency.setText(wFrequency + "%");
    }

    public double calcResult()
    {
        double result = 0;
        for (Stats stat : stats) result = result + stat.getResult();
        return f.formatDoubleXX(result);
    }
    public double calcProfitFactor()
    {
        double result = 0;
        double totalWinResult = stats.stream().filter(s -> s.getResult()!=0).filter(stat -> stat.getResult() > 0).mapToDouble(Stats::getResult).sum();
        double totalLossResult = stats.stream().filter(s -> s.getResult()!=0).filter(stat -> stat.getResult() < 0).mapToDouble(Stats::getResult).sum();
        if(validation.notZero(totalWinResult) && validation.notZero(totalLossResult))
            result = f.formatDoubleXX(totalWinResult/totalLossResult * -1);
        return result;
    }

    public double calcWinLossRatio()
    {
        double result = 0;
        double avgWin =0;
        double avgLoss=0;
        double gainCount = 0 ;
        double lossCount = 0 ;
        for (Stats stat : stats) {
            double currentResult = stat.getResult();
            if (currentResult > 0) {
                avgWin += currentResult;
                gainCount++;
            } else {
                avgLoss += currentResult * -1;
                lossCount++;
            }
        }
        if(validation.notZero(avgWin) && validation.notZero(avgLoss) && lossCount >0) {
            avgWin = avgWin / gainCount;
            avgLoss = avgLoss / lossCount;
            result = f.formatDoubleXX(avgWin/avgLoss);
        }
        return result;
    }
//    public double calcAveragePercent(char type)
//    {
//        double result =0;
//        int totalTrades =0;
//        double holdResult;
//        int i=0;
//            while(i<stats.size())
//            {
//                holdResult = stats.get(i).getPercentResult();
//                    result = result + holdResult;
//                    totalTrades++;
//                i++;
//            }
//        if(validation.notZero(result) && validation.notZero(totalTrades))
//            result = f.formatDoubleXX(result /totalTrades);
//        System.out.println(totalTrades);
//        System.out.println(stats.size());
//        return result;
//    }
    public double calcAveragePercent(char type)
    {
        double result =0;
        DoubleSummaryStatistics summary;
        if(type == 'A')
            summary = stats.stream().collect(Collectors.summarizingDouble(Stats::getPercentResult));
        else if(type == 'W')
            summary = stats.stream().filter(pos->pos.getWinLoss()=='W').collect(Collectors.summarizingDouble(Stats::getPercentResult));
        else
            summary = stats.stream().filter(pos->pos.getWinLoss()=='L').collect(Collectors.summarizingDouble(Stats::getPercentResult));
        if(validation.notZero(summary.getCount()))
            result = f.formatDoubleXX(summary.getSum() / summary.getCount());
        return result;
    }
    public double calcAverageR(char type)
    {
        double result =0;
        DoubleSummaryStatistics summary;
        if(type == 'A')
            summary = stats.stream().collect(Collectors.summarizingDouble(Stats::getResultR));
        else if(type == 'W')
            summary = stats.stream().filter(pos->pos.getWinLoss()=='W').collect(Collectors.summarizingDouble(Stats::getResultR));
        else
            summary = stats.stream().filter(pos->pos.getWinLoss()=='L').collect(Collectors.summarizingDouble(Stats::getResultR));
        if(validation.notZero(summary.getCount()))
                result = f.formatDoubleXX(summary.getSum() / summary.getCount());
        return result;
    }
    public double calcAverageAdjR(char type)
    {
        double result =0;
        DoubleSummaryStatistics summary;
        if(type == 'A')
            summary = stats.stream().collect(Collectors.summarizingDouble(Stats::getResultAdjR));
        else if(type == 'W')
            summary = stats.stream().filter(pos->pos.getWinLoss()=='W').collect(Collectors.summarizingDouble(Stats::getResultAdjR));
        else
            summary = stats.stream().filter(pos->pos.getWinLoss()=='L').collect(Collectors.summarizingDouble(Stats::getResultAdjR));
        if(validation.notZero(summary.getCount()))
            result = f.formatDoubleXX(summary.getSum() / summary.getCount());
        return result;
    }
    public double calcWinFrequency()
    {
        double result =0;
        int winCounts = (int) stats.stream().filter(stats -> stats.getResult() >0).count();
        int totalTrades = stats.size();
        if(validation.notZero(totalTrades))
            result = f.formatDoubleXX(((double)winCounts/totalTrades)*100);
        return result;
    }
}
