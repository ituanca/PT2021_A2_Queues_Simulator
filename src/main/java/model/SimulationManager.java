package model;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SimulationManager implements Runnable{

    public int noOfClients;
    public int noOfServers;
    public int timeLimit;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;
    public boolean simulationIsFinished;
    public SelectionPolicy selectionPolicy;
    public final Scheduler scheduler;
    public final ArrayList<Client> generatedClients = new ArrayList<>();
    public final Statistics statistics;
    public TextArea textArea;
    private final static String NEW_LINE = "\n";

    public SimulationManager(int noOfClients, int noOfServers, int timeLimit, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, String selectionPolicyString, TextArea textArea) {
        this.noOfClients = noOfClients;
        this.noOfServers = noOfServers;
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.simulationIsFinished = false;
        this.textArea = textArea;
        establishSelectionPolicy(selectionPolicyString);
        createClientsArray();
        this.scheduler = new Scheduler(noOfServers, selectionPolicy);
        this.statistics = new Statistics();
        for(Client client : generatedClients){
            statistics.addServiceTime(client);
        }
        for(int i = 0; i < noOfServers; i++){
            scheduler.getServers().get(i).getQueueThread().start();
        }
    }

    public void establishSelectionPolicy(String selectionPolicy){
        if(selectionPolicy.equals("shortest waiting time")){
            this.selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        }else{
            this.selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
        }
    }

    public void createClientsArray(){
        RandomClientsGenerator randomClientsGenerator = new RandomClientsGenerator();
        randomClientsGenerator.createClientsArray(generatedClients, noOfClients, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
        sortClientsArray();
    }

    private void sortClientsArray(){ Collections.sort(generatedClients); }

    public void writeInputData(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write("N: " + noOfClients);
            bw.newLine();
            bw.write("Q: " + noOfServers);
            bw.newLine();
            bw.write("simulation time: " + timeLimit);
            bw.newLine();
            if(selectionPolicy == SelectionPolicy.SHORTEST_QUEUE){
                bw.write("selection policy: shortest number of clients");
            }else{
                bw.write("selection policy: shortest waiting time");
            }
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeWaitingClients(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            int count = 0;
            if(generatedClients.size() > 0) {
                bw.write("Waiting clients: ");
                bw.newLine();
                for (Client client : generatedClients) {
                    bw.write("(" + client.getID() + ", " + client.getArrivalTime() + ", " + client.getServiceTime() + ") ");
                    count++;
                    if (count > 9) {
                        count = 0;
                        bw.newLine();
                    }
                }
                if (count != 0) {
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeServers(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            for (Server server : scheduler.getServers()) {
                writeServer(fileName, server);
            }
            bw.write("-------------------------------------------------------------------------------------------------------------------");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeServer(String fileName, Server server) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write("Queue " + server.getQueueIndex() + ": ");
            int count = 0;
            if(server.getStatus().equals(Server.CLOSED)){
                bw.write(server.status);
            }else{
                for(Client client: server.getClients()){
                    if (count > 9) {            // print max 10 servers in a row
                        count = 0;
                        bw.newLine();
                    }
                    bw.write("(" + client.getID() + ", " + client.getArrivalTime() + ", " + client.getServiceTime() + ") ");
                    count++;
                }
            }
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSimulationTime(String fileName, int simulationTime) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write("Time " + simulationTime);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeStatisticsResults(String fileName, Statistics statistics){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write("Statistics results:");
            bw.newLine();
            bw.write("average waiting time: " + statistics.changeToDecimalFormat(statistics.averageWaitingTime));
            bw.newLine();
            bw.write("average service time: " + statistics.changeToDecimalFormat(statistics.averageServiceTime));
            bw.newLine();
            bw.write("peak hour: " + statistics.peakHour);
            bw.newLine();
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doStatistics(String currentFileName, int maxNumberOfClientsInQueuesAtATime){
        statistics.computeAverageWaitingTime();
        System.out.println("average waiting time: " + statistics.changeToDecimalFormat(statistics.averageWaitingTime));
        statistics.computeAverageServiceTime();
        System.out.println("average service time: " + statistics.changeToDecimalFormat(statistics.averageServiceTime));
        System.out.println("peak hour: " + statistics.peakHour + " (" +  maxNumberOfClientsInQueuesAtATime + " clients)");
        writeStatisticsResults(currentFileName, statistics);
    }

    public void writeCurrentDataToTheTextArea(TextArea textArea, int simulationTime, ArrayList<Client> clients){
        writeSimulationTimeToTheTextArea(textArea, simulationTime);
        writeWaitingClientsToTheTextArea(textArea, clients);
        writeServersToTheTextArea(textArea);
    }

    public void writeSimulationTimeToTheTextArea(TextArea textArea, int simulationTime){
        textArea.appendText("Time: " + simulationTime + NEW_LINE);
    }

    public void writeWaitingClientsToTheTextArea(TextArea textArea, ArrayList<Client> clients){
        int count = 0;
        if(clients.size() > 0) {
            textArea.appendText("Waiting clients:" + NEW_LINE);
            for (Client client : clients) {
                textArea.appendText(" (" + client.getID() + ", " + client.getArrivalTime() + ", " + client.getServiceTime() + ") ");
                count++;
                if (count > 7) {
                    count = 0;
                    textArea.appendText(NEW_LINE);
                }
            }
            if (count != 0) {
                textArea.appendText(NEW_LINE);
            }
        }
    }

    public void writeServersToTheTextArea(TextArea textArea){
        for (Server server : scheduler.getServers()) {
            writeServerToTheTextArea(textArea, server);
        }
        textArea.appendText("-------------------------------------------------------------------------------------");
        textArea.appendText(NEW_LINE);
    }

    public void writeServerToTheTextArea(TextArea textArea, Server server){
        textArea.appendText("Queue " + server.getQueueIndex() + ": ");
        int count = 0;
        if(server.getStatus().equals(Server.CLOSED)){
            textArea.appendText(server.status);
        }else{
            for(Client client: server.getClients()){
                if (count > 7) {            // print max 6 servers in a row
                    count = 0;
                    textArea.appendText(NEW_LINE);
                }
                textArea.appendText("(" + client.getID() + ", " + client.getArrivalTime() + ", " + client.getServiceTime() + ") ");
                count++;
            }
        }
        textArea.appendText(NEW_LINE);
    }

    public void writeStatisticsToTheTextArea(TextArea textArea, String averageWaitingTime, String averageServiceTime, Integer peakHour){
        textArea.appendText("Statistics:" + NEW_LINE);
        textArea.appendText("average waiting time: " + averageWaitingTime + NEW_LINE);
        textArea.appendText("average service time: " + averageServiceTime + NEW_LINE);
        textArea.appendText("peak hour: " + peakHour + NEW_LINE);
        textArea.appendText(NEW_LINE);
        textArea.appendText("Simulation completed successfully.");
    }


    @Override
    public void run() {
        int currentTime = 0;
        String logFileName = "src/main/java/logs/log.txt";
        String test1FileName = "src/main/java/logs/test1.txt";
        String test2FileName = "src/main/java/logs/test2.txt";
        String test3FileName = "src/main/java/logs/test3.txt";
        String currentFileName = logFileName;
        int maxNumberOfClientsInQueuesAtATime = 0;

        writeInputData(currentFileName);
        while(currentTime <= timeLimit) {
            if(!generatedClients.isEmpty() || !scheduler.checkIfAllTheServersAreEmpty()) {
                Iterator<Client> i = generatedClients.iterator();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (i.hasNext()) {
                    Client client = i.next();
                    if (client.getArrivalTime() == currentTime) {
                        try {
                            scheduler.dispatchClient(client, statistics);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i.remove();
                    } else {
                        if (client.getArrivalTime() > currentTime) {
                            break;
                        }
                    }
                }
                if (scheduler.computeNoOfClientsCurrentlyInQueues() > maxNumberOfClientsInQueuesAtATime) {
                    maxNumberOfClientsInQueuesAtATime = scheduler.computeNoOfClientsCurrentlyInQueues();
                    statistics.setPeakHour(currentTime);
                }
                writeSimulationTime(currentFileName, currentTime);
                writeWaitingClients(currentFileName);
                writeServers(currentFileName);
                int finalCurrentTime = currentTime;
                Platform.runLater(() -> writeCurrentDataToTheTextArea(textArea, finalCurrentTime, generatedClients));
            }
            currentTime++;
        }
        try {
            Thread.sleep(1000);
            doStatistics(currentFileName, maxNumberOfClientsInQueuesAtATime);
            Platform.runLater(() -> writeStatisticsToTheTextArea(textArea, statistics.changeToDecimalFormat(statistics.averageWaitingTime),
                    statistics.changeToDecimalFormat(statistics.averageServiceTime), statistics.peakHour));
            scheduler.stopServers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
