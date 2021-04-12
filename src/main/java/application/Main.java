package application;

import javafx.application.Application;
import controller.QueuesSimulatorController;
import model.SimulationManager;

public class Main {
    public static void main(String[] args) throws Exception {
        QueuesSimulatorController queuesController = new QueuesSimulatorController();
        queuesController.start();
    }
}
