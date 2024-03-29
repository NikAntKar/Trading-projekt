package ClosedTrades;
import Analyse.CloseTradesController;
import InfoWindow.OpenPos;
import com.example.javafxtest.TopPaneActions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class ClosedTradesView implements Initializable {
    @FXML
    private Label lblAverageAdjR, lblAveragePercent, lblAverageR, lblProfitFactor, lblWinFrequency, lblWinLoss, lblResult, lblPercentResult, lblMaxDrawdown;
    @FXML
    private Label lblAveragePercentWin, lblAveragePercentLoss, lblAverageRWin, lblAverageRLoss, lblAverageAdjRWin, lblAverageAdjRLoss, lblBiggestRWin, lblBiggestRLoss;
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private LineChart<String,Double> lineChart;

    @FXML
    private Button btnDepth;


    public ClosedTradesView()
    {
    }
    CloseTradesController analyse = new CloseTradesController();

    TopPaneActions topPaneActions = new TopPaneActions();
    @FXML
    private void minimize(MouseEvent event)
    {
        topPaneActions.minimize(event);
    }
    @FXML
    private void maximize(MouseEvent event)
    {
        topPaneActions.maximize(event);
    }
    @FXML
    private void close(MouseEvent event)
    {
        topPaneActions.close(event);
    }
    @FXML
    private void handleMoveWindowAction(MouseEvent event)
    {
        topPaneActions.handleMoveWindowAction(event);
    }
    @FXML
    private void handleMovement(MouseEvent event)
    {
        topPaneActions.handleMovement(event);
    }
    public void addNewItems(OpenPos openPos)
    {
        analyse.addMarketInfoToDb(openPos);
        analyse.addStatsToTb(openPos);
        analyse.addTradesToDb(openPos);
        analyse.addStatsToDb(openPos);
        analyse.addRiskToDb(openPos);
        analyse.addPortFolioToDb(openPos);
    }
    public void setUpAll()
    {
        analyse.setUpStatsFromDb();
        analyse.setUpTradesFromDb();
        analyse.setUpSymbStatsFromDb();
        analyse.setUpMarketInfoFromDb();
        analyse.setUpPortfolioFromDb();
        analyse.setUpRiskInfoFromDb();
    }

    public void setUpCharts() {
        analyse.setUpChart(lineChart);
        analyse.setUpBarChart(barChart);
    }

    public void inDepthView(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxtest/inDepthStats-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene( root, 860, 680);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToHomeScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxtest/openPositions-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene( root, 860, 680);
        String css = this.getClass().getResource("/com/example/javafxtest/Style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpAll();
        setUpCharts();
        analyse.setUpResultLabels(lblProfitFactor, lblWinLoss, lblAveragePercent, lblAverageR, lblAverageAdjR, lblWinFrequency, lblResult, lblPercentResult, lblMaxDrawdown);
        analyse.setUpWinLossLabels(lblAveragePercentWin, lblAveragePercentLoss, lblAverageRWin, lblAverageRLoss, lblAverageAdjRWin, lblAverageAdjRLoss, lblBiggestRWin, lblBiggestRLoss);
    }
}
