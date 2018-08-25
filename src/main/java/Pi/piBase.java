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
        fd = Serial.serialOpen(Serial.DEFAULT_COM_PORT, 9600);
        Log.debug("Serial.serialOpen(Serial.DEFAULT_COM_PORT, 9600); fd="+fd);
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
        return "fd="+fd+"---buffer="+buffer.toString();
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
        Log.debug("commit  fd =" +fd);
        Serial.serialPuts(fd,buffer.append(runtime).toString());
        Log.debug("commit.Serial.serialPuts="+buffer.append(runtime).toString());

        // display data received to console
        int dataavail = Serial.serialDataAvail(fd);
        StringBuffer buf = new StringBuffer();
        Log.debug("fd="+fd);
        Log.debug("datavail="+dataavail);
        while(dataavail > 0) {
            byte data = Serial.serialGetByte(fd);
            buf.append(data);
            Log.debug("write Get Reture Byte=" + data);
            dataavail = Serial.serialDataAvail(fd);
        }

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
