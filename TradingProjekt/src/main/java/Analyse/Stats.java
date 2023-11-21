package Analyse;

import java.util.ArrayList;

public class Stats {

    public Stats(){

    }

    public Stats(int id, String symb, char winLoss, char side, double openPrice,
                 double closePrice, double result, double percentResult, double resultR, double resultAdjR,
                 String dayOfWeek, String month, int holdDays) {
        this.id = id;
        this.symb = symb;
        this.winLoss = winLoss;
        this.side = side;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.result = result;
        this.percentResult = percentResult;
        this.resultR = resultR;
        this.resultAdjR = resultAdjR;
        this.dayOfWeek = dayOfWeek;
        this.month = month;
        this.holdDays = holdDays;
    }

    int id;
    int Mid;
    String symb;
    char winLoss;
    char side;
    double openPrice;
    double closePrice;
    double result;
    double percentResult;
    double resultR;
    double resultAdjR;
    String dayOfWeek;
    String month;
    int holdDays;




    //Getters
    public int getId() {
        return id;
    }



    public int getMid() {
        return Mid;
    }
    public String getSymb() {
        return symb;
    }
    public char getWinLoss() {
        return winLoss;
    }
    public char getSide() {
        return side;
    }
    public double getOpenPrice() {
        return openPrice;
    }
    public double getClosePrice() {
        return closePrice;
    }
    public double getResult() {
        return result;
    }
    public double getPercentResult() {
        return percentResult;
    }
    public double getResultR() {
        return resultR;
    }
    public double getResultAdjR() {
        return resultAdjR;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public String getMonth() {
        return month;
    }
    public int getHoldDays() {
        return holdDays;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setMid(int mid) {
        Mid = mid;
    }

    public void setSymb(String symb) {
        this.symb = symb;
    }
    public void setWinLoss(char winLoss) {
        this.winLoss = winLoss;
    }

    public void setSide(char side) {
        this.side = side;
    }
    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }
    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public void setResultR(double resultR) {
        this.resultR = resultR;
    }

    public void getPercentResult(double percentResunt) {
        this.percentResult = percentResunt;
    }

    public void setResultAdjR(double resultAdjR) {
        this.resultAdjR = resultAdjR;
    }


    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setHoldDays(int holdDays) {
        this.holdDays = holdDays;
    }
}
