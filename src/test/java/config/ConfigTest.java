package config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ConfigTest {
    @Before
    public void before() throws Exception{

    }

    @After
    public void after()throws Exception{

    }

    @Test
    public void testloadActions_select(){
        Config cfg = new Config();

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
