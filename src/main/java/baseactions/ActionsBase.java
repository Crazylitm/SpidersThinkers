package baseactions;
import Spider.*;
import legs.Leg;
import motor.Motor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActionsBase implements Actions {

    int iRunCount=1;// one action can run iRunCount times;
    String name;

    protected List<Actions>  actionsList = new LinkedList<Actions>();
    public ActionsBase(){
        name = new String("stop");
    }
    public ActionsBase(String name){
        this.name = name;
    }
    public void run(Spider spider){
            //DEFAULT IS STOP ACTION; no action
        return;
    }
    public void run(){
        return;
    }

    public String getName(){
        return  name;
    }
    public void addAction(Actions action){
        actionsList.add(action);
    }


}
