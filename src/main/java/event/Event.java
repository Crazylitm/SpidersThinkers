package event;

public interface Event {
    public  boolean exiting();
    public  boolean isRunning();
    public  Event promptForExit();
}
