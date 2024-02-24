package DataBase;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import Analyse.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class DatabaseController {


    public DatabaseController(){
        database = new MyJDBC();
    }
    MyJDBC database;
    CloseTradesController controller;

    public ArrayList<Stats> readStats()
    {
        return database.readStats();
    }
    public ArrayList<Trades> readTrades()
    {
        return database.readTrades();
    }
    public ArrayList<SymbStats> readSymbStat()
    {
        return database.readSymbStats();
    }
    public ArrayList<MarketInfo> readMarketInfo()
    {
        return database.readMarketInfo();
    }
    public ArrayList<Portfolio> readPortfolioInfo(){return database.readPortfolio();}
    public ArrayList<Risk> readRiskInfo(){return database.readRisk();}
    public int getIdStats()
    {
        int id= 0 ;
             String query = "SELECT MAX(idStats) FROM stats;";
             id = database.getID(query);
        return id;
    }

    // Inserts
    public void insertPortfolio(Portfolio portfolio)
    {
        try {
            String date = portfolio.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date theDate = sdf.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(theDate.getTime());
            double value = portfolio.getValue();
            double result = portfolio.getResult();
            double tenAvg = portfolio.getTenAvg();
            double twentyAvg = portfolio.getTwentyAvg();
            double drawdown = portfolio.getDrawdown();
            String queryGetId = "select max(idStats) from stats";
            int idStats = database.getID(queryGetId);
            String query  = "insert into portfolio (Date, value, result, tenAvg,TwentyAvg,Drawdown, idStats)"+
            "values('"+sqlDate+"',"+value+","+result+","+tenAvg+","+twentyAvg+","+drawdown+ ","+idStats+")";
            database.insert(query);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void insertStats(Stats stats, String date)
    {
        String symb = stats.getSymb();
        char winLoss = stats.getWinLoss();
        char side = stats.getSide();
        double openPrice = stats.getOpenPrice();
        double closePrice = stats.getClosePrice();
        double result = stats.getResult();
        double resultR = stats.getResultR();
        double resultAdjr = stats.getResultAdjR();
        String dayOfWekk = stats.getDayOfWeek();
        String month = stats.getMonth();
        int holdDays = stats.getHoldDays();
        double percentResult = stats.getPercentResult();
        int idMarket = 0;
        String queryGetId;
        if(checkMarketAtEntryDate(date).equals("YES"))
        {
            queryGetId = "select idm_a_e from market_at_entry where date = '" +date +"';";
            idMarket = database.getID(queryGetId);
        }
        else
        {
            queryGetId = "select max(idm_a_e) from market_at_entry";
            idMarket = database.getID(queryGetId);
        }

        String query ="insert into stats (symb, idMarket, winloss, side, openPrice, closePrice, dolProfit, percent_result, ProfitR, ProfitAdjR, dayOfWeek, month, holdDays) " +
                "values ('"+symb+"',"+idMarket+",'"+winLoss+"','"+side+"',"+openPrice+","+closePrice+","+result+","+ percentResult+","+resultR+","+resultAdjr+",'"+dayOfWekk+"','"+month+"',"+holdDays+")";
        database.insert(query);
    }
    public void insertRisk(Risk risk)
    {
        double oneR=risk.getOneR();
        double dolRisk = risk.getDolRisk();
        double riskPerUnits = risk.getRiskPerUnits();
        double rTaken = risk.getRTaken();
        double adjR = risk.getAdjR();
        boolean insideR = risk.isInsideR();
        int idStats = getIdStats();

        String query ="insert into risk (oneR, dol_Risk, risk_per_unit, r_taken, adjR, inside_R, idStats) " +
                "values ("+oneR+","+dolRisk+","+riskPerUnits+","+rTaken+","+adjR+","+insideR+","+idStats+")";
        database.insert(query);
    }

    public void insertTrade(Trades trades) {
        try {
            char side = trades.getSide();
            double price = trades.getPrice();
            int units = trades.getUnits();
            int idStats = getIdStats();
            String date = trades.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date theDate = sdf.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(theDate.getTime());
            String query = "insert into trades (side,idStats, price, units, date) " +
                    "values('" + side + "'," +idStats+","+ price + "," + units + ",'" + sqlDate + "')";
            database.insert(query);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void insertSymbStats(SymbStats s)
    {
        double adr = s.getAdr();
        double rangevAtr = s.getRangeVatr();
        boolean insideDay = s.isInsideDayPrior();
        double rangePriorDay = s.getRangevsAdrPriorDay();
        double distToMa10 = s.getAtrToMa10();
        double distToMa20 = s.getAtrToMa20();
        double avgDolVol = s.getAvgDolVol();
        int id = getIdStats();
        String query = "insert into symb_stats(adr, rangeVATR, InsideDayPrior, RangeVSatrPriorDay, ATRDistToMa10, ATRDistToMa20, AvgDolVol, idStats)"+
                "values ("+adr+","+rangevAtr+","+ insideDay+","+rangePriorDay+","+distToMa10+","+distToMa20+","+avgDolVol+","+id+")";
       database.insert(query);
    }
    public void insertMarketInfo(MarketInfo m)
    {
        try {
            if(!checkMarketAtEntryDate(m.getDate()).equals("YES"))
            {
                boolean qOver10 = m.isQqqOver10();
                boolean qOver20 = m.isQqqOver20();
                boolean q10Over20 = m.isQqq10Over20();
                double qAtrDist = m.getQqqAtrDist();

                boolean sOver10 = m.isSpyOver10();
                boolean sOver20 = m.isSpyOver20();
                boolean s10Over20 = m.isSpy10Over20();
                double sAtrDist = m.getSpyAtrDist();

                boolean iOver10 = m.isIwmOver10();
                boolean iOver20 = m.isIwmOver20();
                boolean i10Over20 = m.isIwm10Over20();
                double iAtrDist = m.getIwmAtrDistk();

                String date = m.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date theDate = sdf.parse(date);
                java.sql.Date sqlDate = new java.sql.Date(theDate.getTime());
                String query = "insert into market_at_entry (date, QQQ_Over_10, QQQ_Over_20, Q10_Over_Q20, Q_Atr_Dist, SPY_Over_10, SPY_Over_20, S10_Over_S20, S_Atr_Dist, IWM_Over_10, IWM_Over_20, I10_Over_I20, I_Atr_Dist)" +
                        "values ( '" + sqlDate + "'," + qOver10 + "," + qOver20 + "," + q10Over20 + "," + qAtrDist + "," + sOver10 + "," + sOver20 + "," + s10Over20 + "," + sAtrDist + "," + iOver10 + "," + iOver20 + "," + i10Over20 + "," + iAtrDist + ")";
                database.insert(query);
            }
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public LinkedHashMap<String,Double> getAllDolResultLinked(){
      String query = "select date, dolProfit, symb from trades_schema.market_at_entry \n" +
              "join trades_schema.stats on market_at_entry.idm_a_e = stats.idMarket\n" +
              "order by date;";
      return database.getLinkedHashMapOfResult(query);
    }

    public Risk getRiskForStats(int id)
    {
        String query = "select * from risk where idStats = "+id;
        return database.getOneRiskObject(query);
    }

    public ArrayList<Stats> topForATimePeriod(String startDate, String endDate, int top, String sortWay)
    {
        String query ="select stats.*, market_at_entry.date from stats join market_at_entry on idMarket=idm_a_e where date between"+
                "'"+startDate +"' and '"+ endDate +"' order by dolProfit " + sortWay +" limit "+top;
        return database.getTopStats(query);
    }
    public ArrayList<Stats> getTopFromStats(int top)
    {
        String query ="select * from stats order by dolProfit desc limit "+top;
        ArrayList<Stats> s =  database.getTopStats(query);
        return s;
    }
    public ArrayList<Trades> readTradesWithId(int id)
    {
        String query ="select * from trades where idstats = " +id;
        return database.getStatsForTrade(query);
    }
    public String checkMarketAtEntryDate(String date)
    {
        String exists = "";
        String query = "select date from market_at_entry";
        ArrayList<Date> dates = database.returnDate(query);

        for(int i =0; i< dates.size();i++)
        {
            if(dates.get(i).toString().equals(date))
            {
                exists = "YES";
            }
        }
        return exists;
    }

    public MarketInfo getMarketInfo(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date theDate = sdf.parse(date);
        java.sql.Date sqlDate = new java.sql.Date(theDate.getTime());
        String query = "select * from market_at_entry where date = '" +sqlDate+"';";
        MarketInfo market = database.getMarketInfoAtDate(query);
        return market;
    }
    public double get10LastTrades()
    {
        double result = 0;
        ArrayList<Portfolio> holdInfo = readPortfolioInfo();
        if(!holdInfo.isEmpty())
        {
            String query ="select dolprofit from stats limit 10";
            result = database.getWinLoss(query);
        }
        return result;
    }


    //Returns the result of the MAs in portfolio. If its not enough data it returns 0
    public double getMa10()
    {
        double result = 0;
        ArrayList<Portfolio> holdInfo = readPortfolioInfo();
        if(holdInfo.size() >=10)
        {
            String query ="SELECT AVG(subquery.latest_value) AS average_latest_value FROM ( SELECT value "
            + "AS latest_value FROM portfolio ORDER BY idportfolio DESC LIMIT 10) AS subquery";
            result = database.getDoubleValue(query);
        }
        return result;
    }
    public double getMa20()
    {
        double result = 0;
        ArrayList<Portfolio> holdInfo = readPortfolioInfo();
        if(holdInfo.size() >=20) {
            String query = "SELECT AVG(subquery.latest_value) AS average_latest_value FROM ( SELECT value "
                    + "AS latest_value FROM portfolio ORDER BY idportfolio DESC LIMIT 20) AS subquery";
            result = database.getDoubleValue(query);
        }
        return result;
    }
    public double getMaxDrawdown()
    {
        String query = "Select max(drawdown) from portfolio";
        return database.getDoubleValue(query);
    }
    public double getPortfolioMaxValue()
    {
        double result = 0;
        ArrayList<Portfolio> holdInfo = readPortfolioInfo();
        if(holdInfo.size() >0)
            {
                String query = "select max(value) from portfolio";
                result = database.getDoubleValue(query);
            }
            return result;

    }
    public double getMaxMinR(String type){
        String query;
        if (type.equals("MAX"))
            query = "Select max(ProfitR) from stats";
        else
            query = "Select min(ProfitR) from stats";
        return database.getDoubleValue(query);
    }
    public double getPortFolioCurrentValue()
    {
        double result = 0;
        ArrayList<Portfolio> holdInfo = readPortfolioInfo();
        if(holdInfo.size() >0)
        {
            String query = "select value from portfolio ORDER BY idportfolio desc limit 1";
            result = database.getDoubleValue(query);
        }
        return result;
    }
    public double getStartValue()
    {
        String query = "select value from portfolio order by idportfolio asc limit 1";
        return database.getDoubleValue(query);
    }
}

