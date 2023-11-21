package Analyse;

public class MarketInfo {

    public MarketInfo()
    {

    }

    public MarketInfo(int id, String date, boolean qqqOver10, boolean qqqOver20, boolean qqq10Over20,
                      double qPercentMa10, boolean spyOver10, boolean spyOver20, boolean spy10Over20,
                      double sPercentMa10, boolean iwmOver10, boolean iwmOver20, boolean iwm10Over20, double iPercentMa10) {
        this.id = id;
        this.date = date;
        this.qqqOver10 = qqqOver10;
        this.qqqOver20 = qqqOver20;
        this.qqq10Over20 = qqq10Over20;
        this.qqqAtrDist = qPercentMa10;
        this.spyOver10 = spyOver10;
        this.spyOver20 = spyOver20;
        this.spy10Over20 = spy10Over20;
        this.spyAtrDist = sPercentMa10;
        this.iwmOver10 = iwmOver10;
        this.iwmOver20 = iwmOver20;
        this.iwm10Over20 = iwm10Over20;
        this.iwmAtrDistk = iPercentMa10;
    }

    int id;
    String date;
    boolean qqqOver10;
    boolean qqqOver20;
    boolean qqq10Over20;
    double qqqAtrDist;
    boolean spyOver10;
    boolean spyOver20;
    boolean spy10Over20;
    double spyAtrDist;
    boolean iwmOver10;
    boolean iwmOver20;
    boolean iwm10Over20;
    double iwmAtrDistk;


    //Seter
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setQqqOver10(boolean qqqOver10) {
        this.qqqOver10 = qqqOver10;
    }
    public void setQqqOver20(boolean qqqOver20) {
        this.qqqOver20 = qqqOver20;
    }
    public void setQqq10Over20(boolean qqq10Over20) {
        this.qqq10Over20 = qqq10Over20;
    }
    public void setQqqAtrDist(double qqqAtrDist) {
        this.qqqAtrDist = qqqAtrDist;
    }
    public void setSpyOver10(boolean spyOver10) {
        this.spyOver10 = spyOver10;
    }
    public void setSpyOver20(boolean spyOver20) {
        this.spyOver20 = spyOver20;
    }
    public void setSpy10Over20(boolean spy10Over20) {
        this.spy10Over20 = spy10Over20;
    }

    public void setSpyAtrDist(double spyAtrDist) {
        this.spyAtrDist = spyAtrDist;
    }

    public void setIwmOver10(boolean iwmOver10) {
        this.iwmOver10 = iwmOver10;
    }

    public void setIwmOver20(boolean iwmOver20) {
        this.iwmOver20 = iwmOver20;
    }

    public void setIwm10Over20(boolean iwm10Over20) {
        this.iwm10Over20 = iwm10Over20;
    }

    public void setIwmAtrDistk(double iwmAtrDistk) {
        this.iwmAtrDistk = iwmAtrDistk;
    }

    //Getter

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public boolean isQqqOver10() {
        return qqqOver10;
    }

    public boolean isQqqOver20() {
        return qqqOver20;
    }

    public boolean isQqq10Over20() {
        return qqq10Over20;
    }

    public double getQqqAtrDist() {
        return qqqAtrDist;
    }

    public boolean isSpyOver10() {
        return spyOver10;
    }

    public boolean isSpyOver20() {
        return spyOver20;
    }

    public boolean isSpy10Over20() {
        return spy10Over20;
    }

    public double getSpyAtrDist() {
        return spyAtrDist;
    }

    public boolean isIwmOver10() {
        return iwmOver10;
    }

    public boolean isIwmOver20() {
        return iwmOver20;
    }

    public boolean isIwm10Over20() {
        return iwm10Over20;
    }

    public double getIwmAtrDistk() {
        return iwmAtrDistk;
    }
}
