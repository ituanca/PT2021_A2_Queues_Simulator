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

    public Scheduler(int noOfServers, SelectionPolicy policy) {
        this.servers = new ArrayList<>();
        int queueIndex;
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

    public void stopServers(){
        for(Server server : servers){
            server.stopServer();
        }
    }

    public int computeNoOfClientsCurrentlyInQueues(){
        int noOfClientsCurrentlyInQueues = 0;
        for(Server server : servers){
            noOfClientsCurrentlyInQueues += server.getNoOfClients().get();
        }
        return noOfClientsCurrentlyInQueues;
    }

    public boolean checkIfAllTheServersAreEmpty(){
        int noOfClosedServers = 0;
        for(Server server : servers){
            if(server.status.equals("Closed")){
                noOfClosedServers++;
            }
        }
        return noOfClosedServers == servers.size();
    }

    public void dispatchClient(Client client, Statistics statistics) throws InterruptedException { strategy.addClient(servers, client, statistics); }

    public ArrayList<Server> getServers() { return servers; }

}
