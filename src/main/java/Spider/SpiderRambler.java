package Spider;


import Thinkers.Thinker;
import Thinkers.ThinkerBase;
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
    private Thinker thinker = new ThinkerBase();
    public  void run(){
        while(event.isRunning()){
            try {
                    actions = thinker.getNextActionType(actions);
                    actions.run(this);
                    this.commit();
                    Thread.sleep(1000);
            }catch (Exception e){
                Log.error("SpiderRambler is error!" + e);
            }
        }
    }
    /*
     type : INIT,
            STANDUPPRERARE,
            STANDUPBEGIN,
            STANDUPBEGIN,
            STANDUPEND,
            STANDUP,
            FORWARD,
            BACKWARD,

     */
    public void SetActions(String type){
        actions.SetActionType(type);
    }

}
