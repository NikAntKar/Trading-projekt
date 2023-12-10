package com.example.javafxtest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;


public class ApiCall {



    public ApiCall(String ticker) {
        this.sym = ticker;
        GetSetAdrAtrHodLod(sym);

    }


    public ApiCall() {


    }
    public void setHod(double hod) {
        this.hod = hod;
    }
    public void setLod(double lod) {
        this.lod = lod;
    }
    public void setAdr(double adr) {
        this.adr = adr;
    }
    public void setAtr( double atr){this.atr = atr;}
    public double getLod() {
        return lod;
    }
    public double getHod() {
        return hod;
    }
    public double getAdr() {
        return adr;
    }

    double price;
    double lod = 0;
    double hod = 0;
    double adr = 0;
    double atr = 0;
    double ydHod=0;
    double ma10 = 0;
    double ma20 = 0;
    double ydLod=0;
    double historicalPrice;
    double ydRange=0;
    boolean insideDayPrior;
    public double getAtr() {
        return atr;
    }


    String sym = "";
    String apiKey1 = "ae5fac5be266486dbe36ae33f5276850";
    String apiKey2 = "a3e5ed854b7e4f5985120c4f02889099";
    String apiKey3 ="5f6b8dcb1fbb4d8283e3cb0f6ab5c023";
    String apiKey4 = "2c45d681aef449829b666073ce8f7e90";

    public void setInsideDayPrior(boolean insideDayPrior) {
        this.insideDayPrior = insideDayPrior;
    }
    public void setMa10(double ma)
    {
        ma10 = ma;
    }

    public void setMa20(double ma20) {
        this.ma20 = ma20;
    }

    public double getMa10() {
        return ma10;
    }

    public double getMa20() {
        return ma20;
    }

    public double getHistoricalPrice() {
        return historicalPrice;
    }

    public void setHistoricalPrice(double historicalPrice) {
        this.historicalPrice = historicalPrice;
    }

    public boolean isInsideDayPrior() {
        return insideDayPrior;
    }

    public double getYdRange() {
        return ydRange;
    }

    public void setYdRange(double ydRange) {
        this.ydRange = ydRange;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double GetCurrentPrice(String ticker)
    {
        double price =0;
        try {
            String urlen = "https://api.twelvedata.com/price?symbol=" + ticker + "&apikey=" + apiKey1;
            JSONObject jsonObject = connectionObject(urlen);
            String priceString = (String) jsonObject.get("price");
            if(Objects.isNull(priceString)) {
                urlen = "https://api.twelvedata.com/price?symbol=" + ticker + "&apikey="+ apiKey2;
                jsonObject = connectionObject(urlen);
                priceString = (String) jsonObject.get("price");
            }
            if(Objects.isNull(priceString)) {
                urlen = "https://api.twelvedata.com/price?symbol=" + ticker + "&apikey="+ apiKey3;
                jsonObject = connectionObject(urlen);
                priceString = (String) jsonObject.get("price");
            }
            if(Objects.isNull(priceString)) {
                urlen = "https://api.twelvedata.com/price?symbol=" + ticker + "&apikey="+ apiKey4;
                jsonObject = connectionObject(urlen);
                priceString = (String) jsonObject.get("price");
            }
            price = Double.parseDouble(priceString.toString());
        }
        catch(Exception e){
            System.out.println(e);
        }
        return price;
    }


    //Try with thread and async
//    public class ThreadGetCurrentPrice extends Thread
//    {
//        public ThreadGetCurrentPrice(String ticker, ApiCall api)
//        {
//            this.ticker = ticker;
//            this.api = api;
//        }
//        ApiCall api;
//        String ticker;
//        double price;
//        @Override
//        public void run()
//        {
//            try {
//                String urlen = "https://api.twelvedata.com/price?symbol=" + ticker + "&apikey=" + apiKey1;
//                JSONObject jsonObject = connectionObject(urlen);
//                String priceString = (String) jsonObject.get("price");
//                if(Objects.isNull(priceString)) {
//                    urlen = "https://api.twelvedata.com/price?symbol=" + ticker + "&apikey="+ apiKey2;
//                    jsonObject = connectionObject(urlen);
//                    priceString = (String) jsonObject.get("price");
//                }
//                price = Double.parseDouble(priceString.toString());
//                setPrice(price);
//                System.out.println(getPrice());
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }

//        public double getPrice() {
//            return price;
//        }
//
//        public void setPrice(double price) {
//            this.price = price;
//        }
//    }


