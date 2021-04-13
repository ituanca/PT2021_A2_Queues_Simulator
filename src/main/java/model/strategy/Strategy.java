package model.strategy;

import model.Client;
import model.Server;
import model.Statistics;

import java.util.ArrayList;

public interface Strategy {
    public void addClient(ArrayList<Server> servers, Client client,  Statistics statistics) throws InterruptedException;
}
