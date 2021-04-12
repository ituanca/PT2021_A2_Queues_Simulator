package model.strategy;

import model.Client;
import model.Server;

import java.util.ArrayList;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addClient(ArrayList<Server> servers, Client client) {
        Server shortestQueueByWaitingTime = servers.get(0);
        for(Server server: servers){
            if(server.getWaitingPeriod().get() < shortestQueueByWaitingTime.getWaitingPeriod().get()){
                shortestQueueByWaitingTime = server;
            }
        }
        for(Server server: servers) {
            if(server.getQueueIndex() == shortestQueueByWaitingTime.getQueueIndex()){
                shortestQueueByWaitingTime.getClients().add(client);
                break;
            }
        }
    }
}
