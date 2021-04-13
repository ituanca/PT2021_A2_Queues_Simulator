package model.strategy;

import model.Client;
import model.Server;
import model.Statistics;

import java.util.ArrayList;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addClient(ArrayList<Server> servers, Client client, Statistics statistics) throws InterruptedException {
        Server shortestQueueByWaitingTime = servers.get(0);
        for(Server server: servers){
            if(server.getWaitingPeriod().get() < shortestQueueByWaitingTime.getWaitingPeriod().get()){
                shortestQueueByWaitingTime = server;
            }
        }
        for(Server server: servers) {
            if(server.getQueueIndex() == shortestQueueByWaitingTime.getQueueIndex()){
                //statistics.addWaitingTime(server);
                server.addClientToQueue(client, statistics);
                break;
            }
        }
    }
}
