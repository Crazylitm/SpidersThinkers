package Spider;

import baseactions.Actions;
import baseactions.ActionsBase;
import event.Event;
import event.EventBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiderRambler extends SpiderBase {
    private static Logger Log = LoggerFactory.getLogger(SpiderRambler.class);
    final  Event event = new EventBase();
    private Actions actions = new ActionsBase();
    public  void run(){
        while(event.isRunning()){
            try {
                    actions.run(this);
                    this.commit();
            }catch (Exception e){
                Log.error("SpiderRambler is error!" + e);
            }
        }
    }
    public  void forward(){
        actions = new ActionsBase("FORWARD");
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
