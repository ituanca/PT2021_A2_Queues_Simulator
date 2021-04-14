package controller;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Server;
import model.SimulationManager;
import view.Interface;
import model.Client;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QueuesSimulatorController {

    public TextField tfNumberOfClients;
    public TextField tfNumberOfQueues;
    public TextField tfSimulationInterval;
    public TextField tfMinArrivalTime;
    public TextField tfMaxArrivalTime;
    public TextField tfMinServiceTime;
    public TextField tfMaxServiceTime;
    public Button btnStartSimulation;
    public String shortestWaitingTime;
    public String smallestNumberOfClients;
    public ComboBox cbSelectionPolicy;
    public TextArea textArea;



    SimulationManager simulationManager;

    public void start(){ new Thread(() -> Application.launch(Interface.class)).start(); }

    private boolean validateInput(){
        if(tfNumberOfClients.getText().equals("") || tfNumberOfQueues.getText().equals("") || tfSimulationInterval.getText().equals("") ||
                tfMinArrivalTime.getText().equals("") || tfMaxArrivalTime.getText().equals("") || tfMinServiceTime.getText().equals("") || tfMaxServiceTime.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all the fields");
            alert.show();
            return false;
        }
        if(getNumberOfClients() < 0 || getNumberOfQueues() < 0 || getSimulationInterval() < 0 || getMinArrivalTime() < 0 || getMaxArrivalTime() < 0 || getMinServiceTime() < 0 || getMaxServiceTime() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in valid data");
            alert.show();
            return false;
        }
        if(getMinArrivalTime() > getMaxArrivalTime()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose a maximum arrival time larger than the minimum arrival time");
            alert.show();
            return false;
        }
        if(getMinServiceTime() > getMaxServiceTime()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose a maximum service time larger than the minimum service time");
            alert.show();
            return false;
        }
        if(getNumberOfClients() == -1 || getNumberOfQueues() == -1 || getSimulationInterval() == -1 || getMinServiceTime() == -1 || getMaxServiceTime() == -1 || getMinArrivalTime() == -1 || getMaxArrivalTime() == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in valid data");
            alert.show();
            return false;
        }
        if(cbSelectionPolicy.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose a selection policy");
            alert.show();
            return false;
        }
       return true;
    }

    @FXML
    public void startSimulation(ActionEvent actionEvent) throws InterruptedException {
        actionEvent.consume();
        textArea.setVisible(true);
        if(validateInput()){
            simulationManager = new SimulationManager(getNumberOfClients(), getNumberOfQueues(), getSimulationInterval(),
                    getMinArrivalTime(), getMaxArrivalTime(), getMinServiceTime(), getMaxServiceTime(), getSelectionPolicy(), textArea);
            Thread t = new Thread(simulationManager);
            t.start();
        }
    }


    private Integer getNumberOfClients() {
        try {
            return Integer.parseInt(tfNumberOfClients.getText());
        }catch(NumberFormatException nfe){
            return -1;
        }
    }

    private Integer getNumberOfQueues() {
        try {
            return Integer.parseInt(tfNumberOfQueues.getText());
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private Integer getSimulationInterval() {
        try {
            return Integer.parseInt(tfSimulationInterval.getText());
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private Integer getMinArrivalTime() {
        try {
            return Integer.parseInt(tfMinArrivalTime.getText());
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private Integer getMaxArrivalTime() {
        try {
            return Integer.parseInt(tfMaxArrivalTime.getText());
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private Integer getMinServiceTime() {
        try {
            return Integer.parseInt(tfMinServiceTime.getText());
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private Integer getMaxServiceTime() {
        try {
            return Integer.parseInt(tfMaxServiceTime.getText());
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private String getSelectionPolicy() { return (String)cbSelectionPolicy.getValue(); }

}
