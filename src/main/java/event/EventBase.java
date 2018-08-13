package event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class EventBase implements Event {
    private static Logger Log = LoggerFactory.getLogger(EventBase.class);
    protected boolean exiting = false;

    public synchronized Event promptForExit(){
        exiting = false;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                exiting = true;
                Log.info("Spider is exiting......");
            }
        });
        return this;
    }

    public synchronized boolean exiting(){
        return exiting;
    }
    public synchronized boolean isRunning(){
        return !exiting;
    }
}
