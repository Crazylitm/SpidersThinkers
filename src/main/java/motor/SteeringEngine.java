package motor;


import config.Config;
import Pi.*;

public class SteeringEngine extends MotorBase {

    int iMax = Config.MOTO_STEERING_MAX;
    int iMin = Config.MOTO_STEERING_MIN;
    int iMiddle = Config.MOTO_STEERING_MIDDLE;
    int curMoveData = -9999;
    public SteeringEngine(Pi pi,String motoid){
        super(pi,motoid);
    }
    public  SteeringEngine(int iMax,int iMin,int iMiddle,Pi pi ,String motoid){
        super(pi,motoid);
        this.iMax = iMax;
        this.iMin = iMin;
        this.iMiddle = iMiddle;
    }
    /*
    * check  确保舵机运行在安全的区间内，避免损伤。
    * iMax ,iMin 是创建舵机时候外部初始化出来的最大位置的区间。
    * 有可能iMax 小于 iMin，原因是安装顺序的原因，导致是反的。
    * 只要确保 data 值 在这个区间内就ok.
    * */
    private int check(int data){
            if(data > Config.MOTO_STEERING_MAX)
                data = Config.MOTO_STEERING_MAX;
            if(data < Config.MOTO_STEERING_MIN)
                data = Config.MOTO_STEERING_MIN;
            if(iMax > iMin){
                if(data > iMax)
                    data = iMax;
                if(data < iMin)
                    data = iMin;
            }else {
                if(data > iMin)
                    data = iMin;
                if(data < iMax)
                    data = iMax;
            }
            return  data;
    }
    public String run(int data){

        data = check(data);
        curMoveData =data;
        return super.run(data);
    }
    public void SetMoveData(int data){
        data = check(data);
        curMoveData =data;
    }
    public String run(){
        if(curMoveData == -9999) return "no move ,u should set move Data";
        return super.run(curMoveData);
    }
    public void commit(){
        commit(500);
    }
    public void goMiddle(){
        run(this.iMiddle);
    }
    public void goMax(){
        run(this.iMax);
    }
    public void goMin(){
        run(this.iMin);
    }
}
