package Thinkers;

import baseactions.Actions;
import baseactions.ActionsBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThinkersBase implements Thinkers {
    protected List<String> listActionsName = new LinkedList<String>(){{
        add("forward");
        add("backward");
        add("leftward");
        add("rightward");
        add("leftRotate");
        add("rightRotate");
        add("stop");
    }};
    protected String curRunningActionName = "stop";
    protected Actions curRunningActions = null;
    protected Map<String,Actions> actionsMap = new ConcurrentHashMap();

    public Actions next(){
        if(curRunningActions == null){
            curRunningActions = actionsMap.get(curRunningActionName);
        }
        return curRunningActions;
    }
    public Actions get(String actionname){
        return actionsMap.get(actionname);
    }
    public void set(String actionname,Actions action){
        actionsMap.put(actionname,action);
    }
}
