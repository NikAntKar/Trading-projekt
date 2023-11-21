package ConfirmWindow;

import InfoWindow.OpenPos;

public class DataHolder {

    private static final DataHolder instance = new DataHolder();

    private OpenPos openPos;

    private DataHolder(){

    }
    public static DataHolder getInstance()
    {
        return instance;
    }
    public OpenPos getOpenPos()
    {
        return openPos;
    }
    public void setOpenPos(OpenPos o )
    {
        openPos = o;
    }
}
