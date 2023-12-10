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
        DatabaseController database = new DatabaseController();
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
            iconQqqma10.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconQqqma10.setStyle("-fx-fill: #388e3c;");
        }
        if(qqqPrice < qqqMa20)
            iconQqqma20.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconQqqma20.setStyle("-fx-fill: #388e3c;");
        }
        if(qqqMa10 < qqqMa20)
            iconQqq10Over20.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconQqq10Over20.setStyle("-fx-fill: #388e3c;");
        }

        apicall.getSetAtrMa("SPY", stringDate);
        double spyMa10 = apicall.getMa10();
        double spyMa20 = apicall.getMa20();
        double spyPrice = apicall.GetCurrentPrice("spy");
        double spyAtr = apicall.getAtr();
        if(spyPrice < spyMa10)
            iconSpyma10.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconSpyma10.setStyle("-fx-fill: #388e3c;");
        }
        if(spyPrice < spyMa20)
            iconSpyma20.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconSpyma20.setStyle("-fx-fill: #388e3c;");
        }
        if(spyMa10 < spyMa20)
            iconSpy10Over20.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconSpy10Over20.setStyle("-fx-fill: #388e3c;");
        }

        apicall.getSetAtrMa("iwm", stringDate);
        double iwmMa10 = apicall.getMa10();
        double iwmMa20 = apicall.getMa20();
        double IWMPrice = apicall.GetCurrentPrice("iwm");
        double iwmAtr = apicall.getAtr();
        if(IWMPrice < iwmMa10)
            iconIWMma10.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconIWMma10.setStyle("-fx-fill: #388e3c;");
        }
        if(IWMPrice < iwmMa20)
            iconIwmma20.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconIwmma20.setStyle("-fx-fill: #388e3c;");
        }
        if(iwmMa10 < iwmMa20)
            iconIwm10Over20.setStyle("-fx-fill: #e53935");
        else
        {
            marketMaScore += 0.5;
            iconIwm10Over20.setStyle("-fx-fill: #388e3c;");
        }

        double QQQdist = f.formatDoubleXX(((qqqPrice-qqqMa10)/qqqAtr));
        int roundedQqqValue = (int) Math.round(QQQdist);
        double SPYdist = f.formatDoubleXX(((spyPrice-spyMa10)/spyAtr));
        int roundedSpyValue = (int) Math.round(SPYdist);
        double IWMdist = f.formatDoubleXX(((IWMPrice-iwmMa10)/iwmAtr));
        int roundedIwmValue = (int) Math.round(IWMdist);
        marketAtrScore =0;
        if(roundedQqqValue > 1)
        {
            int score = 1;
            while (score < roundedQqqValue) {
                marketAtrScore--;
                roundedQqqValue --;
            }
        }
        if(roundedSpyValue > 1)
        {
            int score = 1;
            while (score < roundedSpyValue) {
                marketAtrScore--;
                roundedSpyValue --;
            }
        }
        if(roundedIwmValue > 1)
        {
            int score = 1;
            while (score < roundedIwmValue) {
                marketAtrScore--;
                roundedIwmValue --;
            }
        }
    }
    public void setUpDrawDownScore(Label lblDrawdown)
    {
        DatabaseController database = new DatabaseController();
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
        DatabaseController database = new DatabaseController();
        double maxValue=database.getPortfolioMaxValue();
        double currentValue = database.getPortFolioCurrentValue();
        double tenMa = database.getMa10();
        double twentyMa = database.getMa20();
        if(!(tenMa == 0))
        {
            if( currentValue > tenMa)
            {
                iconPortfolioO10.setStyle("-fx-fill: #388e3c;");
            }
            else
            {
                iconPortfolioO10.setStyle("-fx-fill: #e53935");
            }
        }
        else
        {
            iconPortfolioO10.setStyle("-fx-fill: #ffc233");
        }
        if(!(twentyMa == 0))
        {
            if(currentValue > twentyMa)
            {
                iconPortfolioO20.setStyle("-fx-fill: #388e3c;");
            }
            else
            {
                iconPortfolioO20.setStyle("-fx-fill: #e53935;");
            }
        }
        else
        {
            iconPortfolioO20.setStyle("-fx-fill: #ffc233");
        }
        if(!(twentyMa == 0))
        {
            if(tenMa > twentyMa)
            {
                iconPortfolio10O20.setStyle("-fx-fill: #388e3c;");
            }
            else
            {
                iconPortfolio10O20.setStyle("-fx-fill: #e53935;");
            }
        }
        else
        {
            iconPortfolio10O20.setStyle("-fx-fill: #ffc233");
        }
    }
    }




