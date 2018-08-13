package Spider;

import Pi.*;
import com.alibaba.fastjson.JSON;
import config.Config;
import config.SpiderConfig;
import legs.Leg;
import motor.SteeringEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class SpiderBase implements Spider{
    private static Logger Log = LoggerFactory.getLogger(SpiderBase.class);
    private List<SpiderConfig>  spiderConfigs = null;//读入LegMaxMinConfig.json初始化对象
    private Leg Leg_head_left = null;
    private Leg Leg_head_right = null;
    private Leg Leg_middle_left = null;
    private Leg Leg_middle_right = null;
    private Leg Leg_tail_left = null;
    private Leg Leg_tail_right = null;
    Pi pi =null;
    public SpiderBase(){
        pi = new piBase();
        loadLegMaxMinConfigJson();
        init();
        //单独测试过这每个Leg的3个舵机的运动位置，在这里初始化所有的设定
        /*
        Leg_head_left = new Leg(2500,550,1200,
                                2400,500,1500,
                                800,1700,1400,
                                 pi,"#3","#2","#1");
        Leg_middle_left = new Leg(2500,500,1500,
                                  2400,500,1500,
                                  800,1900,1400,
                                   pi,"#6","#5","#4");
        Leg_tail_left = new Leg(2500,550,1500,
                                2500,400,1500,
                                900,2200,1400,
                                 pi,"#9","#8","#7");
        Leg_tail_right = new Leg(900,2500,1500,
                                 500,2250,1500,
                                 1400,500,900,
                                  pi,"#24","#23","#22");
        Leg_middle_right = new Leg(750,2550,1500,
                                   500,2450,1500,
                                   1600,600,1100,
                                    pi,"#27","#26","#25");
        Leg_head_right = new Leg(750,2550,1500,
                                 400,2400,1500,
                                 2200,1200,1700,
                                  pi,"#30","#29","#28");
        */
    }
    private void loadLegMaxMinConfigJson(){
            String legconfig = Config.ReadJsonCfg("LegMaxMinConfig.json");
            spiderConfigs = JSON.parseArray(legconfig,SpiderConfig.class);
            System.out.println(spiderConfigs.toString());
    }

    private void init(){
        if(spiderConfigs == null){
            loadLegMaxMinConfigJson();
        }
        if(spiderConfigs != null){
            for(SpiderConfig spiderConfig: spiderConfigs){
                List<SpiderConfig.LegConfg> legs = spiderConfig.getLEG();
                SteeringEngine shank = null;
                SteeringEngine thigh = null;
                SteeringEngine waist = null;
                for(SpiderConfig.LegConfg leg : legs){
                    SteeringEngine se = new SteeringEngine(Integer.valueOf(leg.getiMax()),
                            Integer.valueOf(leg.getiMin()),
                            Integer.valueOf(leg.getiMiddle()),
                            pi,
                            leg.getMotorid());
                    if(leg.getSteeringEngine().equals("shank")){
                        shank = se;
                    }else  if(leg.getSteeringEngine().equals("thigh")){
                        thigh = se;
                    }else if(leg.getSteeringEngine().equals("waist")){
                        waist = se;
                    }else {
                        Log.error("LegMaxMinConfig is error. i can not find the SteeringEngine =" + leg.getSteeringEngine());
                    }
                }
                if(spiderConfig.getLegName().equals("Leg_head_left")){
                    Leg_head_left = new Leg(shank,thigh,waist);
                }else if(spiderConfig.getLegName().equals("Leg_middle_left")){
                    Leg_middle_left = new Leg(shank,thigh,waist);

                }else if(spiderConfig.getLegName().equals("Leg_tail_left")){
                    Leg_tail_left = new Leg(shank,thigh,waist);
                }else if(spiderConfig.getLegName().equals("Leg_tail_right")){
                    Leg_tail_right = new Leg(shank,thigh,waist);
                }else if(spiderConfig.getLegName().equals("Leg_middle_right")){
                    Leg_middle_right = new Leg(shank,thigh,waist);
                }else if(spiderConfig.getLegName().equals("Leg_head_right")){
                    Leg_head_right = new Leg(shank,thigh,waist);
                }else {
                    Log.error("LegMaxMinConfig is error. i can not find  " + spiderConfig.getLegName());
                }
            }
        }

    }

    public void commit(){
        Leg_head_left.commit();// or other legs to commit;
    }
    public void commit(int time){
        Leg_head_left.commit();// or other legs to commit;
    }


    //初始位置：
    //#1P2500#2P500#3P1400
    //#4P2500#5P500#6P1400
    //#7P2500#8P400#9P1400
    //#22P900#23P2250#24P900
    //#25P750#26P2450#27P1100
    //#28P750#29P2400#30P1700T500
    public void startup(){
        pi.clear();
        Leg_head_left.run(1400,500,2500);
        Leg_middle_left.run(1400,500,2500);
        Leg_tail_left.run(1400,400,2500);
        Leg_tail_right.run(900,2250,900);
        Leg_middle_right.run(1100,2450,750);
        Leg_head_right.run(1700,2400,750);
        Log.debug(pi.toString());
        System.out.println(pi);
    }
    //#1P1200#2P700#3P1400
    // #4P1100#5P700#6P1400
    // #7P1200#8P700#9P1400
    // #22P2000#23P1850#24P900
    // #25P1900#26P2100#27P1100
    // #28P2200#29P2100#30P1700
    // T500

    public void standup(){
        pi.clear();
        Leg_head_left.run(1400,700,1200);
        Leg_middle_left.run(1400,700,1100);
        Leg_tail_left.run(1400,700,1200);
        Leg_tail_right.run(900,1850,2000);
        Leg_middle_right.run(1100,2100,1900);
        Leg_head_right.run(1700,2100,2200);
        Log.debug(pi.toString());
        System.out.println(pi);
    }
    public abstract void run();
    public  void forward(){

    }
    public void clear(){
        pi.clear();
    }

    public Leg getLeg_head_left(){
        return Leg_head_left;
    }

    public Leg getLeg_middle_left() {
        return Leg_middle_left;
    }

    public Leg getLeg_tail_left() {
        return Leg_tail_left;
    }

    public Leg getLeg_head_right() {
        return Leg_head_right;
    }

    public Leg getLeg_middle_right() {
        return Leg_middle_right;
    }

    public Leg getLeg_tail_right() {
        return Leg_tail_right;
    }

    public abstract void backward();
    public abstract void leftward();
    public abstract void rightward();
    public abstract void leftRotate();
    public abstract void rightRotate();
}
