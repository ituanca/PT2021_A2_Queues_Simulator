package model;

import model.strategy.ConcreteStrategyQueue;
import model.strategy.ConcreteStrategyTime;
import model.strategy.Strategy;

import java.util.ArrayList;

public class Scheduler {

    private ArrayList<Server> servers;
    private int noOfServers;
    private int noOfClients;
    private Strategy strategy;
    private ArrayList<Client> allClients;
    private int queueIndex;

    public Scheduler(int noOfServers, ArrayList<Client> allClients, SelectionPolicy policy) {
        this.allClients =  allClients;
        this.servers = new ArrayList<>();
        for(queueIndex = 0; queueIndex < noOfServers; queueIndex++){
            Server server = new Server(queueIndex + 1);
            servers.add(server);
        }
        changeStrategy(policy);
    }

    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
    }

    public void startServers(){
        for(int i = 0; i < noOfServers; i++){
            servers.get(i).getQueueThread().start();
        }
    }

    public void stopServers(){
        for(Server server : servers){
           // if(server.getQueueThread().isAlive()){
           //     server.getQueueThread().interrupt();
                server.stopServer();
            //}
        }
    }

    public int computeNoOfClientsCurrentlyInQueues(){
        int noOfClientsCurrentlyInQueues = 0;
        for(Server server : servers){
            noOfClientsCurrentlyInQueues += server.getNoOfClients().get();
        }
        return noOfClientsCurrentlyInQueues;
    }

    public void dispatchClient(Client client, Statistics statistics) throws InterruptedException { strategy.addClient(servers, client, statistics); }

    public ArrayList<Server> getServers() { return servers; }

    public void setServers(ArrayList<Server> servers) { this.servers = servers; }

    public int getNoOfServers() { return noOfServers; }

    public void setNoOfServers(int noOfServers) { this.noOfServers = noOfServers; }

    public int getNoOfClients() { return noOfClients; }

    public void setNoOfClients(int noOfClients) { this.noOfClients= noOfClients; }

    public Strategy getStrategy() { return strategy; }

    public void setStrategy(Strategy strategy) { this.strategy = strategy; }

    public int getQueueIndex() { return queueIndex; }

    public void setQueueIndex(int queueIndex) { this.queueIndex = queueIndex; }

    public ArrayList<Client> getAllClients() { return allClients; }

    public void setAllClients(ArrayList<Client> allClients) { this.allClients = allClients; }
}
