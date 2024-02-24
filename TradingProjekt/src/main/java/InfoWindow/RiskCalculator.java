package InfoWindow;

import DataBase.DatabaseController;
import com.example.javafxtest.ApiCall;
import com.example.javafxtest.FormatterClass;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.shape.Circle;

import java.time.LocalDate;

public class RiskCalculator {

    public RiskCalculator()
    {

    }

    int id;
    double risk;
    DatabaseController database = new DatabaseController();
    double score;
    double marketMaScore;
    double marketAtrScore;
    double portfolioScore;
    double openPosScore;
    double drawDownScore;

    FormatterClass f = new FormatterClass();

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public void setUpScore()
    {
        score = marketMaScore + marketAtrScore+ portfolioScore+ drawDownScore+ openPosScore;
        System.out.println(marketMaScore + "    marketMaScore");
        System.out.println(marketAtrScore + "    marketAtrScore");
        System.out.println(portfolioScore + "    portfolioScore");
        System.out.println(openPosScore + "    openPosScore");
        System.out.println(drawDownScore + "    drawDownScore");
        System.out.println(score + "    score");
        System.out.println(getRisk() + " risk");
    }

    public void calcAdjR(Label lblAdjR)
    {
        if(score >= 15)
            risk= 1.5;
        else if (score >= 12.5)
            risk= 1.25;
        else if (score >= 10)
            risk= 1;
        else if (score >= 7.5)
            risk= 0.75;
        else if (score >= 5)
            risk= 0.5;
        else
            risk= 0.25;
        String riskString = String.valueOf(risk);
        lblAdjR.setText(riskString + "%");
    }

    public void setUpOpenPosScore(TableView<OpenPos> o){
        int result=0;
        int size = o.getItems().size();
        for(int i =0; i<size; i++) {
            OpenPos holdPos = o.getItems().get(i);
            double price = holdPos.getCurrentPrice();
            double adr =holdPos.getAdr();
            double entry = holdPos.getOpenPrice();
            double adrResult = f.formatDoubleXX((price-entry)/adr);
            if(adrResult>=1 || adrResult<=0.5)
            {
                int adrInt = (int) Math.round(adrResult);
                result += adrInt;
            }
        }
        openPosScore = result;
    }

    public void setUpPortfolioScore() {
        portfolioScore =0;
        double ma10 = database.getMa10();
        double ma20 = database.getMa20();
        double currentPrice= database.getPortFolioCurrentValue();
        if(ma10 != 0)
        {
            if(currentPrice > ma10)
                portfolioScore ++;
            if(ma20 != 0)
            {
                if(currentPrice > ma20)
                    portfolioScore ++;
                if(ma10 > ma20)
                    portfolioScore ++;
            }
        }
        double lastTradesScore = database.get10LastTrades()*0.25;
        portfolioScore += lastTradesScore;
    }
    public void setUpMarketScore(Label lblQQQ, Label lblSPY, Label lblIWM,
                                 Circle iconQqqma10, Circle iconQqqma20, Circle iconQqq10Over20,
                                 Circle iconSpyma10, Circle iconSpyma20, Circle iconSpy10Over20,
                                 Circle iconIWMma10, Circle iconIwmma20, Circle iconIwm10Over20)
    {
        processMarketActions("qqq", iconQqqma10, iconQqqma20, iconQqq10Over20, lblQQQ);
        processMarketActions("spy", iconSpyma10, iconSpyma20, iconSpy10Over20, lblSPY);
        processMarketActions("iwm", iconIWMma10, iconIwmma20, iconIwm10Over20, lblIWM);
    }
    private void processMarketActions(String symbol, Circle ma10Circle, Circle ma20Circle, Circle ma10Over20Circle, Label label) {
        ApiCall apiCall = new ApiCall();
        LocalDate date = LocalDate.now();
        String stringDate = date.toString();
        apiCall.getSetAtrMa(symbol, stringDate);
        double ma10 = apiCall.getMa10();
        double ma20 = apiCall.getMa20();
        double price = apiCall.GetCurrentPrice(symbol);
        handleMarketScores(price, ma10, ma20, ma10Circle, ma20Circle, ma10Over20Circle);
        updateLabels(label,price, ma10, apiCall.getAtr());
        updateMarketCirles(price, ma10, ma20, ma10Circle, ma20Circle, ma10Over20Circle);
    }
    public void updateMarketCirles(double price, double ma10, double ma20, Circle ma10Circle, Circle ma20Circle, Circle ma10Over20Circle){
        String style = (price < ma10) ? "-fx-fill: #e53935" : "-fx-fill: #388e3c";
        ma10Circle.setStyle(style);
        style = (price < ma20) ? "-fx-fill: #e53935" : "-fx-fill: #388e3c";
        ma20Circle.setStyle(style);
        style = (ma10 < ma20) ? "-fx-fill: #e53935" : "-fx-fill: #388e3c";
        ma10Over20Circle.setStyle(style);
    }
    private void handleMarketScores(double price, double ma10, double ma20, Circle ma10Circle, Circle ma20Circle, Circle ma10Over20Circle) {
        if (price > ma10)
            marketMaScore += 0.5;
        if (price > ma20)
            marketMaScore += 0.5;
        if (ma10 > ma20)
            marketMaScore += 0.5;
    }
    private void updateLabels(Label label, double price, double tenMa, double atr) {
        double dist = f.formatDoubleXX(((price-tenMa)/atr));
        label.setText(dist +" ATR");
        label.setVisible(true);
    }

    public void setUpDrawDownScore(Label lblDrawdown)
    {
        double maxValue = database.getPortfolioMaxValue();
        double currentValue = database.getPortFolioCurrentValue();
        double drawdown = f.formatDoubleXX(((maxValue-currentValue)/maxValue)*-100);
        String stringDrawdown = String.valueOf(drawdown);
        lblDrawdown.setText(stringDrawdown + "%");

        if(drawdown <= -20)
            drawDownScore= -10;
        else if (drawdown <= -15)
            drawDownScore= -6;
        else if (drawdown <= -10)
            drawDownScore= -3;
        else if (drawdown <= -5)
            drawDownScore= -1;

        if(!(currentValue == 0))
        {
            String drawdownValue = String.valueOf(f.formatDoubleXX(drawdown));
            lblDrawdown.setText(drawdownValue + "%");
        }
        else
            lblDrawdown.setText("N/A");
    }

    public void setUpPortfolioIcons(Circle iconPortfolioO10, Circle iconPortfolioO20, Circle iconPortfolio10O20)
    {
        double currentValue = database.getPortFolioCurrentValue();
        double tenMa = database.getMa10();
        double twentyMa = database.getMa20();
        String style ="";
        if(tenMa != 0){
            style = (currentValue > tenMa) ? "-fx-fill: #388e3c;" : "-fx-fill: #e53935";
            iconPortfolioO10.setStyle(style);
        }
        else
            iconPortfolioO10.setStyle("-fx-fill: #ffc233");
        if(twentyMa != 0){
            style = (currentValue > twentyMa) ? "-fx-fill: #388e3c;" : "-fx-fill: #e53935";
            iconPortfolioO20.setStyle(style);
        }
        else
            iconPortfolioO20.setStyle("-fx-fill: #ffc233");
        if(twentyMa != 0 || tenMa != 0) {
            style = (tenMa > twentyMa) ? "-fx-fill: #388e3c;" : "-fx-fill: #e53935";
            iconPortfolio10O20.setStyle(style);
        }
        else
            iconPortfolio10O20.setStyle("-fx-fill: #ffc233");
    }
}




