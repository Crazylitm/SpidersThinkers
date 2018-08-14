package Thinkers;

import baseactions.Actions;
import Spider.*;
public interface Thinker {
    Actions getNextActionType(Actions action);
}
