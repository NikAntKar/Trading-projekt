package Analyse;

public class SymbStats {

    public SymbStats()
    {

    }

    public SymbStats(int id, double adr, double rangeVatr, boolean insideDayPrior, double rangevsAtrPriorDay, double atrToMa10, double atrToMa20, double avgDolVol, int statsId) {
        this.id = id;
        this.adr = adr;
        this.rangeVatr = rangeVatr;
        this.insideDayPrior = insideDayPrior;
        this.rangevsAtrPriorDay = rangevsAtrPriorDay;
        this.atrToMa10 = atrToMa10;
        this.atrToMa20 = atrToMa20;
        this.avgDolVol = avgDolVol;
        this.statsId = statsId;
    }

    int id;
    int statsId;
    double adr;
    double rangeVatr;
    boolean insideDayPrior;
    double rangevsAtrPriorDay;
    double atrToMa10;
    double atrToMa20;
    double avgDolVol;

    //Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setStatsId(int statsId) {
        this.statsId = statsId;
    }

    public void setAdr(double adr) {
        this.adr = adr;
    }

    public void setRangeVatr(double rangeVatr) {
        this.rangeVatr = rangeVatr;
    }

    public void setInsideDayPrior(boolean insideDayPrior) {
        this.insideDayPrior = insideDayPrior;
    }

    public void setRangevsAtrPriorDay(double rangevsAtrPriorDay) {
        this.rangevsAtrPriorDay = rangevsAtrPriorDay;
    }

    public void setAtrToMa10(double adrToMa10) {
        this.atrToMa10 = adrToMa10;
    }

    public void setAtrToMa20(double adrToMa20) {
        this.atrToMa20 = adrToMa20;
    }

    public void setAvgDolVol(double avgDolVol) {
        this.avgDolVol = avgDolVol;
    }

    //Getters

    public int getId() {
        return id;
    }

    public int getStatsId() {
        return statsId;
    }

    public double getAdr() {
        return adr;
    }


    public double getRangeVatr() {
        return rangeVatr;
    }

    public boolean isInsideDayPrior() {
        return insideDayPrior;
    }

    public double getRangevsAdrPriorDay() {
        return rangevsAtrPriorDay;
    }

    public double getAtrToMa10() {
        return atrToMa10;
    }

    public double getAtrToMa20() {
        return atrToMa20;
    }

    public double getAvgDolVol() {
        return avgDolVol;
    }
}
