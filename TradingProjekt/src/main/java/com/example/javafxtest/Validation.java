package com.example.javafxtest;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Validation {


    public Validation(){

    }

    public boolean CheckNumberTextfields(TextField input) {

        boolean test = true;
        String text = input.getText();
        if (!text.matches("\\d*\\.?\\d*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter only numeric values.");
            alert.showAndWait();
            test = false;

        }
        return test;
    }
    public boolean CheckNumberString(String text) {

        boolean test = true;
        if (!text.matches("\\d*\\.?\\d*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter only numeric values.");
            alert.showAndWait();
            test = false;

        }
        return test;
    }
    public boolean dateSelected (DatePicker datePicker)
    {
        boolean test = true;
        if (datePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Date");
            alert.setHeaderText(null);
            alert.setContentText("Please select a date.");
            alert.showAndWait();
            test = false;
        }
        return test;
    }

    public boolean notAMinusValue(float value)
    {
        boolean test = true;
        if (value <0)
        {
            test = false;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid value");
            alert.setHeaderText(null);
            alert.setContentText("You cant sell or buy more than what you have");
            alert.showAndWait();
            test = false;
        }
        return test;
    }

    public boolean notZero(double value)
    {
        if(value==0)
        {
            return false;
        }
        else
            return true;
    }
}
