package config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigTest {
    @Before
    public void before() throws Exception{

    }

    @After
    public void after()throws Exception{

    }

    @Test
    public void testloadActions_select(){
       // Config cfg = new Config();

        SetRunString("#1P2500#2P500#3P1300#4P2500#5P500#6P1300#7P2500#8P400#9P1300#22P500#23P2500#24P1300#25P550#26P2500#27P1300#28P550#29P2400#30P1300T1000");

    }
    public void SetRunString(String actionstring){

        String pos_params[] = actionstring.split("#");
        Map<Integer,Integer> pos_params_map = new ConcurrentHashMap<Integer, Integer>();
        for(String cur : pos_params){
            if(cur.equals("") == true) continue;
            int i = cur.indexOf("T");
            if( i != -1){
                cur = cur.substring(0,i);
            }
            String str[] = cur.split("P");
            pos_params_map.put(Integer.valueOf(str[0]),Integer.valueOf(str[1]));
        }

    }
    @Test
    public void testgetCommandByType(){
        Config cfg = new Config();
        try {
            List<ActionString>  ret = cfg.getCommandByType("LEFTWARD");
            System.out.println("======================================================");
            System.out.println(ret);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
