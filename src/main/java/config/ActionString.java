package config;

/*
    是ActionConfigBean读取的具体的执行命令的行的String，例如：
      "actions_list_content" :[{
        "1":"#1P1700#2P1300#3P1300#4P1500#5P1600#6P1300#7P1300#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000",
        "2":"#1P1700#2P1600#3P1300#4P1500#5P1600#6P1300#7P1300#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T1000",
        "3":"#1P1700#2P1600#3P1300#4P1500#5P1400#6P1500#7P1300#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1100#28P1300#29P1500#30P1300T1000",
        "4":"#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1300#28P1300#29P1500#30P1300T1000",
        "5":"#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1700#23P1400#24P1300#25P1500#26P1400#27P1300#28P1300#29P1200#30P1300T1000",
        "6":"#1P1700#2P1300#3P1300#4P1500#5P1600#6P1500#7P1300#8P1300#9P1300#22P1700#23P1400#24P1300#25P1500#26P1600#27P1100#28P1300#29P1200#30P1300T1000"
      }],
 */
public class ActionString {
        private int seriesid;
        private String commandString;

        public ActionString(){
            seriesid = -1;//如果是-1表示空
        }
        public ActionString(int seriesid,String commandString){
            this.seriesid = seriesid;
            this.commandString = commandString;
        }
    public int getSeriesid() {
        return seriesid;
    }

    public void setSeriesid(int seriesid) {
        this.seriesid = seriesid;
    }

    public String getCommandString() {
        return commandString;
    }

    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }

    @Override
    public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append(seriesid).append(" : ").append(commandString).append("\n");
        return String.valueOf(buf);
    }
}
