package legs;

import baseactions.Actions;
import motor.SteeringEngine;
import Pi.*;

public class Leg {
    protected SteeringEngine  waist;  // down
    protected SteeringEngine  thigh;  //middle
    protected SteeringEngine  shank; // top
    protected Actions         action; // curAction;

    public Leg(Pi pi,String waistId,String thighId,String shankId){
        waist = new SteeringEngine(pi,waistId);
        thigh = new SteeringEngine(pi,thighId);
        shank = new SteeringEngine(pi,shankId);
    }
    public Leg(SteeringEngine shank,
               SteeringEngine thigh,
               SteeringEngine waist){
        this.shank = shank;
        this.thigh = thigh;
        this.waist = waist;
    }
    public Leg(int iMaxShank,int iMinShank,int iMiddleShank,
               int iMaxThigh,int iMinThigh,int iMiddleThigh,
               int iMaxWaist,int iMinWaist,int iMiddleWaist,
               Pi pi,String waistId,String thighId,String shankId) {
        waist = new SteeringEngine(iMaxWaist, iMinWaist, iMiddleWaist,pi,waistId);
        thigh = new SteeringEngine(iMaxThigh,iMinThigh,iMiddleThigh,pi,thighId);
        shank = new SteeringEngine(iMaxShank,iMinShank,iMiddleShank,pi,shankId);
    }
    public void SetLeg(int waist_movedata,int thigh_movedata, int shank_movedata){
            waist.SetMoveData(waist_movedata);
            thigh.SetMoveData(thigh_movedata);
            shank.SetMoveData(shank_movedata);
    }
    public void SetLegWaist(int data){
            waist.SetMoveData(data);
    }
    public void SetLegThigh(int data){
            waist.SetMoveData(data);
    }
    public void SetLegShank(int data){
            waist.SetMoveData(data);
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    public void run(int waist_newPos,
                    int thigh_newPos,
                    int shank_newPos){
         shank.run(shank_newPos);
         thigh.run(thigh_newPos);
         waist.run(waist_newPos);
    }
    public void run(){
        shank.run();
        thigh.run();
        waist.run();


    }
    public void commit(){
        shank.commit();// or wait.commit() or thigh.commit()
    }
    public void commit(int time){
        shank.commit(time);// or wait.commit(time) or thigh.commit(time)
    }
}