    public double GetOpen(String ticker){
            double open =0;
            try {
                String urlen = "https://api.twelvedata.com/quote?symbol=" + ticker + "&apikey=" + apiKey1;
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(urlen);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responsecontent = EntityUtils.toString(entity);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(responsecontent);
                    String priceString = (String) jsonObject.get("open");
                    open = Double.parseDouble(priceString);
                } else {
                    System.err.println("Api doesnt responde");
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
            return open;
        };


    public JSONObject connectionString(String url)
    {

        JSONObject returnValue = new JSONObject();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String responsecontent = EntityUtils.toString(entity);
                JSONParser parser = new JSONParser();
                returnValue = (JSONObject) parser.parse(responsecontent);
            } else {
                System.err.println("API call failed with status code: " + response.getStatusLine().getStatusCode());
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return returnValue;
    }
    public JSONObject connectionObject(String url)
    {
        JSONObject jsonObject = new JSONObject();

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String responsecontent = EntityUtils.toString(entity);
                JSONParser parser = new JSONParser();
                 jsonObject = (JSONObject) parser.parse(responsecontent);
            } else {
                System.err.println("API call failed with status code: " + response.getStatusLine().getStatusCode());
            }
        }
            catch(Exception e){
            System.out.println(e);
        }
        return jsonObject;
    }

    public double GetMa10(String ticker, String date){
        double Ma = 0;
        String startDate = date;
        String endDate = date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parseDate = LocalDate.parse(endDate, formatter);
        LocalDate nextDay = parseDate.plusDays(1);
        endDate = nextDay.format(formatter);

        try {
            String urlen = "https://api.twelvedata.com/ma?symbol=" + ticker + "&interval=1day&time_period=10&start_date="+startDate+"&apikey=" + apiKey1;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlen);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                String responsecontent = EntityUtils.toString(entity);
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(responsecontent);
                // Get the first "values" entry
                JSONArray dataArray = (JSONArray) jsonObject.get("values");
                if (!dataArray.isEmpty()) {
                    if (!dataArray.isEmpty()) {
                        JSONObject firstEntry = (JSONObject) dataArray.get(0);
                        String ma = (String) firstEntry.get("ma");
                        Ma = Double.parseDouble(ma);
                    }
                    else
                        System.out.println("JsonArray is empty in GetMa10 method");
                }
                    else {
                    System.err.println("No data in the 'values' array.");
                }
            } else {
                System.err.println("API call failed with status code: " + response.getStatusLine().getStatusCode());
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return Ma;
    }
    public double GetMa20(String ticker, String date ){
            double Ma = 0;
            String startDate = date;
            String endDate = date;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parseDate = LocalDate.parse(endDate, formatter);
            LocalDate nextDay = parseDate.plusDays(1);
            endDate = nextDay.format(formatter);
        try {
            String urlen = "https://api.twelvedata.com/ma?symbol=" + ticker + "&interval=1day&time_period=20&apikey=" + apiKey1;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlen);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                String responsecontent = EntityUtils.toString(entity);
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(responsecontent);
                // Get the first "values" entry
                JSONArray dataArray = (JSONArray) jsonObject.get("values");
                if (!dataArray.isEmpty()) {
                    if (!dataArray.isEmpty()) {
                        JSONObject firstEntry = (JSONObject) dataArray.get(0);
                        String ma = (String) firstEntry.get("ma");
                        Ma = Double.parseDouble(ma);
                    }
                    else
                        System.out.println("JsonArray is empty in GetMa20 method");


                } else {
                    System.err.println("No data in the 'values' array.");
                }
            } else {
                System.err.println("API call failed with status code: " + response.getStatusLine().getStatusCode());
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return Ma;
    }


    public double GetAvgDolVol(String ticker, String date){
        double avgDolVol =0;
        //LocalDate today  = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parseDate = LocalDate.parse(date, formatter);
        LocalDate currentDate = parseDate.minusDays(1);
        LocalDate startDate =  calculateWorkingDaysInPast(parseDate, 10);
        int times = 0;
        double sumVol =0;
        try {
            String urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&end_date="+currentDate+"&start_date="+startDate+"&interval=1day&&apikey=" + apiKey1;
            JSONObject jsonObject = connectionObject(urlen);
            JSONArray dataArray = (JSONArray) jsonObject.get("values");
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&end_date="+currentDate+"&start_date="+startDate+"&interval=1day&&apikey=" + apiKey2;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&end_date="+currentDate+"&start_date="+startDate+"&interval=1day&&apikey=" + apiKey3;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&end_date="+currentDate+"&start_date="+startDate+"&interval=1day&&apikey=" + apiKey4;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
                if (!dataArray.isEmpty()) {
                for (Object item : dataArray) {
                    JSONObject jsonItem = (JSONObject) item;
                    String close = (String) jsonItem.get("close");
                    String volume = (String) jsonItem.get("volume");
                    double doubleClose = Double.parseDouble(close);
                    double doublevolume = Double.parseDouble(volume);
                    sumVol =sumVol+ (doubleClose * doublevolume);
                    times ++;
                }

                }
                else
                    System.out.println("JsonArray is empty in GetAvgDolVol method");

                avgDolVol = (sumVol/times)/1000000;
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedAvgDolVolString = decimalFormat.format(avgDolVol);
                avgDolVol = Double.parseDouble(formattedAvgDolVolString);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return avgDolVol;
    }

    public void setYDActions(String ticker, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parseDate = LocalDate.parse(date, formatter);
        LocalDate daysBack =  calculateWorkingDaysInPast(parseDate, 1);
        LocalDate endDate = parseDate.plusDays(1);
        double firstHod =0;
        double firstLod = 0;
        double secondHod =0;
        double secondLod = 0;
        try {
            String urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + daysBack + "&end_date=" + endDate+"&interval=1day&&apikey=" + apiKey1;
            JSONObject jsonObject = connectionObject(urlen);
            JSONArray dataArray = (JSONArray) jsonObject.get("values");
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + daysBack + "&end_date=" + endDate+"&interval=1day&&apikey=" + apiKey2;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + daysBack + "&end_date=" + endDate+"&interval=1day&&apikey=" + apiKey3;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }    if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + daysBack + "&end_date=" + endDate+"&interval=1day&&apikey=" + apiKey4;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
            if (!dataArray.isEmpty()) {
                int index  =0;
            for (Object item : dataArray) {
                JSONObject jsonItem = (JSONObject) item;
                String lowString = (String) jsonItem.get("low");
                String highString = (String) jsonItem.get("high");
                String closeString = (String) jsonItem.get("close");
                double doubleClose = Double.parseDouble(closeString);
                double doubleLow = Double.parseDouble(lowString);
                double doubleHigh = Double.parseDouble(highString);
                if(index==0)
                {
                    setLod(doubleLow);
                    setHod(doubleHigh);
                }
                if(index == 1)
                {
                    secondHod = doubleHigh;
                    secondLod = doubleLow;
                }
                if(index == 2)
                {
                    firstHod = doubleHigh;
                    firstLod = doubleLow;
                }
                index++;
            }
            if(firstHod>secondHod && firstLod<secondLod)
            {
                setInsideDayPrior(true);
            }
            else{
                setInsideDayPrior(false);
            }
            FormatterClass f = new FormatterClass();
            setYdRange(f.formatDoubleXXXX(secondHod-secondLod));
            }
            else
                System.out.println("JsonArray is empty in setYDActions method");
            }
         catch(Exception e){
            System.out.println(e);
        }
    }
    public double Get5MinAdr(String ticker, int period)  {
        double adr= 0;
        int times = 0;
        String tid = getNewYorkTime(period);
        try {
            String urlen = "https://api.twelvedata.com/time_series?&symbol=" + ticker + "&interval=5min&&start_date="+tid+"&apikey=" + apiKey1;
            JSONObject jsonObject = connectionObject(urlen);
            JSONArray dataArray = (JSONArray) jsonObject.get("values");
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?&symbol=" + ticker + "&interval=5min&&start_date="+tid+"&apikey=" + apiKey2;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?&symbol=" + ticker + "&interval=5min&&start_date="+tid+"&apikey=" + apiKey3;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
            if(dataArray==null)
            {
                urlen = "https://api.twelvedata.com/time_series?&symbol=" + ticker + "&interval=5min&&start_date="+tid+"&apikey=" + apiKey4;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }

            if (!dataArray.isEmpty()) {
                for (Object item : dataArray) {
                    JSONObject jsonItem = (JSONObject) item;
                    String lowString = (String) jsonItem.get("low");
                    String highString = (String) jsonItem.get("high");
                    double doubleLow = Double.parseDouble(lowString);
                    double doubleHigh = Double.parseDouble(highString);
                    adr = adr + (doubleHigh - doubleLow);
                    times ++;
                }}
            else
                System.out.println("JsonArray is empty in Get5MinAdr method");
        }
        catch(Exception e){
            System.out.println(e);
        }
        adr = adr /times;
        DecimalFormat df = new DecimalFormat("0.00");
        String formattValueStr = df.format(adr);
        double formatAdr = Double.parseDouble(formattValueStr);

        return formatAdr;

    }
    public double getHistoricalPerformance(String ticker, String startDate, String endDate)
    {
        LocalDate parseStartDate = LocalDate.parse(startDate);
        LocalDate parseEndDate = LocalDate.parse(endDate);
        parseEndDate = parseEndDate.plusDays(1);
        double performance =0;
        double startValue =0;
        double endValue =0;
        String urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + parseStartDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" + apiKey1;
        JSONObject jsonObject = connectionObject(urlen);
        JSONArray dataArray = (JSONArray) jsonObject.get("values");
        if(dataArray==null)
        {
            urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + parseStartDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" +apiKey2;
            jsonObject = connectionObject(urlen);
            dataArray = (JSONArray) jsonObject.get("values");
        }
        if(dataArray==null)
        {
            urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + parseStartDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" +apiKey3;
            jsonObject = connectionObject(urlen);
            dataArray = (JSONArray) jsonObject.get("values");
        }
        if(dataArray==null)
        {
            urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + parseStartDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" +apiKey4;
            jsonObject = connectionObject(urlen);
            dataArray = (JSONArray) jsonObject.get("values");
        }
        int index = 0;
        for (Object item : dataArray) {
            JSONObject jsonItem = (JSONObject) item;
            String closeString = (String) jsonItem.get("close");
            double doubleClose = Double.parseDouble(closeString);
            if(index == 0)
            {
                endValue = doubleClose;
                index++;
            }
            startValue = doubleClose;

        }
        performance = (endValue-startValue)/startValue*100;
        return performance;
    }
    public void getSetAtrMa(String ticker, String date)
    {
        LocalDate currentDate = LocalDate.now();
        LocalDate parseEndDate = LocalDate.parse(date);
        LocalDate startDate = LocalDate.parse(date);

        if(currentDate.equals(parseEndDate))
        {
            parseEndDate = parseEndDate.plusDays(1);
            startDate = calculateWorkingDaysInPast(parseEndDate, 19);
        }
        else
        {
            parseEndDate = parseEndDate.plusDays(1);
             startDate = calculateWorkingDaysInPast(parseEndDate, 20);
        }
        int indexTen = 0;
        int i=0;
        double sumMaTen = 0.0 ;
        double sumMaTwenty = 0.0 ;

        String urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" + apiKey1;
        JSONObject jsonObject = connectionObject(urlen);
        JSONArray dataArray = (JSONArray) jsonObject.get("values");
            if(dataArray==null)
        {
            urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" +apiKey2;
            jsonObject = connectionObject(urlen);
            dataArray = (JSONArray) jsonObject.get("values");
        }
        if(dataArray==null)
        {
            urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" +apiKey3;
            jsonObject = connectionObject(urlen);
            dataArray = (JSONArray) jsonObject.get("values");
        }
        if(dataArray==null)
        {
            urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&end_date=" + parseEndDate + "&interval=1day&&apikey=" +apiKey4;
            jsonObject = connectionObject(urlen);
            dataArray = (JSONArray) jsonObject.get("values");
        }
            ArrayList<Double> closes = new ArrayList<Double>();
            ArrayList<Double> highs = new ArrayList<Double>();
            ArrayList<Double> lows = new ArrayList<Double>();
            ArrayList<Double> maxValue = new ArrayList<Double>();
            int k =0;
        for (Object item : dataArray) {
            JSONObject jsonItem = (JSONObject) item;
            String closeString = (String) jsonItem.get("close");
            double doubleClose = Double.parseDouble(closeString);
            String closeOpen = (String) jsonItem.get("open");
            double doubleOpen = Double.parseDouble(closeOpen);
            String lowString = (String) jsonItem.get("low");
            String highString = (String) jsonItem.get("high");
            double doubleLow = Double.parseDouble(lowString);
            double doubleHigh = Double.parseDouble(highString);
            if (i > 4) {
                highs.add(doubleHigh);
                lows.add(doubleLow);
            }
            if (i > 5) {
                closes.add(doubleClose);
                maxValue.add(calcAtr(closes.get(k), highs.get(k), lows.get(k)));
                k++;
            }
            if (indexTen == 0) {
                setHistoricalPrice(doubleOpen);
            }
            if (i < 10) {
                sumMaTen = sumMaTen + doubleClose;
                indexTen++;
            }
            sumMaTwenty = sumMaTwenty + doubleClose;
            i++;
            }
            double sumRange = 0;
            for (int l = 0; l < maxValue.size(); l++) {
                sumRange += maxValue.get(l);
            }
            atr = sumRange / maxValue.size();
            for (int f = 0; f < maxValue.size(); f++) {
                double currentTrueRange = maxValue.get(f);
                atr = ((atr * (maxValue.size() - 1)) + currentTrueRange) / maxValue.size();
            }
        FormatterClass f = new FormatterClass();
        setMa10(f.formatDoubleXXXX(sumMaTen / indexTen));
        setMa20(f.formatDoubleXXXX(sumMaTwenty / i));
        setAtr(atr);
    }

    public void GetSetAdrAtrHodLod(String ticker){
    double adr = 0;
    double times =0;
    double atr = 0;
    LocalDate today  = LocalDate.now();
    LocalDate startDate =  calculateWorkingDaysInPast(today, 18);
    try {
        String urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&interval=1day&apikey=" + apiKey1;
        JSONObject jsonObject = connectionObject(urlen);
        JSONArray dataArray = (JSONArray) jsonObject.get("values");
        if(dataArray == null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&interval=1day&apikey=" + apiKey2;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
        if(dataArray == null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&interval=1day&apikey=" + apiKey3;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
        if(dataArray == null)
            {
                urlen = "https://api.twelvedata.com/time_series?symbol=" + ticker + "&start_date=" + startDate + "&interval=1day&apikey=" + apiKey4;
                jsonObject = connectionObject(urlen);
                dataArray = (JSONArray) jsonObject.get("values");
            }
            if (!dataArray.isEmpty()) {

                ArrayList<Double> closes = new ArrayList<Double>();
                ArrayList<Double> highs = new ArrayList<Double>();
                ArrayList<Double> lows = new ArrayList<Double>();
                ArrayList<Double> maxValue = new ArrayList<Double>();
                int i = 0;
                int k = 0;
                for (Object item : dataArray) {
                    JSONObject jsonItem = (JSONObject) item;
                    String lowString = (String) jsonItem.get("low");
                    String highString = (String) jsonItem.get("high");
                    String closeString = (String) jsonItem.get("close");
                    double doubleClose = Double.parseDouble(closeString);
                    double doubleLow = Double.parseDouble(lowString);
                    double doubleHigh = Double.parseDouble(highString);
                    if (i > 4) {
                        highs.add(doubleHigh);
                        lows.add(doubleLow);
                    }
                    if (i > 5) {
                        closes.add(doubleClose);
                        maxValue.add(calcAtr(closes.get(k), highs.get(k), lows.get(k)));
                        k++;
                    }
                    if (i != 0) {
                        adr = adr + (doubleHigh / doubleLow);
                        times++;
                    } else {
                        setLod(doubleLow);
                        setHod(doubleHigh);
                    }
                    i++;

                }
                double sumRange = 0;
                for (int l = 0; l < maxValue.size(); l++) {
                    sumRange += maxValue.get(l);
                }
                atr = sumRange / maxValue.size();
                for (int f = 0; f < maxValue.size(); f++) {
                    double currentTrueRange = maxValue.get(f);
                    atr = ((atr * (maxValue.size() - 1)) + currentTrueRange) / maxValue.size();
                }

                adr = (adr / times - 1) * 100;
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedAvgDolVolString = decimalFormat.format(adr);
                adr = Double.parseDouble(formattedAvgDolVolString);
            }
            else
                System.out.println("JsonArray is empty in GetSetAdrAtrHodLod method");
            }
            catch(Exception e){
                System.out.println(e);
            }
        setAdr(adr);
        setAtr(atr);
        }
        public double calcAtr(double close, double high, double low)
        {
            double value1 = high - low;
           double value2 = high - close;
           double value3 = close - low;
           double greatest = Math.max(value1, Math.max(value2, value3));
           return greatest;
        }
    public static LocalDate calculateWorkingDaysInPast(LocalDate startDate, int workingDays) {
        LocalDate currentDate = startDate.minusDays(1); // Start from one day before the given date
        int i = 0 ;
        while (workingDays > 0) {
            currentDate = currentDate.minusDays(1);
            // Check if the current date is a working day (e.g., excluding weekends)
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays--;
            }
        }
        return currentDate;
    }
    public static String getNewYorkTime(int periods) {
        // Define the time zone for New York (Eastern Time)
        ZoneId newYorkZone = ZoneId.of("America/New_York");

        // Get the current date and time in New York
        ZonedDateTime newYorkTime = ZonedDateTime.now(newYorkZone);

        // Define the restricted trading time window (9:30 to 15:55)
        LocalTime openingTime = LocalTime.of(9, 30);
        LocalTime closingTime = LocalTime.of(15, 55);

        // Calculate the duration of one 5-minute interval
        Duration fiveMinutes = Duration.ofMinutes(5);

        for (int i = 0; i < periods; i++) {
            // Subtract 5 minutes
            newYorkTime = newYorkTime.minus(fiveMinutes);

            // Check if the time is within the trading time window
            LocalTime currentTime = newYorkTime.toLocalTime();
            if (currentTime.isBefore(openingTime)) {
                // If before the opening time, set the time to the previous day's closing time
                newYorkTime = newYorkTime.minusDays(1).withHour(closingTime.getHour()).withMinute(closingTime.getMinute()).withSecond(0);
            } else if (currentTime.isAfter(closingTime)) {
                // If after the closing time, set the time to the same day's closing time
                newYorkTime = newYorkTime.withHour(closingTime.getHour()).withMinute(closingTime.getMinute()).withSecond(0);
            } else {
                // Set the seconds to "00"
                newYorkTime = newYorkTime.withSecond(0);
            }
        }

        // Check if the prior day or today is a weekend
        while (newYorkTime.getDayOfWeek() == DayOfWeek.SATURDAY || newYorkTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            // If it's a weekend, subtract a day to go to the previous day
            newYorkTime = newYorkTime.minusDays(1);
        }

        // If the previous day is Sunday, go back to the previous Friday
        if (newYorkTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            newYorkTime = newYorkTime.minusDays(2);
        }

        // Format the final time using a desired format (e.g., "yyyy-MM-dd HH:mm z")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd%20HH:mm:ss");
        return newYorkTime.format(formatter);
    }
}





