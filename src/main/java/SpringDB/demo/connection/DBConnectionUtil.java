package SpringDB.demo.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static SpringDB.demo.connection.ConnectionConst.PASSWORD;
import static SpringDB.demo.connection.ConnectionConst.URL;
import static SpringDB.demo.connection.ConnectionConst.USERNAME;

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // JDBC 가 제공하는 driver manager를 통해 가져오게 됨
            log.info("get connection={}, class={}", connection, connection.getClass()); // class=class org.h2.jdbc.JdbcConnection
            // JDBC Connection 인터페이스를 구현한 구현체
            // DB가 바뀌어도 커넥션을 수정할 필요가 없음

            //Driver Manager
            // : 라이브러리에 등록된 드라이버 목록 자동으로 인식
            // 각각의 드라이버는 url을 넘겨서 커넥션을 획득할 수 있는지 확인 - url로 확인(jdbc:h2...)
            // 찾는 커넥션 구현체를 클라이언트에 반환
             return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
