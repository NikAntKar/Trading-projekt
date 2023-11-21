package InfoWindow;
import com.example.javafxtest.FormatterClass;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "id",
        "symb",
        "side",
        "openPrice",
        "unitsLeft",
        "currentPrice",
        "units",
        "stop",
        "lod",
        "hod",
        "thirdSell",
        "adr",
        "atrTarget",
        "risk",
        "totalRisk",
        "threR",
        "sixR",
        "nineR",
        "openR",
        "openAdjR",
        "takenR",
        "adjCalcR",
        "atrTarget",
        "range",
        "closePrice",

        // Add other property names in the desired order
})


public class OpenPos {
    public OpenPos()
    {

    }

    public OpenPos(int id, String ticker, double price, int units, double lod, double risk, double totalRisk, double hod, double stop,
                   char side, double adr, double range, double atr, DatePicker datePicker, double oneR, double takenR, double adjR) {
        FormatterClass f = new FormatterClass();
        LocalDate localDate = datePicker.getValue();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        this.id = id;
        this.symb = ticker;
        this.lod = lod;
        this.hod = hod;
        this.adr = adr;
        this.range = range;
        this.unitsLeft = units;
        this.thirdSell = units/3;
        this.openPrice = price;
        this.startUnits = units;
        stops.add(stop);
        this.stop = stop;
        this.currentPrice = price;
        addUnits(units);
        date.add(formattedDate);
        addPrice(price);
        addSide(side);
        this.risk = risk ;
        this.totalRisk = totalRisk;
        this.takenR= takenR;
        this.calcAdjR = adjR;
        this.atr = atr;
        this.currentR = oneR;
        openR = f.formatDoubleXX((risk*units)/ oneR);
        if(side == 'B') {
            this.side = 'B';
            threR = f.formatDoubleXX(price + (risk*3));
            sixR = f.formatDoubleXX(price + (risk*6));
            nineR = f.formatDoubleXX(price + (risk*9));
            atrTarget =  f.formatDoubleXX(lod + (atr*3));
        }
        else
        {
            this.side = 'S';
            threR = f.formatDoubleXX(price + (risk*3-1));
            sixR = f.formatDoubleXX((price - (risk*6*-1)));
            nineR = f.formatDoubleXX((price - (risk*9*-1)));
            atrTarget =f.formatDoubleXX( hod - (atr*3));
        }
    }
    ArrayList<Character> sides = new ArrayList<Character>();
    ArrayList <Double> price = new ArrayList<Double>();
    ArrayList <Integer> units = new ArrayList<Integer>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList <Double> stops = new ArrayList<Double>();
    int id;
    String symb;
    double range;
    double atr;
    double risk;
    double currentR;
    double openPrice;
    double closePrice;
    double lod;
    double hod;
    int unitsLeft;
    int thirdSell;
    double atrTarget;
    double stop;
    double currentPrice;
    double takenR;
    double totalRisk;
    double calcAdjR;
    int startUnits;
    double adr;
    char side;
    double threR;
    double sixR;
    double nineR;
    double openR;
    double openAdjR;

