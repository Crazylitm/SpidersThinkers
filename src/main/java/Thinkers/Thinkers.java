package Thinkers;

import baseactions.Actions;

public interface Thinkers {
    public Actions next();
    public Actions get(String actionname);
    public void set(String actionname,Actions action);
}
