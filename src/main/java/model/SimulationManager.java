package model;

import controller.QueuesSimulatorController;

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

    public SelectionPolicy selectionPolicy;
    private final Scheduler scheduler;
    private final ArrayList<Client> generatedClients = new ArrayList<>();
    private QueuesSimulatorController controller = new QueuesSimulatorController();

    public SimulationManager(int noOfClients, int noOfServers, int timeLimit, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, String selectionPolicyString) {
        this.noOfClients = noOfClients;
        this.noOfServers = noOfServers;
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        establishSelectionPolicy(selectionPolicyString);
        createClientsArray();
        this.scheduler = new Scheduler(noOfServers, generatedClients, selectionPolicy);
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
        for (Client client : generatedClients){
            client.setStatus(Client.NOT_YET_IN_QUEUE);
        }
        sortClientsArray();
    }

    private void sortClientsArray(){ Collections.sort(generatedClients); }

    private void printInputData(){
        System.out.println("N: " + noOfClients);
        System.out.println("Q: " + noOfServers);
        System.out.println("simulation time: " + timeLimit);
        System.out.println("selection policy: " + selectionPolicy);
    }

    private void printClients(){
        System.out.println("Clients:");
        int count = 0;
        for (Client client : generatedClients) {
            System.out.print("(" + client.getID() + ", " + client.getArrivalTime() + ", " + client.getServiceTime() + ") ");
            count++;
            if(count == 10) {
                count = 0;
                System.out.println();
            }
        }
        if(count!=0){
            System.out.println();
        }
    }

    public void writeInputData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/logs/log.txt", true))) {
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

    public void writeWaitingClients() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/logs/log.txt", true))) {
            int count = 0;
            if(generatedClients.size() > 0) {
                bw.write("Waiting clients: ");
                for (Client client : generatedClients) {
                    bw.write("(" + client.getID() + ", " + client.getArrivalTime() + ", " + client.getServiceTime() + ") ");
                    count++;
                    if (count == 9) {
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

    public void writeServers() {
        for(Server server : scheduler.getServers()){
            writeServer(server);
        }
    }

    public static void writeServer(Server server) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/logs/log.txt", true))) {
            bw.write("Queue " + server.getQueueIndex() + ": ");
            if(server.getStatus().equals(Server.CLOSED)){
                bw.write(server.status);
            }else{
                for(Client client: server.getClients()){
                    bw.write("(" + client.getID() + ", " + client.getArrivalTime() + ", " + client.getServiceTime() + ") ");
                }
            }
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSimulationTime(int simulationTime) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/logs/log.txt", true))) {
            bw.write("Time " + simulationTime);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int currentTime = 0;
        writeInputData();
        while(currentTime < timeLimit) {
            Iterator<Client> i = generatedClients.iterator();
            while(i.hasNext()) {
                Client client = i.next();
                if (client.getArrivalTime() == currentTime) {
                    scheduler.dispatchClient(client);
                    i.remove();
                }else{
                    if(client.getArrivalTime() > currentTime){
                        break;
                    }
                }
            }
            writeSimulationTime(currentTime);
            writeWaitingClients();
            writeServers();
            currentTime++;
        }
        scheduler.stopServers();
    }
}
