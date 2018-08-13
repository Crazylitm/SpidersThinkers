package Spider;

import legs.Leg;

public interface Spider {
     Leg getLeg_head_left();

     Leg getLeg_middle_left();

     Leg getLeg_tail_left();

     Leg getLeg_head_right();

     Leg getLeg_middle_right() ;

     Leg getLeg_tail_right() ;

     void commit();
}
