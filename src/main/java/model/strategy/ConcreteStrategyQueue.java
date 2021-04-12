package model.strategy;

import model.Client;
import model.Server;

import java.util.ArrayList;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addClient(ArrayList<Server> servers, Client client) {
        Server shortestQueue = servers.get(0);
        for(Server server: servers){
            if(server.getClients().size() < shortestQueue.getClients().size()){
                shortestQueue = server;
            }
        }
        for(Server server: servers) {
            if(server.getQueueIndex() == shortestQueue.getQueueIndex()){
                server.getClients().add(client);
                break;
            }
        }
    }
}
