package model;
import java.util.ArrayList;

public class RandomClientsGenerator {

    public void createClientsArray(ArrayList<Client> clients, Integer noOfClients, Integer minArrivalTime, Integer maxArrivalTime, Integer minServiceTime, Integer maxServiceTime){
        for(int i = 0; i < noOfClients; i++) {
            clients.add(generateRandomTuple(i + 1, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime));
        }
    }

    private Client generateRandomTuple(int clientNo, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime){
        int crtArrivalTime = generateRandomArrivalTime(minArrivalTime, maxArrivalTime);
        int crtServiceTime = generateRandomServiceTime(minServiceTime, maxServiceTime);
        return new Client(clientNo, crtArrivalTime, crtServiceTime);
    }

    private int generateRandomArrivalTime(int minArrivalTime, int maxArrivalTime){
        return (int)Math.floor( Math.random() * ( maxArrivalTime - minArrivalTime + 1 ) + minArrivalTime );
    }

    private int generateRandomServiceTime(int minServiceTime, int maxServiceTime){
        return (int)Math.floor( Math.random() * ( maxServiceTime - minServiceTime + 1 ) + minServiceTime );
    }
}
