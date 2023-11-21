package Analyse;

public class Risk {
    public Risk() {
    }

    public Risk(int idRisk, double oneR, double dolRisk, double riskPerUnits, double rTaken, double adjR, boolean insideR) {
        this.idRisk = idRisk;
        this.oneR = oneR;
        this.dolRisk = dolRisk;
        this.riskPerUnits = riskPerUnits;
        this.rTaken = rTaken;
        this.adjR = adjR;
        this.insideR = insideR;
        this.idStats = idStats;
    }

    int idRisk;
    double oneR;
    double dolRisk;
    double riskPerUnits;
    double rTaken;
    double adjR;
    boolean insideR;
    int idStats;


    //Setters

    public void setIdRisk(int idRisk) {
        this.idRisk = idRisk;
    }

    public void setOneR(double oneR) {
        this.oneR = oneR;
    }

    public void setDolRisk(double dolRisk) {
        this.dolRisk = dolRisk;
    }

    public void setRiskPerUnits(double riskPerUnits) {
        this.riskPerUnits = riskPerUnits;
    }

    public void setRTaken(double rTaken) {
        this.rTaken = rTaken;
    }

    public void setAdjR(double adjR) {
        this.adjR = adjR;
    }

    public void setInsideR(boolean insideR) {
        this.insideR = insideR;
    }

    public void setIdStats(int idStats) {
        this.idStats = idStats;
    }

    //GEtters

    public int getIdRisk() {
        return idRisk;
    }

    public double getOneR() {
        return oneR;
    }

    public double getDolRisk() {
        return dolRisk;
    }

    public double getRiskPerUnits() {
        return riskPerUnits;
    }

    public double getRTaken() {
        return rTaken;
    }

    public double getAdjR() {
        return adjR;
    }

    public boolean isInsideR() {
        return insideR;
    }

    public int getIdStats() {
        return idStats;
    }
}
