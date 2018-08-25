package Thinkers;

import baseactions.Actions;
import Spider.*;
import config.Config;

public class ThinkerBase implements Thinker {
    public ThinkerBase(){

    }
    public Actions getNextActionType(Actions action){
        if(Config.FORWARD.equals(action.getType()) == true){
            if(action.isEnd()){
                action.SetActionType(Config.BACKWARD);
            }
        }else if(Config.STANDUPEND.equals(action.getType())== true){
            if(action.isEnd()){
                action.SetActionType(Config.FORWARD);
            }
        }else if(Config.BACKWARD.equals(action.getType())==true){
            if(action.isEnd()){
                action.SetActionType(Config.FORWARD);
            }
        }else if(Config.INIT.equals(action.getType()) == true){
            if(action.isEnd()){
                action.SetActionType(Config.STANDUPPRERARE);
            }
        }else if(Config.STANDUPPRERARE.equals(action.getType()) == true){
            if(action.isEnd()){
                action.SetActionType(Config.STANDUPBEGIN);
            }

        }else if(Config.STANDUPBEGIN.equals(action.getType()) == true){
            if(action.isEnd()){
                action.SetActionType(Config.STANDUPEND);
            }
        }else {
            action.SetActionType(Config.INIT);
        }

        return  action;
    }
}
