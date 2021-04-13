package model.strategy;

import model.Client;
import model.Server;
import model.Statistics;

import java.util.ArrayList;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addClient(ArrayList<Server> servers, Client client,  Statistics statistics) throws InterruptedException {
        Server shortestQueue = servers.get(0);
        for(Server server: servers){
            if(server.getClients().size() < shortestQueue.getClients().size()){
                shortestQueue = server;
            }
        }
        for(Server server: servers) {
            if(server.getQueueIndex() == shortestQueue.getQueueIndex()){
                //statistics.addWaitingTime(server);
                server.addClientToQueue(client, statistics);
                break;
            }
        }
    }
}
