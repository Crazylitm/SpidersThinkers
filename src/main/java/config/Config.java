package config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import event.EventBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static Logger Log = LoggerFactory.getLogger(Config.class);

    static public int MOTO_STEERING_MAX             = 2550  ;
    static public int MOTO_STEERING_MIN             = 400   ;
    static public int MOTO_STEERING_MIDDLE          = 1500  ;
    static public String MOTO_STEERING_STEP         = "P50" ;
    static public String MOTO_STEERING_DEFAULT_TIME = "T500";
    static public String MOTO_STEP_TIME             = "T100";

    private List<ActionConfigBean> actions_select = null;

    public Config(){
        try {
            loadActions_select();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void loadActions_select() throws IOException{
            try {
                actions_select = ReadBaseactionJson();
            }catch (IOException e){
                throw e;
            }
            if(actions_select == null) {
                Log.error("Read baseaction.json error !");
                throw new IOException("Read baseaction.json error !");
            }

    }
    public  List<ActionString> getCommandByType(String type) throws IOException {
        if(actions_select == null )
        {
            loadActions_select();
        }
        for(ActionConfigBean bean:actions_select){
                if(bean.getAction_type().equals(type) == true){
                     return bean.getAction_list_content();
                }
        }
        return null;
    }
    public List<ActionString> getCommandByName(String name) throws  IOException{
        if(actions_select == null )
        {
            loadActions_select();
        }
        for(ActionConfigBean bean:actions_select){
            if(bean.getAction_name().equals(name) == true){
                return bean.getAction_list_content();
            }
        }
        return null;
    }
    private List<ActionConfigBean> ReadBaseactionJson() throws IOException{
        JSONObject json = JSONObject.parseObject(ReadJsonCfg("baseaction.json"));
        JSONArray array = json.getJSONArray("ACTION_CONFIG");
        List<ActionConfigBean> ret = new ArrayList<ActionConfigBean>();
        for(int i = 0 ; i < array.size(); i++){
            //获得一个配置文件的动作的配置节点
            JSONObject actionbean = array.getJSONObject(i);
            int action_list_num = actionbean.getInteger("action_list_num");
            String action_name  = actionbean.getString("action_name");
            String action_type  = actionbean.getString("action_type");
            String action_desc = actionbean.getString("action_desc");

            //解析这个动作节点的具体动作列表
            List<ActionString> actioncommand = new ArrayList<ActionString>();

            JSONArray commandArray = actionbean.getJSONArray("actions_list_content");
            int size = commandArray.size();
            if(size != action_list_num){
                throw new IOException("baseaction.json config is error, the action_list_num != 实际配置数量. action_type=" + action_type);
            }
            for(int j =0 ; j < size;j++){
                JSONObject command = commandArray.getJSONObject(j);
                ActionString commandString = new ActionString(j+1,command.getString(String.valueOf(j+1)));
                actioncommand.add(commandString);
            }

            ActionConfigBean configBean = new ActionConfigBean(action_name,action_type,action_list_num,actioncommand,action_desc);
            Log.debug(configBean.toString());
            ret.add(configBean);
        }
        return  ret;
    }
    public static String ReadJsonCfg(String filename){
        //https://www.cnblogs.com/happyPawpaw/archive/2013/04/09/3010017.html
        //Class.getResource与ClassLoader.getResource()区别

        //String p = this.getClass().getResource("/baseaction.json").getPath();
        StringBuffer buf = null;
        try{
            String  p = Config.class.getClassLoader().getResource(filename).getPath();
            p = java.net.URLDecoder.decode(p,"utf-8");
            File file = new File(p);
            FileInputStream is =null;

            is = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(streamReader);
            String line ;
            buf = new StringBuffer();
            while ((line = reader.readLine()) != null){
                buf.append(line);
            }
            reader.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return String.valueOf(buf);
    }
}
/*
1、每个舵机编号，位置，最大，中间，最小
头部定义：电路板上充电插入的地方和数据线对接的地方，以及2个前脚中间的舵机靠近

头前左脚：编号：1，2，3

编号1 前腿：最大：2500 （前脚朝天）最小：550（前脚全部折叠）中间：1500
编号2 中腿：最大：2400 （中腿朝底）最小：500（中腿朝天）    中间：1500
编号3 根腿：最大：700（600）  （根腿朝左）最小：1700（根腿朝前）   中间：1300

头部左中腿：编号：4，5，6
编号4 前腿：最大：2500 （前脚朝天）最小：550（前脚全部折叠）中间：1500
编号5 中腿：最大：2500 （中腿朝底）最小：500（中腿朝天）    中间：1500
编号6 根腿：最大：900（600）  （根腿朝尾部后方）最小：2200（2100）（根腿朝头部前方）   中间：1300

头部左后腿：编号：7，8，9
编号7 前腿：最大：2500 （前脚朝天）最小：500（前脚全部折叠）中间：1500
编号8 中腿：最大：2500 （中腿朝底）最小：400（中腿朝天）    中间：1500
编号9 根腿：最大：900  （根腿朝尾部后方）最小：2200（根腿朝头部左侧前方）   中间：1300			正后方：1000   -300

头部右后腿：编号：22，23，24
编号22 前腿：最大：500 （前脚朝天）最小：2500（前脚全部折叠）中间：1500
编号23 中腿：最大：400 （中腿朝底）最小：2500（中腿朝天）    中间：1500
编号24 根腿：最大：2200  （根腿朝尾部后方）最小：900（600）（根腿朝头部右侧前方）   中间：1300    正后方：1600   +300

头部右中腿：编号：25，26，27
编号25 前腿：最大：550 （前脚朝天）最小：2500（前脚全部折叠）中间：1500
编号26 中腿：最大：500 （中腿朝底）最小：2500（中腿朝天）    中间：1500
编号27 根腿：最大：2200  （根腿朝尾部斜后方）最小：900（600）（根腿朝头部右侧斜前方）   中间：1300

头部右腿：编号：28，29，30
编号28 前腿：最大：550 （前脚朝天）最小：2500（前脚全部折叠）中间：1500
编号29 中腿：最大：500 （中腿朝底）最小：2400（中腿朝天）    中间：1300
编号30 根腿：最大：1900  （根腿朝尾部斜后方）最小：900（根腿朝头部前方）   中间：1300           +-200
#22P900#23P2250#24P1300T500
#7P2500#8P400#9P1300#T500
#4P2500#5P500#6P1300#T500
#25P750#26P2450#27P1300#T1000
#1P2500#2P500#3P1300T500
#28P750#29P2400#30P1300T500
#22P500#23P2500#24P1300T500
#7P500#8P400#9P1300#T500
==============================
1、站立位置：前脚2个正向超前，后腿2个正向朝后，左右两只正向朝向各自方向。
#1P2500#2P500#3P1700#4P2500#5P500#6P1300#7P2500#8P400#9P1000#22P500#23P2500#24P1600#25P550#26P2500#27P1300#28P550#29P2400#30P900T1000
2、所有得根腿都放在中间位置，也是初始化位置
#1P2500#2P500#3P1300#4P2500#5P500#6P1300#7P2500#8P400#9P1300#22P500#23P2500#24P1300#25P550#26P2500#27P1300#28P550#29P2400#30P1300T1000
2.1、前腿全部折叠到身边
#1P700#2P500#3P1300#4P700#5P500#6P1300#7P700#8P400#9P1300#22P2100#23P2500#24P1300#25P2200#26P2500#27P1300#28P2300#29P2400#30P1300T1000
2.2、中腿让机器站立脱离地面
#1P700#2P800#3P1300#4P700#5P800#6P1300#7P700#8P700#9P1300#22P2100#23P2200#24P1300#25P2200#26P2200#27P1300#28P2300#29P2100#30P1300T1000
脱离地面稍微更高一点
#1P700#2P800#3P1300#4P700#5P900#6P1300#7P700#8P700#9P1300#22P2100#23P2200#24P1300#25P2200#26P2200#27P1300#28P2300#29P2100#30P1300T1000
3、站立位置（还有些舵机位置有微调，需要再check一下ing）
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
4、向前行走（1号和28号舵机的中间，舵机控制板的电源和sub的方向为前方）
第一步：
抬起 28、4、22  脚做向前迈步得准备
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1300#7P1300#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000
第二步：将28、4、22三至脚落地
#1P1700#2P1600#3P1300#4P1500#5P1600#6P1300#7P1300#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T1000
第三步：抬起
#1P1700#2P1600#3P1300#4P1500#5P1400#6P1500#7P1300#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1100#28P1300#29P1500#30P1300T1000
第四步：28，4，22 收回
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1300#28P1300#29P1500#30P1300T1000
第五步：将1，7，25 三脚放下
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1700#23P1400#24P1300#25P1500#26P1400#27P1300#28P1300#29P1200#30P1300T1000
第六步：
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1500#7P1300#8P1300#9P1300#22P1700#23P1400#24P1300#25P1500#26P1600#27P1100#28P1300#29P1200#30P1300T1000
第七步：（重复第一步）
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1300#7P1300#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000
==============================
上面向前走的步幅快节奏版本
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1300#7P1300#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T300
#1P1700#2P1600#3P1300#4P1500#5P1600#6P1300#7P1300#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T300
#1P1700#2P1600#3P1300#4P1500#5P1400#6P1500#7P1300#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1100#28P1300#29P1500#30P1300T300
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1300#28P1300#29P1500#30P1300T300
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1700#23P1400#24P1300#25P1500#26P1400#27P1300#28P1300#29P1200#30P1300T300
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1500#7P1300#8P1300#9P1300#22P1700#23P1400#24P1300#25P1500#26P1600#27P1100#28P1300#29P1200#30P1300T300
==============================
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
5、向后行走（7和22中间的方向，舵机控制板电源和usb方向的对面反方向）
第一步：7（+200，-200，），1（-200，-200，），25（，+200，+200）  做出后退抬腿姿势
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1300#7P1700#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1300T1000
第二步：7（，+100，），1（，0，），25（，0，）放下后腿的脚
#1P1300#2P1500#3P1300#4P1500#5P1600#6P1300#7P1700#8P1600#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1500#28P1500#29P1300#30P1300T1000
第三步：4（，-200，-200），22（，+200，），28（+200，+200，）抬起另外三只腿
#1P1300#2P1500#3P1300#4P1500#5P1400#6P1100#7P1700#8P1600#9P1300#22P1300#23P1600#24P1300#25P1500#26P1400#27P1500#28P1700#29P1500#30P1300T1000
第四步：7（0，0，），1（0，，），25（，，0）回到站立剧中位置，向前位移一点
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1300#22P1300#23P1600#24P1300#25P1500#26P1400#27P1300#28P1700#29P1500#30P1300T1000
第五步：4（，0，），22（，0，），28（，，）放下另外三只脚
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1100#7P1500#8P1500#9P1300#22P1300#23P1400#24P1300#25P1500#26P1400#27P1300#28P1700#29P1300#30P1300T1000
第六步：7（+200，-200，），1（-200，-200，），25（,+200,+200）
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1100#7P1700#8P1300#9P1300#22P1300#23P1400#24P1300#25P1500#26P1600#27P1500#28P1700#29P1300#30P1300T1000
第七步：重复第一步
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1300#7P1700#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1300T1000
==============================

站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000

6、向左向直行 （4号方向）
第一步：4（+200：1700，-200：1400，），22（，+200：1600，+200：1500），28（，+200：1500，-200：1100）4，22，28 抬起做出向前的动作
#1P1500#2P1500#3P1300#4P1700#5P1400#6P1300#7P1500#8P1500#9P1300#22P1500#23P1600#24P1500#25P1500#26P1400#27P1300#28P1500#29P1500#30P1100T1000
第二步：4（,+50:1650，），22（，0:1400，），28（，0:1300，） 放下撑住机器
#1P1500#2P1500#3P1300#4P1700#5P1650#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1500#25P1500#26P1400#27P1300#28P1500#29P1300#30P1100T1000
第三步：1（，-200：1300，-200：1100），7（，-200：1300，+200：1500），25（+200：1700，+200：1600，）  另外三只脚抬起来
#1P1500#2P1300#3P1100#4P1700#5P1650#6P1300#7P1500#8P1300#9P1500#22P1500#23P1400#24P1500#25P1700#26P1600#27P1300#28P1500#29P1300#30P1100T1000
第四步：4（0:1500,0:1600,），22（,,0:1300），28（,,0:1300）向前一小步
#1P1500#2P1300#3P1100#4P1500#5P1600#6P1300#7P1500#8P1300#9P1500#22P1500#23P1400#24P1300#25P1700#26P1600#27P1300#28P1500#29P1300#30P1300T1000
第五步：1（，0：1500，），7（，0：1500，），25（，0：1400，） 放下
#1P1500#2P1500#3P1100#4P1500#5P1600#6P1300#7P1500#8P1500#9P1500#22P1500#23P1400#24P1300#25P1700#26P1400#27P1300#28P1500#29P1300#30P1300T1000
第六步：4（，，），22（，，），28（，，）
#1P1500#2P1500#3P1100#4P1700#5P1400#6P1300#7P1500#8P1500#9P1500#22P1500#23P1600#24P1500#25P1700#26P1400#27P1300#28P1500#29P1500#30P1100T1000

===========================
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
7、向右直行（25号是方向）
第一步：25（-200：1300，+200：1600，）、1（，-200：1300，+200：1500）、7（，-200：1300，-200：1100）抬起做出向前的动作
#1P1500#2P1300#3P1500#4P1500#5P1600#6P1300#7P1500#8P1300#9P1100#22P1500#23P1400#24P1300#25P1300#26P1600#27P1300#28P1500#29P1300#30P1300T1000
第二步：25（，-100：1300，），1（，0：1500，），7（，0：1500，）放下撑住机器
#1P1500#2P1500#3P1500#4P1500#5P1600#6P1300#7P1500#8P1500#9P1100#22P1500#23P1400#24P1300#25P1300#26P1300#27P1300#28P1500#29P1300#30P1300T1000
第三步：28（，+200：1500，+200：1500）、4（-200：1300，-200：1400，）、22（，+200：1600，-200：1100） 抬起脚做向前走的动作
#1P1500#2P1500#3P1500#4P1300#5P1400#6P1300#7P1500#8P1500#9P1100#22P1500#23P1600#24P1100#25P1300#26P1300#27P1300#28P1500#29P1500#30P1500T1000
第四步：25（0：1500，0：1400，），1（，，0：1300），7（，，0：1300） 向前一小步
#1P1500#2P1500#3P1300#4P1300#5P1400#6P1300#7P1500#8P1500#9P1300#22P1500#23P1600#24P1100#25P1500#26P1400#27P1300#28P1500#29P1500#30P1500T1000
第五步：28（，0：1300，），4（，0：1600，），22（，+100：1400，） 放下
#1P1500#2P1500#3P1300#4P1300#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1100#25P1500#26P1400#27P1300#28P1500#29P1300#30P1500T1000
第六步：25（，，），1（，，），7（，，） 抬起
#1P1500#2P1300#3P1500#4P1300#5P1600#6P1300#7P1500#8P1300#9P1100#22P1500#23P1400#24P1100#25P1300#26P1600#27P1300#28P1500#29P1300#30P1500T1000
===========================
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
8、向左前方（1号脚在的方向）
第一步:1(+200：1700,-200：1300,),25(,+200：1600,-1100),7(,-200：1300,+200：1500) 抬起做出向前的动作
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1300#7P1500#8P1300#9P1500#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000
第二步：1（，+100：1600，），25（，，）7（，，） 放下撑住机器
#1P1700#2P1600#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1500#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T1000
第三步：28（，+200：1500，-200：1100）、4（，-200：1400，+200：1600）、22（+200：1700，+200：1600，）另外3条腿抬起脚向前的走的动作
#1P1700#2P1600#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1500#22P1700#23P1600#24P1300#25P1500#26P1400#27P1100#28P1500#29P1500#30P1100T1000
第四步：1（，，）25（，，）7（，，）向前一小步  几个值都是站立位置值
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1300#28P1500#29P1500#30P1100T1000
第五步：28（，0：1300，）4（，0：1600，）22（，，）放下另外3只脚
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1700#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1100T1000
第六步：1（，，）25（，，）7（，，）将最先的3只脚抬起和第一步动作一致
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1500#7P1500#8P1300#9P1500#22P1700#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1100T1000

===========================
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
9、向右前方（28号脚在的方向）
第一步：28（-200：1300，+200：1500，）4（，-200：1400，+200：1500）22（，+200：1600，-200：1100）抬起做出向前的动作
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1500#23P1600#24P1100#25P1500#26P1400#27P1300#28P1300#29P1500#30P1300T1000
第二步：28（，-100:1200，）4（，0:1600，）22（，0:1400，）放下
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1500#23P1400#24P1100#25P1500#26P1400#27P1300#28P1300#29P1200#30P1300T1000
第三步：25(,+200：1600,-200：1100)1(,-200:1300,+200:1500) 7(-200:1300,-200:1300,) 抬起另外3只
#1P1500#2P1300#3P1500#4P1500#5P1600#6P1500#7P1300#8P1300#9P1300#22P1500#23P1400#24P1100#25P1500#26P1600#27P1100#28P1300#29P1200#30P1300T1000
第四步：28（，，）4（，，）22（，，）向前一小步，和站立位置一样
#1P1500#2P1300#3P1500#4P1500#5P1600#6P1300#7P1300#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000
第五步：25（，，） 1（，，）7（，，）放下另外3只
#1P1500#2P1500#3P1500#4P1500#5P1600#6P1300#7P1300#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T1000
第六步：28（，，）4（，，）22（，，）抬起和第一步一样
#1P1500#2P1500#3P1500#4P1500#5P1400#6P1500#7P1300#8P1500#9P1300#22P1500#23P1600#24P1100#25P1500#26P1400#27P1100#28P1300#29P1500#30P1300T1000

===========================
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
10、向右后方（22号脚在的方向）
第一步：22（-200：1300，+200：1600，）4（，-200：1400，-200：1100）28（，+200：1500，）抬起做出向前的动作
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1300#22P1300#23P1600#24P1300#25P1500#26P1400#27P1300#28P1500#29P1500#30P1500T1000
第二步：22（，0：1400，）4（，0：1600，）28（，0：1300，）放下
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1100#7P1500#8P1500#9P1300#22P1300#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1500T1000
第三步：25（，+200：1600，+200：1500）7（，-200：1300，-200：1100）1（-200：1300，-200：1300，）抬起
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1100#7P1500#8P1300#9P1100#22P1300#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1500T1000
第四步：22（，，）4（，，）28（，，）向前走，和站立位置一样
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1300#7P1500#8P1300#9P1100#22P1500#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1300T1000
第五步：25（，0：1400，）7（，0：1500，）1（，-100：1400，）放下
#1P1300#2P1400#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1100#22P1500#23P1400#24P1300#25P1500#26P1400#27P1500#28P1500#29P1300#30P1300T1000
第六步：22（，，）4（，，）28（，，）抬起和第一步一样
#1P1300#2P1400#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1100#22P1300#23P1600#24P1300#25P1500#26P1400#27P1500#28P1500#29P1500#30P1500T1000

===========================
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
11、向左后方（7号脚在的方向）
第一步7（+200:1700，-200：1300，）25（，+200:1600，+200:1500）1（，-200:1300，-200:1100）抬起做出向前的动作
#1P1500#2P1300#3P1100#4P1500#5P1600#6P1300#7P1700#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1300T1000
第二步7（，100：1600，）25（，0：1400，）1（，0：1500，）放下
#1P1500#2P1500#3P1100#4P1500#5P1600#6P1300#7P1700#8P1600#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1500#28P1500#29P1300#30P1300T1000
第三步22（，+200：1600，+200：1500）4（，-200：1400，-200：1100）28（，+200：1500，）抬起
#1P1500#2P1500#3P1100#4P1500#5P1400#6P1100#7P1700#8P1600#9P1300#22P1500#23P1600#24P1500#25P1500#26P1400#27P1500#28P1700#29P1500#30P1300T1000
第四步7（，，）25（，，）1（，，）向前一步和站立位置相同
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1300#22P1500#23P1600#24P1500#25P1500#26P1400#27P1300#28P1700#29P1500#30P1300T1000
第五步：22（，0：1400，）4（，0：1600，）28（，，）放下
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1100#7P1500#8P1500#9P1300#22P1500#23P1400#24P1500#25P1500#26P1400#27P1300#28P1700#29P1400#30P1300T1000
第六步：7（，，）25（，，）1（，，）抬起和第一步一样
#1P1500#2P1300#3P1100#4P1500#5P1600#6P1100#7P1700#8P1300#9P1300#22P1500#23P1400#24P1500#25P1500#26P1600#27P1500#28P1700#29P1400#30P1300T1000
===========================
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
12、抬起前腿攻击状态
第一步4（，-200：1400，+400：1700）25（，+200：1600，-400：900）向前抬起
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1700#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P900#28P1500#29P1300#30P1300T1000

第二步4（，0：1600，）25（，0：1400，）放下
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1700#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P900#28P1500#29P1300#30P1300T1000

第三步1（，-900：600，+400：1700）28（，+1000：2300，-600：900）抬起前脚
#1P1500#2P600#3P1700#4P1700#5P1600#6P1700#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P900#28P1500#29P2300#30P900T1000



=========
站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
13
第一步1（，-200：1300，-200：1100）22（，+200：1600，-200:1100）向后抬起
#1P1500#2P1300#3P1100#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1600#24P1100#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
第二步1（，，）22（，，）放下
#1P1500#2P1500#3P1100#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1100#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
第三步：28（，+200：1500，+200：1500）7（，-200：1300，200：1500）
#1P1500#2P1500#3P1100#4P1500#5P1600#6P1300#7P1500#8P1300#9P1500#22P1500#23P1400#24P1100#25P1500#26P1400#27P1300#28P1500#29P1500#30P1500T1000
第四步：28（，，）7（，，）放下
#1P1500#2P1500#3P1100#4P1500#5P1600#6P1300#7P1500#8P1500#9P1500#22P1500#23P1400#24P1100#25P1500#26P1400#27P1300#28P1500#29P1300#30P1500T1000
第五步：4（，-600：1000，）25（，+1000：2400，）抬起中脚
#1P1500#2P1500#3P1100#4P1500#5P600#6P1300#7P1500#8P1500#9P1500#22P1500#23P1400#24P1100#25P1500#26P2400#27P1300#28P1500#29P1300#30P1500T1000
第六步：22（，，）

========
14（放弃，做不到4条腿走路，后面再说）

站立位置
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1300T1000
12、抬起前腿攻击状态
第一步4（，-200：1400，+800：2100）25（，+200：1600，-700：600）向前抬起
#1P1500#2P1500#3P1300#4P1500#5P1400#6P2100#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P600#28P1500#29P1300#30P1300T1000

第二步4（，0：1600，）25（，0：1400，）放下
#1P1500#2P1500#3P1300#4P1500#5P1600#6P2100#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P600#28P1500#29P1300#30P1300T1000

第三步1（，-900：600，+400：1700）28（，+1000：2300，-600：900）抬起前脚
#1P1500#2P600#3P1700#4P1500#5P1600#6P2100#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P600#28P1500#29P2300#30P900T1000

第四步22（，+200：1600，-800：500）
#1P1500#2P600#3P1700#4P1500#5P1600#6P2100#7P1500#8P1500#9P1300#22P1500#23P1600#24P500#25P1500#26P1400#27P600#28P1500#29P2300#30P900T1000

第五步22（，，）放下
#1P1500#2P600#3P1700#4P1500#5P1600#6P2100#7P1500#8P1500#9P1300#22P1500#23P1400#24P500#25P1500#26P1400#27P600#28P1500#29P2300#30P900T1000

第六步25（，+200：1600，+500：1100）
#1P1500#2P600#3P1700#4P1500#5P1600#6P2100#7P1500#8P1500#9P1300#22P1500#23P1400#24P500#25P1500#26P1600#27P1100#28P1500#29P2300#30P900T1000

第七步25（，，）22(,,)4(,,)放下
#1P1500#2P600#3P1700#4P1500#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P2300#30P900T1000

第八步7（，，）
#1P1500#2P600#3P1700#4P1500#5P1600#6P1900#7P1500#8P1300#9P2100#22P1500#23P1400#24P700#25P1500#26P1400#27P1100#28P1500#29P2300#30P900T1000


第九步7（，，）放下

====================
15、蹲下向前行走

站立位置
#1P700#2P800#3P1300#4P700#5P800#6P1300#7P700#8P700#9P1300#22P2100#23P2200#24P1300#25P2200#26P2200#27P1300#28P2300#29P2100#30P1300T1000

1、1（，，）7（，，）25（，，）向前


====================
#1P1500#2P1500#3P1700#28P1500#29P1300#30P900T1000
#1P2500#2P500#3P1700#28P550#29P2400#30P900T1000

#1P1200#2P700#3P1800#28P2200#29P2100#30P1500T500
测试
#1P1200#2P700#3P1700#4P1100#5P700#6P1900#7P1200#8P700#9P1700#22P2000#23P1850#24P1000#25P1900#26P2150#27P600#28P2200#29P2100#30P1200T500

#1P1200#2P700#3P1700
#4P1100#5P700#6P1400
#7P1200#8P700#9P2200
#22P2000#23P1850#24P900
#25P1900#26P2100#27P600
#28P2200#29P2100#30P1700T500

初始位置：
#1P2500#2P500#3P1400#4P2500#5P500#6P1400#7P2500#8P400#9P1400#22P900#23P2250#24P900#25P750#26P2450#27P1100#28P750#29P2400#30P1700T500
#1P2500#2P500#3P1400#4P2500#5P500#6P1400#7P2500#8P400#9P1400#22P900#23P2250#24P900#25P750#26P2450#27P1100#28P750#29P2400#30P1700

站立位置：
#1P1200#2P700#3P1400#4P1100#5P700#6P1400#7P1200#8P700#9P1400#22P2000#23P1850#24P900#25P1900#26P2100#27P1100#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P1400#4P1100#5P700#6P1400#7P1200#8P700#9P1400#22P2000#23P1850#24P900#25P1900#26P2100#27P1100#28P2200#29P2100#30P1700

扭腰动作：
#1P1200#2P700#3P1900#4P1100#5P700#6P1900#7P1200#8P700#9P2200#22P2000#23P1850#24P1400#25P1900#26P2100#27P1600#28P2200#29P2100#30P2200T500
#1P1200#2P700#3P1400#4P1100#5P700#6P1400#7P1200#8P700#9P1400#22P2000#23P1850#24P900#25P1900#26P2100#27P1100#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P800#4P1100#5P700#6P800#7P1200#8P700#9P900#22P2000#23P1850#24P600#25P1900#26P2100#27P600#28P2200#29P2100#30P1200T500

向前行走
=====趴在地上直接向后移动
#1P1200#2P700#3P1700#4P1100#5P700#6P1900#7P1200#8P700#9P2200#22P2000#23P1850#24P500#25P1900#26P2100#27P600#28P2200#29P2100#30P1200T500
#1P1200#2P700#3P1400#4P1100#5P700#6P1400#7P1200#8P700#9P1400#22P2000#23P1850#24P900#25P1900#26P2100#27P1100#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P800#4P1100#5P700#6P800#7P1200#8P700#9P900#22P2000#23P1850#24P1400#25P1900#26P2100#27P1600#28P2200#29P2100#30P2200T500
=====

#1P1200#2P500#3P1700#4P1100#5P700#6P1400#7P1200#8P400#9P2200#22P2000#23P1850#24P900#25P1900#26P2450#27P600#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P1700#4P1100#5P700#6P1400#7P1200#8P700#9P2200#22P2000#23P1850#24P900#25P1900#26P2100#27P600#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P1700#4P1100#5P500#6P1900#7P1200#8P700#9P2200#22P2000#23P2250#24P1400#25P1900#26P2100#27P600#28P2200#29P2400#30P1200T500
#1P1200#2P700#3P1700#4P1100#5P700#6P1900#7P1200#8P700#9P2200#22P2000#23P1850#24P500#25P1900#26P2100#27P600#28P2200#29P2100#30P1200T500

#1P1200#2P700#3P1400#4P1100#5P700#6P1400#7P1200#8P700#9P1400#22P2000#23P1850#24P900#25P1900#26P2100#27P1100#28P2200#29P2100#30P1700T500

#1P1200#2P700#3P1400#4P1100#5P700#6P800#7P1200#8P700#9P900#22P2000#23P1850#24P1400#25P1900#26P2100#27P1600#28P2200#29P2100#30P1700T500

#1P1200#2P500#3P1700#4P1100#5P700#6P800#7P1200#8P400#9P1700#22P2000#23P1850#24P1400#25P1900#26P2450#27P600#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P1700#4P1100#5P700#6P800#7P1200#8P700#9P1700#22P2000#23P1850#24P1400#25P1900#26P2150#27P600#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P1700#4P1100#5P500#6P800#7P1200#8P700#9P1700#22P2000#23P2250#24P1400#25P1900#26P2150#27P600#28P2200#29P2400#30P1700T500
#1P1200#2P700#3P1700#4P1100#5P500#6P1900#7P1200#8P700#9P1700#22P2000#23P2250#24P1000#25P1900#26P2150#27P600#28P2200#29P2400#30P1200T500
#1P1200#2P700#3P1700#4P1100#5P700#6P1900#7P1200#8P700#9P1700#22P2000#23P1850#24P1000#25P1900#26P2150#27P600#28P2200#29P2100#30P1200T500

站立---向前迈3脚（1，2）---向前迈2脚和向后迈1脚---这个是一个向前冲得动作，行动不够连贯
#1P1200#2P700#3P1400#4P1100#5P700#6P1400#7P1200#8P700#9P1400#22P2000#23P1850#24P900#25P1900#26P2100#27P1100#28P2200#29P2100#30P1700T500
#1P1200#2P500#3P1700#4P1100#5P700#6P1400#7P1200#8P400#9P2200#22P2000#23P1850#24P900#25P1900#26P2450#27P600#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P1700#4P1100#5P700#6P1400#7P1200#8P700#9P2200#22P2000#23P1850#24P900#25P1900#26P2100#27P600#28P2200#29P2100#30P1700T500
#1P1200#2P700#3P1700#4P1100#5P500#6P1900#7P1200#8P700#9P2200#22P2000#23P2250#24P1400#25P1900#26P2100#27P600#28P2200#29P2400#30P1200T500
#1P1200#2P700#3P1700#4P1100#5P700#6P1900#7P1200#8P700#9P2200#22P2000#23P1850#24P1400#25P1900#26P2100#27P600#28P2200#29P2100#30P1200T500
#1P1200#2P700#3P800#4P1100#5P700#6P800#7P1200#8P700#9P900#22P2000#23P1850#24P1400#25P1900#26P2100#27P1600#28P2200#29P2100#30P2200T500
#1P1200#2P500#3P800#4P1100#5P700#6P800#7P1200#8P400#9P900#22P2000#23P1850#24P1400#25P1900#26P2450#27P1600#28P2200#29P2100#30P2200T500
#1P1200#2P500#3P1400#4P1100#5P700#6P800#7P1200#8P400#9P1400#22P2000#23P1850#24P1400#25P1900#26P2450#27P1100#28P2200#29P2100#30P2200T500
#1P1200#2P700#3P1400#4P1100#5P700#6P800#7P1200#8P700#9P1400#22P2000#23P1850#24P1400#25P1900#26P2100#27P1100#28P2200#29P2100#30P2200T500
#1P1200#2P700#3P1400#4P1100#5P500#6P800#7P1200#8P700#9P1400#22P2000#23P2250#24P1400#25P1900#26P2100#27P1100#28P2200#29P2400#30P2200T500
#1P1200#2P700#3P1400#4P1100#5P500#6P1400#7P1200#8P700#9P1400#22P2000#23P2250#24P900#25P1900#26P2100#27P1100#28P2200#29P2400#30P1700T500

向前行走，均匀前行
#1P1200#2P800#3P1400#4P1100#5P800#6P1400#7P1200#8P800#9P1400#22P2000#23P1750#24P900#25P1900#26P2000#27P1100#28P2200#29P2000#30P1700T500
#1P1200#2P500#3P1700#4P1100#5P800#6P1400#7P1200#8P400#9P2200#22P2000#23P1750#24P900#25P1900#26P2450#27P600#28P2200#29P2000#30P1700T500
#1P1200#2P800#3P1700#4P1100#5P800#6P1400#7P1200#8P800#9P2200#22P2000#23P1750#24P900#25P1900#26P2000#27P600#28P2200#29P2000#30P1700T500
#1P1200#2P800#3P1700#4P1100#5P500#6P1900#7P1200#8P800#9P2200#22P2000#23P2250#24P1400#25P1900#26P2000#27P600#28P2200#29P2400#30P1200T500
#1P1200#2P800#3P1700#4P1100#5P800#6P1900#7P1200#8P800#9P2200#22P2000#23P1750#24P1400#25P1900#26P2000#27P600#28P2200#29P2000#30P1200T500
#1P1200#2P800#3P1400#4P1100#5P800#6P1400#7P1200#8P800#9P1400#22P2000#23P1750#24P1400#25P1900#26P2000#27P1100#28P2200#29P2000#30P1700T500
#1P1200#2P500#3P1700#4P1100#5P800#6P1400#7P1200#8P400#9P2200#22P2000#23P1750#24P1400#25P1900#26P2450#27P600#28P2200#29P2000#30P1700T500
#1P1200#2P800#3P1700#4P1100#5P800#6P1400#7P1200#8P800#9P2200#22P2000#23P1750#24P1400#25P1900#26P2000#27P600#28P2200#29P2000#30P1700T500
#1P1200#2P800#3P1700#4P1100#5P500#6P1900#7P1200#8P800#9P2200#22P2000#23P2250#24P1400#25P1900#26P2000#27P600#28P2200#29P2400#30P1200T500
#1P1200#2P800#3P1700#4P1100#5P800#6P1900#7P1200#8P800#9P2200#22P2000#23P1750#24P900#25P1900#26P2000#27P600#28P2200#29P2000#30P1200T500

顺时针旋转
#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1100#22P1800#23P1350#24P1400#25P1700#26P1600#27P1300#28P1800#29P1600#30P1700T200
#1P1600#2P1000#3P1700#4P1500#5P1200#6P1400#7P1500#8P1000#9P1500#22P1800#23P1350#24P1400#25P1700#26P1800#27P900#28P1800#29P1600#30P1700T400
#1P1600#2P1200#3P1700#4P1500#5P1200#6P1400#7P1500#8P1200#9P1500#22P1800#23P1350#24P1400#25P1700#26P1600#27P900#28P1800#29P1600#30P1700T100
#1P1600#2P1200#3P1700#4P1500#5P1000#6P1800#7P1500#8P1200#9P1500#22P1800#23P1550#24P1000#25P1700#26P1600#27P900#28P1800#29P1800#30P1200T400
#1P1600#2P1200#3P1700#4P1500#5P1200#6P1800#7P1500#8P1200#9P1500#22P1800#23P1350#24P1000#25P1700#26P1600#27P900#28P1800#29P1600#30P1200T100

前两脚向前走
#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P1100#28P1800#29P1600#30P1700T300
#1P2000#2P800#3P1700#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P1100#28P1400#29P1900#30P1200T300
#1P2000#2P1200#3P1700#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P1100#28P1400#29P1500#30P1200T100
#1P1600#2P1200#3P1700#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P1100#28P1800#29P1500#30P1200T100
#1P1600#2P1000#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P1100#28P1800#29P1800#30P1700T100
#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P1100#28P1800#29P1600#30P1700T100

中间两脚向前走
#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P13.50#24P900#25P1700#26P1600#27P1100#28P1800#29P1600#30P1700T300
#1P1600#2P1200#3P1200#4P1500#5P800#6P1900#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P2000#27P600#28P1800#29P1600#30P1700T300
#1P1600#2P1200#3P1200#4P1500#5P1200#6P1900#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P600#28P1800#29P1600#30P1700T300

后两脚
#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1400#22P1800#23P1350#24P900#25P1700#26P1600#27P1100#28P1800#29P1600#30P1700T300



向前走
#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1300#22P1800#23P1350#24P1300#25P1700#26P1600#27P1100#28P1800#29P1600#30P1700T300
发现可以中间两个脚要向人一样有节奏得来回走。前面2个脚向外走。

模仿youtube视频得六足机器人向前走

#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1300#22P1800#23P1350#24P1300#25P1700#26P1600#27P1100#28P1800#29P1600#30P1700T300

#1P1600#2P1200#3P1200#4P1500#5P1200#6P1400#7P1500#8P1200#9P1300#22P1800#23P1350#24P1300#25P1700#26P1600#27P1100#28P1800#29P1600#30P1700T300
#1P2000#2P800#3P1500#4P1500#5P1200#6P1400#7P1100#8P600#9P1300#22P1800#23P1350#24P1300#25P1700#26P2000#27P900#28P1800#29P1600#30P1700T300
#1P2000#2P1200#3P1500#4P1500#5P1200#6P1400#7P1100#8P1200#9P1300#22P1800#23P1350#24P1300#25P1700#26P1600#27P900#28P1800#29P1600#30P1700T300
#1P2000#2P1200#3P1500#4P1500#5P900#6P1700#7P1100#8P1200#9P1300#22P2100#23P1850#24P1300#25P1700#26P1600#27P900#28P1400#29P2000#30P1500T300


第一步：
#1P1100#2P1500#3P1700#4P1500#5P1200#6P1600#7P2000#8P1700#9P1300#22P2000#23P2000#24P1300#25P1500#26P1500#27P1600#28P1200#29P1900#30P900T1000
#1P1100#2P1500#3P1700#4P1500#5P1500#6P1600#7P2000#8P1700#9P1300#22P2000#23P1600#24P1300#25P1500#26P1500#27P1600#28P1200#29P1100#30P900T1000
#1P1400#2P900#3P1700#4P1500#5P1500#6P1600#7P1000#8P1000#9P1300#22P2000#23P1600#24P1300#25P1500#26P1800#27P1300#28P1200#29P1100#30P900T1000
#1P1400#2P900#3P1700#4P1500#5P1500#6P1000#7P1000#8P1000#9P1300#22P1500#23P1600#24P1300#25P1500#26P1800#27P1300#28P1800#29P1100#30P900T1000
#1P1400#2P1500#3P1700#4P1500#5P1500#6P1000#7P1000#8P1400#9P1300#22P1500#23P1600#24P1300#25P1500#26P1500#27P1300#28P1800#29P1100#30P900T1000


向前
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1300#7P1300#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000
#1P1700#2P1600#3P1300#4P1500#5P1600#6P1300#7P1300#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T1000
#1P1700#2P1600#3P1300#4P1500#5P1400#6P1500#7P1300#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1100#28P1300#29P1500#30P1300T1000
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1300#28P1300#29P1500#30P1300T1000
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1700#23P1400#24P1300#25P1500#26P1400#27P1300#28P1300#29P1200#30P1300T1000
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1500#7P1300#8P1300#9P1300#22P1700#23P1400#24P1300#25P1500#26P1600#27P1100#28P1300#29P1200#30P1300T1000


向后
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1300#7P1700#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1300T1000
#1P1300#2P1500#3P1300#4P1500#5P1600#6P1300#7P1700#8P1600#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1500#28P1500#29P1300#30P1300T1000
#1P1300#2P1500#3P1300#4P1500#5P1400#6P1100#7P1700#8P1600#9P1300#22P1300#23P1600#24P1300#25P1500#26P1400#27P1500#28P1700#29P1500#30P1300T1000
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1300#22P1300#23P1600#24P1300#25P1500#26P1400#27P1300#28P1700#29P1500#30P1300T1000
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1100#7P1500#8P1500#9P1300#22P1300#23P1400#24P1300#25P1500#26P1400#27P1300#28P1700#29P1300#30P1300T1000
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1100#7P1700#8P1300#9P1300#22P1300#23P1400#24P1300#25P1500#26P1600#27P1500#28P1700#29P1300#30P1300T1000


向左
#1P1500#2P1500#3P1300#4P1700#5P1400#6P1300#7P1500#8P1500#9P1300#22P1500#23P1600#24P1500#25P1500#26P1400#27P1300#28P1500#29P1500#30P1100T1000
#1P1500#2P1500#3P1300#4P1700#5P1650#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1500#25P1500#26P1400#27P1300#28P1500#29P1300#30P1100T1000
#1P1500#2P1300#3P1100#4P1700#5P1650#6P1300#7P1500#8P1300#9P1500#22P1500#23P1400#24P1500#25P1700#26P1600#27P1300#28P1500#29P1300#30P1100T1000
#1P1500#2P1300#3P1100#4P1500#5P1600#6P1300#7P1500#8P1300#9P1500#22P1500#23P1400#24P1300#25P1700#26P1600#27P1300#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1100#4P1500#5P1600#6P1300#7P1500#8P1500#9P1500#22P1500#23P1400#24P1300#25P1700#26P1400#27P1300#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1100#4P1700#5P1400#6P1300#7P1500#8P1500#9P1500#22P1500#23P1600#24P1500#25P1700#26P1400#27P1300#28P1500#29P1500#30P1100T1000

向右
#1P1500#2P1300#3P1500#4P1500#5P1600#6P1300#7P1500#8P1300#9P1100#22P1500#23P1400#24P1300#25P1300#26P1600#27P1300#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1500#4P1500#5P1600#6P1300#7P1500#8P1500#9P1100#22P1500#23P1400#24P1300#25P1300#26P1300#27P1300#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1500#4P1300#5P1400#6P1300#7P1500#8P1500#9P1100#22P1500#23P1600#24P1100#25P1300#26P1300#27P1300#28P1500#29P1500#30P1500T1000
#1P1500#2P1500#3P1300#4P1300#5P1400#6P1300#7P1500#8P1500#9P1300#22P1500#23P1600#24P1100#25P1500#26P1400#27P1300#28P1500#29P1500#30P1500T1000
#1P1500#2P1500#3P1300#4P1300#5P1600#6P1300#7P1500#8P1500#9P1300#22P1500#23P1400#24P1100#25P1500#26P1400#27P1300#28P1500#29P1300#30P1500T1000
#1P1500#2P1300#3P1500#4P1300#5P1600#6P1300#7P1500#8P1300#9P1100#22P1500#23P1400#24P1100#25P1300#26P1600#27P1300#28P1500#29P1300#30P1500T1000

向左前
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1300#7P1500#8P1300#9P1500#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000
#1P1700#2P1600#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1500#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T1000
#1P1700#2P1600#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1500#22P1700#23P1600#24P1300#25P1500#26P1400#27P1100#28P1500#29P1500#30P1100T1000
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1700#23P1600#24P1300#25P1500#26P1400#27P1300#28P1500#29P1500#30P1100T1000
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1700#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1100T1000
#1P1700#2P1300#3P1300#4P1500#5P1600#6P1500#7P1500#8P1300#9P1500#22P1700#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1100T1000


向右前
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1500#7P1500#8P1500#9P1300#22P1500#23P1600#24P1100#25P1500#26P1400#27P1300#28P1300#29P1500#30P1300T1000
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1500#7P1500#8P1500#9P1300#22P1500#23P1400#24P1100#25P1500#26P1400#27P1300#28P1300#29P1200#30P1300T1000
#1P1500#2P1300#3P1500#4P1500#5P1600#6P1500#7P1300#8P1300#9P1300#22P1500#23P1400#24P1100#25P1500#26P1600#27P1100#28P1300#29P1200#30P1300T1000
#1P1500#2P1300#3P1500#4P1500#5P1600#6P1300#7P1300#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1100#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1500#4P1500#5P1600#6P1300#7P1300#8P1500#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1100#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1500#4P1500#5P1400#6P1500#7P1300#8P1500#9P1300#22P1500#23P1600#24P1100#25P1500#26P1400#27P1100#28P1300#29P1500#30P1300T1000

向右后
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1300#22P1300#23P1600#24P1300#25P1500#26P1400#27P1300#28P1500#29P1500#30P1500T1000
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1100#7P1500#8P1500#9P1300#22P1300#23P1400#24P1300#25P1500#26P1400#27P1300#28P1500#29P1300#30P1500T1000
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1100#7P1500#8P1300#9P1100#22P1300#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1500T1000
#1P1300#2P1300#3P1300#4P1500#5P1600#6P1300#7P1500#8P1300#9P1100#22P1500#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1300T1000
#1P1300#2P1400#3P1300#4P1500#5P1600#6P1300#7P1500#8P1500#9P1100#22P1500#23P1400#24P1300#25P1500#26P1400#27P1500#28P1500#29P1300#30P1300T1000
#1P1300#2P1400#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1100#22P1300#23P1600#24P1300#25P1500#26P1400#27P1500#28P1500#29P1500#30P1500T1000


向左后
#1P1500#2P1300#3P1100#4P1500#5P1600#6P1300#7P1700#8P1300#9P1300#22P1500#23P1400#24P1300#25P1500#26P1600#27P1500#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1100#4P1500#5P1600#6P1300#7P1700#8P1600#9P1300#22P1500#23P1400#24P1300#25P1500#26P1400#27P1500#28P1500#29P1300#30P1300T1000
#1P1500#2P1500#3P1100#4P1500#5P1400#6P1100#7P1700#8P1600#9P1300#22P1500#23P1600#24P1500#25P1500#26P1400#27P1500#28P1700#29P1500#30P1300T1000
#1P1500#2P1500#3P1300#4P1500#5P1400#6P1100#7P1500#8P1500#9P1300#22P1500#23P1600#24P1500#25P1500#26P1400#27P1300#28P1700#29P1500#30P1300T1000
#1P1500#2P1500#3P1300#4P1500#5P1600#6P1100#7P1500#8P1500#9P1300#22P1500#23P1400#24P1500#25P1500#26P1400#27P1300#28P1700#29P1400#30P1300T1000
#1P1500#2P1300#3P1100#4P1500#5P1600#6P1100#7P1700#8P1300#9P1300#22P1500#23P1400#24P1500#25P1500#26P1600#27P1500#28P1700#29P1400#30P1300T1000

*/