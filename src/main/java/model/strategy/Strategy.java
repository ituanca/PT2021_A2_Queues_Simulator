package model.strategy;

import model.Client;
import model.Server;

import java.util.ArrayList;

public interface Strategy {
    public void addClient(ArrayList<Server> servers, Client client) throws InterruptedException;
}
