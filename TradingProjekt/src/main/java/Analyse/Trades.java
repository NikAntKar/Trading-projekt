package Analyse;

public class Trades {

    public Trades(){

    }

    public Trades(int id, int statsID, char side, double price, int units, String date) {
        this.id = id;
        this.statsID = statsID;
        this.side = side;
        this.date = date;
        this.price = price;
        this.units = units;
    }

    int id;
    int statsID;
    char side;
    String date;
    double price;
    int units;

    // Getter
    public int getId() {
        return id;
    }

    public int getStatsID() {
        return statsID;
    }

    public char getSide() {
        return side;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public int getUnits() {
        return units;
    }


    //Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setStatsID(int statsID) {
        this.statsID = statsID;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
