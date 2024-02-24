package DataBase;
import Analyse.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MyJDBC {

    public MyJDBC() {
        connect();
    }

    Connection connection;

    public void setConnection(Connection c) {
        this.connection = c;
    }

    public void connect() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/trades_schema",
                    "root",
                    "Februariv1234"
                );
            setConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void insert (String query ) {

        try{
            Statement statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(query);
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<java.util.Date> returnDate(String query)
    {
        ArrayList<java.util.Date> dates = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                dates.add(resultSet.getDate("date"));
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }


    public int getID(String query)
    {
        int id = 0;
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public ArrayList<Trades> getStatsForTrade(String query)
    {
        ArrayList<Trades> t = new ArrayList<Trades>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int idTrades = rs.getInt("idTrades");
                int idStats = rs.getInt("idStats");
                char side = (rs.getString("side")).charAt(0);
                double price = rs.getDouble("price");
                int units = rs.getInt("units");
                String date = rs.getDate("date").toString();
                Trades holdTrades = new Trades(idTrades, idStats,side,price,units,date);
                t.add(holdTrades);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    public double getDoubleValue(String query)
    {
        double result = 0;
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                result = resultSet.getDouble(1);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public double getWinLoss(String query)
    {
        double result = 0;
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                double holdResult;
                holdResult = resultSet.getDouble("dolProfit");
                if(holdResult >0)
                    result += result;
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void delete(String query)
    {
        try{
            Statement statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Risk getOneRiskObject(String query)
    {
        Risk r = new Risk();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int idRisk = rs.getInt("idRisk");
                double oneR = rs.getDouble("oneR");
                double dolRisk = rs.getDouble("dol_Risk");
                double riskPerUnits = rs.getDouble("risk_per_unit");
                double rTaken = rs.getDouble("r_taken");
                double adjR  = rs.getDouble("adjR");
                boolean insideR =rs.getBoolean("inside_R");
                int idStats = rs.getInt("idStats");
                Risk holdRisk= new Risk(idRisk,oneR,dolRisk,riskPerUnits,rTaken,adjR,insideR);
                holdRisk.setIdStats(idStats);
                r= holdRisk;
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public LinkedHashMap<String,Double> getLinkedHashMapOfResult(String query)
    {
        LinkedHashMap<String,Double> holdValue = new LinkedHashMap<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            double result;
            String date;
            while(rs.next()){
                result = rs.getDouble("dolProfit");
                date = rs.getString("date");
                    if (holdValue.containsKey(date)) {
                        result += holdValue.get(date);
                    }
                System.out.println(date);
                //holdValue.put(rs.getString("date").toString(), rs.getDouble("dolProfit"));
                holdValue.put(date, result);
            }
            statement.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return holdValue;
    }

    public ArrayList<Stats> getTopStats(String query)
    {
        ArrayList<Stats> s = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int statsId = rs.getInt("idStats");
                int idMarket = rs.getInt("idMarket");
                String symb = rs.getString("symb");
                char winLoss = (rs.getString("WinLoss")).charAt(0);
                char side = (rs.getString("side")).charAt(0);
                double openPrice = rs.getDouble("openPrice");
                double closePrice = rs.getDouble("closePrice");
                double dolProfit = rs.getDouble("dolProfit");
                double percentResult = rs.getDouble("percent_result");
                double profitR = rs.getDouble("profitR");
                double profitAdjR  = rs.getDouble("ProfitAdjR");
                String dayOfWeek =rs.getString("dayOfWeek");
                String month =rs.getString("month");
                int holdDays = rs.getInt("holdDays");
                Stats holdStats = new Stats(statsId,symb,winLoss,side,openPrice,closePrice,dolProfit,percentResult,profitR,profitAdjR,dayOfWeek,month,holdDays );
                holdStats.setMid(idMarket);
                s.add(holdStats);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
    // Readers
    public ArrayList<Trades> readTrades () {
        ArrayList<Trades> t = new ArrayList<Trades>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM TRADES");
            while (rs.next()) {
                int idTrades = rs.getInt("idTrades");
                int idStats = rs.getInt("idStats");
                char side = (rs.getString("side")).charAt(0);
                double price = rs.getDouble("price");
                int units = rs.getInt("units");
                String date = rs.getDate("date").toString();
                Trades holdTrades = new Trades(idTrades, idStats,side,price,units,date);
                t.add(holdTrades);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
    public ArrayList<Stats> readStats () {
        ArrayList<Stats> s = new ArrayList<Stats>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM STATS");
            while (rs.next()) {
                int statsId = rs.getInt("idStats");
                int idMarket = rs.getInt("idMarket");
                String symb = rs.getString("symb");
                char winLoss = (rs.getString("WinLoss")).charAt(0);
                char side = (rs.getString("side")).charAt(0);
                double openPrice = rs.getDouble("openPrice");
                double closePrice = rs.getDouble("closePrice");
                double dolProfit = rs.getDouble("dolProfit");
                double percentResult = rs.getDouble("percent_result");
                double profitR = rs.getDouble("profitR");
                double profitAdjR  = rs.getDouble("ProfitAdjR");
                String dayOfWeek =rs.getString("dayOfWeek");
                String month =rs.getString("month");
                int holdDays = rs.getInt("holdDays");
                Stats holdStats = new Stats(statsId,symb,winLoss,side,openPrice,closePrice,dolProfit,percentResult,profitR,profitAdjR,dayOfWeek,month,holdDays );
                holdStats.setMid(idMarket);
                s.add(holdStats);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
    public ArrayList<Risk> readRisk () {
        ArrayList<Risk> r = new ArrayList<Risk>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM RISK");
            while (rs.next()) {
                int idRisk = rs.getInt("idRisk");
                double oneR = rs.getDouble("oneR");
                double dolRisk = rs.getDouble("dol_Risk");
                double riskPerUnits = rs.getDouble("risk_per_unit");
                double rTaken = rs.getDouble("r_taken");
                double adjR  = rs.getDouble("adjR");
                boolean insideR =rs.getBoolean("inside_R");
                int idStats = rs.getInt("idStats");
                Risk holdRisk= new Risk(idRisk,oneR,dolRisk,riskPerUnits,rTaken,adjR,insideR);
                holdRisk.setIdStats(idStats);
                r.add(holdRisk);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public ArrayList<Portfolio> readPortfolio ()
    {
        ArrayList<Portfolio> array = new ArrayList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM PORTFOLIO");
            while (rs.next()) {
                int idPortfolio = rs.getInt("idPortfolio");
                String date = rs.getDate("date").toString();
                double value = (rs.getDouble("value"));
                double result = (rs.getDouble("result"));
                double tenAvg = rs.getDouble("tenAvg");
                double twentyAvg = rs.getDouble("twentyAvg");
                double drawDown = rs.getDouble("drawDown");
                int idStats = rs.getInt("idStats");
                Portfolio holdPortfolio = new Portfolio(idPortfolio,date,value,result,tenAvg,twentyAvg,drawDown,idStats );
                array.add(holdPortfolio);
            }
            rs.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return array;
    }
    public ArrayList<SymbStats> readSymbStats () {
        ArrayList<SymbStats> s = new ArrayList<SymbStats>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM symb_stats");
            while (rs.next()) {
                int idSymbStats = rs.getInt("idSymbStats");
                double adr = rs.getDouble("adr");
                double rangeVATR = rs.getDouble("rangeVATR");
                boolean insideDayPrior = rs.getBoolean("InsideDayPrior");
                double rangeVsatrPriorDay = rs.getDouble("rangeVsatrPriorDay");
                double ATRdistToMa10 = rs.getDouble("ATRdistToMa10");
                double ATRdistToMa20 = rs.getDouble("ATRdistToMa20");
                double avgDolvol = rs.getDouble("avgDolvol");
                int idStats = rs.getInt("idStats");
                SymbStats holdSymbStats = new SymbStats(idSymbStats, adr,rangeVATR,insideDayPrior,rangeVsatrPriorDay,ATRdistToMa10
                        ,ATRdistToMa20,avgDolvol,idStats);
                s.add(holdSymbStats);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
    public ArrayList<MarketInfo> readMarketInfo () {
        ArrayList<MarketInfo> m = new ArrayList<MarketInfo>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM MARKET_AT_ENTRY");
            while (rs.next()) {
                int idMarket = rs.getInt("idm_a_e");
                String date = rs.getDate("date").toString();
                boolean qqq_over_10 = rs.getBoolean("qqq_over_10");
                boolean qqq_over_20 = rs.getBoolean("qqq_over_20");
                boolean q10_Over_q20 = rs.getBoolean("q10_Over_q20");
                double qqqAtrDist = rs.getDouble("Q_Atr_dist");

                boolean spy_over_10 = rs.getBoolean("spy_over_10");
                boolean spy_over_20 = rs.getBoolean("spy_over_20");
                boolean s10_Over_s20 = rs.getBoolean("s10_Over_s20");
                double spyAtrDist = rs.getDouble("S_Atr_Dist");

                boolean iwm_over_10 = rs.getBoolean("iwm_over_10");
                boolean iwm_over_20 = rs.getBoolean("iwm_over_20");
                boolean i10_Over_i20 = rs.getBoolean("i10_Over_i20");
                double iwmAtrDist = rs.getDouble("I_Atr_Dist");

                MarketInfo holdMarketInfo = new MarketInfo(idMarket, date,qqq_over_10,qqq_over_20,q10_Over_q20,qqqAtrDist
                        ,spy_over_10,spy_over_20,s10_Over_s20,spyAtrDist,iwm_over_10,iwm_over_20,i10_Over_i20,iwmAtrDist);
                m.add(holdMarketInfo);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
    public MarketInfo getMarketInfoAtDate(String query)
    {
        MarketInfo m = new MarketInfo();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int idMarket = rs.getInt("idm_a_e");
                String date = rs.getDate("date").toString();
                boolean qqq_over_10 = rs.getBoolean("qqq_over_10");
                boolean qqq_over_20 = rs.getBoolean("qqq_over_20");
                boolean q10_Over_q20 = rs.getBoolean("q10_Over_q20");
                double qqqAtrDist = rs.getDouble("Q_Atr_dist");

                boolean spy_over_10 = rs.getBoolean("spy_over_10");
                boolean spy_over_20 = rs.getBoolean("spy_over_20");
                boolean s10_Over_s20 = rs.getBoolean("s10_Over_s20");
                double spyAtrDist = rs.getDouble("S_Atr_Dist");

                boolean iwm_over_10 = rs.getBoolean("iwm_over_10");
                boolean iwm_over_20 = rs.getBoolean("iwm_over_20");
                boolean i10_Over_i20 = rs.getBoolean("i10_Over_i20");
                double iwmAtrDist = rs.getDouble("I_Atr_Dist");

                MarketInfo holdMarketInfo = new MarketInfo(idMarket, date,qqq_over_10,qqq_over_20,q10_Over_q20,qqqAtrDist
                        ,spy_over_10,spy_over_20,s10_Over_s20,spyAtrDist,iwm_over_10,iwm_over_20,i10_Over_i20,iwmAtrDist);
                m = holdMarketInfo;
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
}
