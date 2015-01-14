package mini_camel;

public class ErrMsg {

    public Type type;
    public String message;

    public Exception ex;

    public enum Type {
        INFO,
        WARN,
        ERROR
    }


}
