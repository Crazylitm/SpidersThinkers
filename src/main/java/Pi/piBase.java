package Pi;

import com.pi4j.wiringpi.Serial;
import motor.MotorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class piBase implements Pi {
    private static Logger Log = LoggerFactory.getLogger(piBase.class);

    int fd = 0; // pi serial port for communication
    StringBuffer buffer = new StringBuffer(); // before PI received the message , u first need to wirte to this buffer.  this is buffer
    public piBase(){
        init();
    }
    public void init() {
        // open serial port for communication
        int fd = -1;//Serial.serialOpen(Serial.DEFAULT_COM_PORT, 9600);
        if (fd == -1) {
            Log.error("==>> SERIAL SETUP FAILED");
            return;
        }
    }
    public void clear(){
        buffer = new StringBuffer();
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

    void print(){

    }
    public String write(String Data){
       buffer.append(Data);
       return  buffer.toString();
    }

    public String commit(String runtime){
        if(fd == 0 ){
            init();
        }
/*
        Serial.serialPuts(fd,buffer.append(runtime).toString());
        // display data received to console
        int dataavail = Serial.serialDataAvail(fd);
        StringBuffer buffer = new StringBuffer();
        while(dataavail > 0) {
            byte data = Serial.serialGetByte(fd);
            buffer.append(data);
            Log.debug("write Get Reture Byte=" + data);
            dataavail = Serial.serialDataAvail(fd);
        }
*/
        try {
             runtime= runtime.substring(runtime.indexOf("T")+1,runtime.length());
            Thread.sleep(Long.valueOf(runtime));
        }catch (Exception e){
            Log.error("Sleep =" +runtime + " is error");
            e.printStackTrace();
        }

        return buffer.toString();
    }
}
