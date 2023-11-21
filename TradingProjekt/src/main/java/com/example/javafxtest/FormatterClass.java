package com.example.javafxtest;

import java.text.DecimalFormat;

public class FormatterClass {

    public FormatterClass(){

    }

    public double formatDoubleXX(double value)
    {
        if ("∞".equals(value)) {
            // Handle the case where input is "∞"
            return Double.POSITIVE_INFINITY; // or any other appropriate value
        } else {
            // Try to parse the input as a double
            DecimalFormat formatter = new DecimalFormat("#.##");
            String fv = formatter.format(value);
            return Double.parseDouble(fv);
        }


    }
    public double formatDouble(double value)
    {

        DecimalFormat formatter = new DecimalFormat("#");
        String fv = formatter.format(value);
        return Double.parseDouble(fv);

    }
    public double formatDoubleX(double value)
    {

        DecimalFormat formatter = new DecimalFormat("#.#");
        String fv = formatter.format(value);
        return Double.parseDouble(fv);

    }
    public double formatDoubleXXX(double value)
    {
        DecimalFormat formatter = new DecimalFormat("#.###");
        String fv = formatter.format(value);
        return Double.parseDouble(fv);
    }
    public double formatDoubleXXXX(double value)
    {
        DecimalFormat formatter = new DecimalFormat("#.####");
        String fv = formatter.format(value);
        return Double.parseDouble(fv);
    }
}
