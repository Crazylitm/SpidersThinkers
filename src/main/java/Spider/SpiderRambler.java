package Spider;

import Thinkers.*;
import baseactions.Actions;
import event.Event;
import event.EventBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiderRambler extends SpiderBase {
    private static Logger Log = LoggerFactory.getLogger(SpiderRambler.class);
    final  Event event = new EventBase();
    Thinkers thinkers = new ThinkersBase();
    public  void run(){
        while(event.isRunning()){
            try {
                Actions action = thinkers.get("forward");
                action.run(this);
            }catch (Exception e){
                Log.error("SpiderRambler is error!" + e);
            }
        }
    }
    public  void forward(){

    }
    public  void backward(){

    }
    public  void leftward(){

    }
    public  void rightward(){

    }
    public  void leftRotate(){

    }
    public  void rightRotate(){

    }
}
