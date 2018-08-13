package baseactions;
import Spider.*;
public class Forward extends ActionsBase{

    public Forward(String name){
        super(name);
    }
    public Forward(){
        super("forward");
    }
    public void run(Spider spider){
            spider.getLeg_head_left().run( );
            spider.getLeg_middle_left().run();
            spider.getLeg_tail_left().run();
            spider.getLeg_head_right().run();
            spider.getLeg_middle_right().run();
            spider.getLeg_tail_right().run();


    }
}