    public void removeTransaction(int index)
    {
        System.out.println("sides 1 " +sides);
        sides.remove(index);
        units.remove(index);
        price.remove(index);
        date.remove(index);
        System.out.println("sides 2 " +sides);
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTotalRisk(double totalRisk) {
        this.totalRisk = totalRisk;
    }
    public void setTakenR(double takenR) {
        this.takenR = takenR;
    }
    public void setCalcAdjR(double calcAdjR) {
        this.calcAdjR = calcAdjR;
    }
    public void setSides(ArrayList<Character> sides) {
        this.sides = sides;
    }
    public void setDate(ArrayList<String> date) {
        this.date = date;
    }
    public void setSideAtIndex(int index, Character c)
    {
        sides.set(index, c);
    }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    public void setSymb(String symb) {
        this.symb = symb;
    }
    public void setUnitsLeft(int unitsLeft) {
        this.unitsLeft = unitsLeft;
    }
    public void setOpenPrice(double price)
    {
        this.openPrice = price;
    }
    public void setStartUnits(int units)
    {
        this.startUnits = units;
    }
    public void setUnits(ArrayList<Integer> units) {
        this.units = units;
    }
    public void setPrice(ArrayList<Double> prices)
    {
        this.price = prices;
    }
    public void setStops(ArrayList<Double> stops) {
        this.stops = stops;
    }
    public void setRange(double range) {
        this.range = range;
    }
    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }
    public void setLod(double lod) {
        this.lod = lod;
    }
    public void setHod(double hod) {
        this.hod = hod;
    }
    public void setThirdSell(int thirdSell) {
        this.thirdSell = thirdSell;
    }
    public void setAtrTarget(double atrTarget) {
        this.atrTarget = atrTarget;
    }
    public void setStop(double stop) {
        this.stop = stop;
    }
    public void setAdr(double adr) {
        this.adr = adr;
    }
    public void setSide(char side) {
        this.side = side;
    }
    public void setThreR(double threR) {
        this.threR = threR;
    }
    public void setSixR(double sixR) {
        this.sixR = sixR;
    }
    public void setNineR(double nineR) {
        this.nineR = nineR;
    }
    public void setOpenR(double openR) {
        this.openR = openR;
    }
    public void setOpenAdjR(double openAdjR) {
        this.openAdjR = openAdjR;
    }

    public void setAtr(double atr) {
        this.atr = atr;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public void setCurrentR(double currentR) {
        this.currentR = currentR;
    }

    // Add
    public void addSide(char theSide)
    {
        sides.add(theSide);
    }
    public void addPrice (double thePrice)
    {
        price.add(thePrice);
    }
    public void addUnits(int theUnits)
    {
        units.add(theUnits);
    }
    public void addDate(String theDate)
    {
        date.add(theDate);
    }




    //Getters


    public ArrayList<Double> getStops() {
        return stops;
    }
    public ArrayList<Double> getPrice() {
        return price;
    }
    public ArrayList<Character> getSides() {
        return sides;
    }
    public double getTotalRisk() {
        return totalRisk;
    }
    public int  getId() {
        return id;
    }
    public String getSymb() {
        return symb;
    }
    public char getSide() {
        return side;
    }
    public double getTakenR() {
        return takenR;
    }
    public double getCalcAdjR() {
        return calcAdjR;
    }
    public double getAtr() {
        return atr;
    }
    public double getRisk() {
        return risk;
    }
    public String getDateAtIndex(int index)
    {
        return date.get(index);
    }
    public char getSideAtindex(int index)
    {
        return sides.get(index);
    }
    public int getUnitsAtIndex(int index)
    {
        return units.get(index);
    }
    public ArrayList<String> getDate() {
        return date;
    }
    public ArrayList<Integer> getUnits() {
        return units;
    }
    public double getPriceAtIndex(int index)
    {
        return getPrice().get(index);
    }
    public double getRange() {
        return range;
    }
    public double getClosePrice() {
        return closePrice;
    }
    public double getOpenPrice() {
        return openPrice;
    }
    public double getCurrentPrice() {
        return currentPrice;
    }
    public double getStop() {
        return stop;
    }
    public int getStartUnits() {
        return startUnits;
    }
    public double getLod() {
        return lod;
    }
    public double getHod() {
        return hod;
    }
    public int getUnitsLeft() {
        return unitsLeft;
    }
    public int getThirdSell() {
        return thirdSell;
    }

    public double getCurrentR() {
        return currentR;
    }

    public double getAtrTarget() {
        return atrTarget;
    }
    public double getAdr() {
        return adr;
    }
    public double getThreR() {
        return threR;
    }
    public double getSixR() {
        return sixR;
    }
    public double getNineR() {
        return nineR;
    }
    public double getOpenR() {
        return openR;
    }
    public double getOpenAdjR() {
        return openAdjR;
    }
}
