package model;

public class Statistics {

    public double averageWaitingTime;
    private double totalWaitingTime;
    public double averageServiceTime;
    private double totalServiceTime;
    private int totalNumberOfClientsFromQueues;
    public int peakHour;
    public int maxNumberOfClientsInQueuesAtATime;

    public Statistics() {
        this.averageWaitingTime = 0;
        this.totalWaitingTime = 0;
        this.averageServiceTime = 0;
        this.totalServiceTime = 0;
        this.totalNumberOfClientsFromQueues = 0;
        this.peakHour = 0;
        this.maxNumberOfClientsInQueuesAtATime = 0;
    }

    public void addWaitingTime(Server server){
        this.totalWaitingTime = this.totalWaitingTime + server.getWaitingPeriod().get();
    }

    public void addClient(){
        this.totalNumberOfClientsFromQueues++;
    }

    public void computeAverageWaitingTime(){
        this.averageWaitingTime = this.totalWaitingTime / this.totalNumberOfClientsFromQueues;
    }

    public void addServiceTime(Client client){
        this.totalServiceTime = this.totalServiceTime + client.getServiceTime();
    }

    public void computeAverageServiceTime(){
        this.averageServiceTime = this.totalServiceTime / this.totalNumberOfClientsFromQueues;
    }

    public int getMaxNumberOfClientsInQueuesAtATime(int noOfClientsCurrentlyInQueues){
        if(noOfClientsCurrentlyInQueues > this.maxNumberOfClientsInQueuesAtATime){
            this.maxNumberOfClientsInQueuesAtATime = noOfClientsCurrentlyInQueues;
        }
        return this.maxNumberOfClientsInQueuesAtATime;
    }

    public void setPeakHour(int peakHour) { this.peakHour = peakHour; }
}
