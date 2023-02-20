package SpringDB.demo.connection;

public abstract class ConnectionConst { // 객체를 생성하면 안되므로 abstract 로 막아놓음
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
