package config;

import java.util.ArrayList;
import java.util.List;


/*
示例
{
      "action_name" : "ACTION_INIT_POS",
      "action_type" : "INIT",
      "action_list_num" : "1",
      "actions_list_content" :[{
          "1":"#1P2500#2P500#3P1300#4P2500#5P500#6P1300#7P2500#8P400#9P1300#22P500#23P2500#24P1300#25P550#26P2500#27P1300#28P550#29P2400#30P1300T1000"
      }],
      "action_desc" : "在初始化位置"
    },
 */
public class ActionConfigBean {
    private String action_name;
    private String action_type;
    private int action_list_num;
    private List<ActionString> action_list_content = new ArrayList<ActionString>();
    private String action_desc;

    public ActionConfigBean(String action_name,String action_type,int action_list_num,List<ActionString> action_list_content,String action_desc){
        this.action_name = action_name;
        this.action_type = action_type;
        this.action_list_num = action_list_num;
        this.action_list_content = action_list_content;
        this.action_desc = action_desc;
    }
    public void setAction_name(String action_name){
            this.action_name = action_name;
    }


    public String getAction_name() {
        return action_name;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getAction_type() {
        return action_type;
    }

    public int getAction_list_num() {
        return action_list_num;
    }

    public void setAction_list_num(int action_list_num) {
        this.action_list_num = action_list_num;
    }

    public void setAction_desc(String action_desc) {
        this.action_desc = action_desc;
    }

    public String getAction_desc() {
        return action_desc;
    }

    public void setAction_list_content(List<ActionString> action_list_content) {
        this.action_list_content = action_list_content;
    }

    public List<ActionString> getAction_list_content() {
        return action_list_content;
    }

    @Override
    public String toString() {
        /*

         */
        StringBuffer  buf = new StringBuffer("类型：");
        buf.append(action_type).append("\n名字：").append(action_name).
                append("\n数量:").append(action_list_num).append("\n命令行：").append(action_list_content);
        return String.valueOf(buf);
    }
}
