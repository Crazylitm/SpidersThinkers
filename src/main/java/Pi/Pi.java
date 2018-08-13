package Pi;

public interface Pi {
    void init();
    void clear();
    String write(String Data);
    String commit(String runtime);// after write function be called ,u call commit function ,then send to PI;

}
