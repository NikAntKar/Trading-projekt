package InfoWindow;

import com.example.javafxtest.FormatterClass;

public class PotentialPos {

    public PotentialPos(){

    }


    public PotentialPos(String symb, double price, double hod, double lod, double risk, int units, double adr, double adr5min, double atr, double threeRtarget, char side, double stop) {

        FormatterClass f = new FormatterClass();
        this.symb = symb;
        this.price = price;
        this.hod = hod;
        this.lod = lod;
        this.risk = risk;
        this.atr = atr;
        this.units = units;
        this.adr = adr;
        this.adr5min = adr5min;
        this.side = side;
        this.stop = stop;
        this.threeRtarget = threeRtarget;
        if(side=='B')
        {
            if(price > 0 || lod > 0 || atr > 0)
                rangeVAtr = f.formatDoubleXX((price-lod)/atr);
        }
        else
        {
            if(price > 0 || hod > 0 || atr > 0)
                rangeVAtr = f.formatDoubleXX((hod-price)/atr);
        }
    }
    double rangeVAtr;
    String symb;
    double price;
    double hod;
    double lod;
    double risk;
    int units;
    double atr;
    double threeRtarget;
    double stop;
    double adr;
    double adr5min;
    public void setRangeVAtr(double rangeVAtr) {
        this.rangeVAtr = rangeVAtr;
    }
    public void setSymb(String symb) {
        this.symb = symb;
    }
    public void setHod(double hod) {
        this.hod = hod;
    }
    public void setLod(double lod) {
        this.lod = lod;
    }
    public void setAtr(double atr) {
        this.atr = atr;
    }
    public void setThreeRtarget(double threeRtarget) {
        this.threeRtarget = threeRtarget;
    }
    public void setAdr(double adr) {
        this.adr = adr;
    }
    public void setAdr5min(double adr5min) {
        this.adr5min = adr5min;
    }
    public void setSide(char side) {
        this.side = side;
    }
    char side;
    public void setPrice(double price) {
        this.price = price;
    }
    public void setUnits(int units) {
        this.units = units;
    }
    public void setRisk(double risk) {
        this.risk = risk;
    }
    public void setStop(double stop) {
        this.stop = stop;
    }
    public double getStop() {
        return stop;
    }
    public char getSide() {
        return side;
    }
    public double getAdr5min() {
        return adr5min;
    }
    public double getRangeVAtr() {
        return rangeVAtr;
    }

    public double getAtr() {
        return atr;
    }

    public double getThreeRtarget() {
        return threeRtarget;
    }
    public String getSymb() {
        return symb;
    }
    public double getPrice() {
        return price;
    }
    public double getHod() {return hod;}
    public double getLod() {
        return lod;
    }
    public double getRisk() {
        return risk;
    }
    public double getUnits() {
        return units;
    }
    public double getAdr() {
        return adr;
    }
}
