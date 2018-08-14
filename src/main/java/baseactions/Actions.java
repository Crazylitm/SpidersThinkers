package baseactions;

import Spider.Spider;

public interface Actions {
     void run(Spider spider);
     void run();
     void SetActionType(String type);
     String getType();
     Boolean isEnd();
}
