package model;

public class Client implements Comparable<Client>{
    private final Integer ID;
    private final Integer arrivalTime;
    private Integer serviceTime;
    public static final String NOT_YET_IN_QUEUE = "Not yet in queue";
    public static final String WAITING = "Waiting";
    public static final String BEING_SERVED = "Being served";
    public static final String SERVED = "Served";
    private String status;

    public Client(Integer ID, Integer arrivalTime, Integer serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public Integer getID() {
        return ID;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public Integer getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Integer serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public int compareTo(Client compareClient) {
        int compareArrivalTime = compareClient.getArrivalTime();
        return this.arrivalTime - compareArrivalTime;
    }
}
