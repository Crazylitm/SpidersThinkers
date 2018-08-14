package baseactions;
import Spider.*;
import config.ActionString;
import config.Config;
import legs.Leg;
import motor.Motor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActionsBase implements Actions {

    int iRunCount=1;// one action can run iRunCount times;
    String name = new String("INIT");
    List<ActionString> currentActionsList;
    int curActionPos=0;//指向curentAcionslist每次取的指令的当前条，如果循环一遍后让从头再循环，每次调用run就执行+1
    private Config cfg = new Config();//只能有一个地方读取action配置
    public ActionsBase(){
        this("INIT");
    }
    public ActionsBase(String name){
        SetActionType(name);
    }
    public String getType(){
        return name;
    }
    public void SetActionType(String type){
        this.name = type;
        try {
            currentActionsList = cfg.getCommandByType(name);
            curActionPos =0;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Boolean isEnd(){
        if(curActionPos >= currentActionsList.size() ||
                curActionPos == 0)
            return true;
        else
            return false;
    }
    public void run(Spider spider){
        ActionString action = currentActionsList.get(curActionPos++);
        spider.SetRunString(action.getCommandString());
        if(curActionPos > currentActionsList.size()) curActionPos = 0;
        return;
    }
    public void run(){
        return;
    }

    public String getName(){
        return  name;
    }

}
