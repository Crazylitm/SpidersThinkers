package config;

import java.util.ArrayList;
import java.util.List;

public class SpiderConfig {
    private String LegName;

    public class LegConfg{
        private String SteeringEngine;
        private String motorid;
        private String iMax;
        private String iMin;
        private String iMiddle;

        public String getiMax() {
            return iMax;
        }

        public String getiMiddle() {
            return iMiddle;
        }

        public String getiMin() {
            return iMin;
        }

        public String getMotorid() {
            return motorid;
        }

        public String getSteeringEngine() {
            return SteeringEngine;
        }

        public void setiMax(String iMax) {
            this.iMax = iMax;
        }

        public void setiMiddle(String iMiddle) {
            this.iMiddle = iMiddle;
        }

        public void setiMin(String iMin) {
            this.iMin = iMin;
        }

        public void setMotorid(String motorid) {
            this.motorid = motorid;
        }

        public void setSteeringEngine(String steeringEngine) {
            SteeringEngine = steeringEngine;
        }

        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer("LegConfg:\n");
           // buf.append("=======================================================\n");
            buf.append("SteeringEngine=").append(SteeringEngine).append("  ");
            buf.append("motorid=").append(motorid).append("  ");
            buf.append("iMax=").append(iMax).append("  ");
            buf.append("iMin=").append(iMin).append("  ");
            buf.append("iMiddle=").append(iMiddle).append("\n");
           // buf.append("=======================================================\n");
            return String.valueOf(buf);
        }
    };

    private List<LegConfg> LEG = new ArrayList<LegConfg>();

    public List<LegConfg> getLEG() {
        return LEG;
    }

    public String getLegName() {
        return LegName;
    }

    public void setLEG(List<LegConfg> LEG) {
        this.LEG = LEG;
    }

    public void setLegName(String legName) {
        LegName = legName;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer("SpiderConfig:\n");
        buf.append("=======================================================\n");
        buf.append("LegName =").append(LegName).append("\n");
        buf.append(LEG.toString());
        buf.append("=======================================================\n");
        return String.valueOf(buf);
    }
}
