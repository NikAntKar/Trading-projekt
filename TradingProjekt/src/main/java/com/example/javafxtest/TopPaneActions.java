package com.example.javafxtest;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TopPaneActions {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void minimize(MouseEvent event)
    {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    @FXML
    public void maximize(MouseEvent event)
    {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setFullScreen(true);

    }
    @FXML
    public void close(MouseEvent event)
    {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.close();
    }

    @FXML
    public void handleMoveWindowAction(MouseEvent event)
    {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        xOffset = s.getX() - event.getScreenX();
        yOffset= s.getY() - event.getScreenY();
    }
    @FXML
    public  void handleMovement(MouseEvent event)
    {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setX(event.getScreenX() + xOffset);
        s.setY(event.getScreenY() + yOffset);
    }


}
