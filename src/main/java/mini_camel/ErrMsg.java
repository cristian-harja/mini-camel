package mini_camel;

import ldf.java_cup.runtime.LocationAwareEntity;

public class ErrMsg {

    public Type type;
    public LocationAwareEntity loc;
    public String message;

    public Exception ex;

    public enum Type {
        INFO,
        WARN,
        ERROR
    }


}
