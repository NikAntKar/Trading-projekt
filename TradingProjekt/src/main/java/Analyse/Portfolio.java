package Analyse;

import DataBase.DatabaseController;

public class Portfolio {

    public Portfolio(int id, String date, double value, double result, double tenAvg, double twentyAvg, double drawdown, double idStats)
    {
        this.id =id;
        this.date = date;
        this.value = value;
        this.result = result;
        this.tenAvg =tenAvg;
        this.twentyAvg = twentyAvg;
        this.drawdown = drawdown;
        this. idStats = idStats;
    }
    public Portfolio()
    {
        setUpValue();
    }

    int id;
    String date;
    double value;
    double result;
    double tenAvg;
    double twentyAvg;
    double drawdown;
    double idStats;
    private double oneR;

    public double getValue() {
        return value;
    }

    public double getOneR() {
        return oneR;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getResult() {
        return result;
    }

    public double getTenAvg() {
        return tenAvg;
    }

    public double getTwentyAvg() {
        return twentyAvg;
    }

    public double getDrawdown() {
        return drawdown;
    }

    public double getIdStats() {
        return idStats;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public void setTenAvg(double tenAvg) {
        this.tenAvg = tenAvg;
    }

    public void setTwentyAvg(double twentyAvg) {
        this.twentyAvg = twentyAvg;
    }

    public void setDrawdown(double drawdown) {
        this.drawdown = drawdown;
    }

    public void setIdStats(double idStats) {
        this.idStats = idStats;
    }

    public void setOneR(double oneR) {
        this.oneR = oneR;
    }


    public void setUpValue()
    {
        DatabaseController database = new DatabaseController();
        setValue(database.getPortFolioCurrentValue());
        setOneR(value*0.01);
    }
}
