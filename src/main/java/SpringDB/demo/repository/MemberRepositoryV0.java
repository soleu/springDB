package SpringDB.demo.repository;

import SpringDB.demo.connection.DBConnectionUtil;
import SpringDB.demo.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC - DriverManger 사용
 */
@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?,?)"; // ?가 아닌 하드코딩을 하게되면 sql injection 해킹 문제가 발생할 수 있음

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql); // db에 전달할 SQL과 파라미터 등 데이터를 준비
            pstmt.setString(1, member.getMemberId()); // 파라미터 바인딩
            pstmt.setInt(2, member.getMoney()); // 파라미터 바인딩
            int i = pstmt.executeUpdate();// 쿼리 실행 (영향 받은 row 수 반환)
            log.info("{}개의 row 영향 받음", i);
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e; // 확인하기 위해 로그 정보 남기고 밖으로 다시 던진다
        } finally {// 커넥션을 닫아줘야함 (리소스 누수 방지)
//            pstmt.close();  //여기서 exception이 터지면 아래 con.close()가 작동이 안될수 있음
//            con.close();

            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        // exception이 터지더라도 다 작동이 되게끔
        if (rs != null) {
            try {
                rs.close(); // Exception
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close(); // Exception
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (con != null) {
            try {
                con.close(); // Exception
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
