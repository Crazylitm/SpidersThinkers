package motor;

import Pi.Pi;
import Pi.piBase;
import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class MotorBase implements Motor{
    private static Logger Log = LoggerFactory.getLogger(MotorBase.class);

    private Pi pi;
    private StringBuffer  motorid;

    public MotorBase(Pi pi,String motorid){
        this.pi = pi;
        this.motorid = new StringBuffer(motorid);
    }
    //default  but i did not use it;
    public MotorBase(){
        this.pi = new piBase();
        motorid = new StringBuffer("#1");
    }
    public  String  run(int data){
        //#12P1000T500
        String ret = pi.write(new StringBuffer(motorid).append("P").
                                        append(data).toString());
        return ret;
    }
    public  String commit(int runtime){
        return pi.commit((new StringBuffer("T")).append(runtime).toString());
    }
    public  void step(){
        //#1P50T100
        pi.write(motorid.append(Config.MOTO_STEERING_STEP).toString());
    }
    public void backward(){
        pi.write(motorid.append("-").append(Config.MOTO_STEERING_STEP).toString());
    }

}
