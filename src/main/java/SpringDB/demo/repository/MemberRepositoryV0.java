package SpringDB.demo.repository;

import SpringDB.demo.connection.DBConnectionUtil;
import SpringDB.demo.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

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

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        // try-catch문 밖에서도 값이 전달되어야하므로 선언 필요
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        // Result set
        // : 쿼리 결과가 순서대로 들어간 데이터 객체
        // 내부의 커서로 컬럼 하나씩을 가져옴 (다음 : rs.next() == true : 데이터가 있음)
        // getString / getInt 로 값을 반환할 수 있음
        try {
            con = getConnection();
            psmt = con.prepareStatement(sql);
            psmt.setString(1, memberId);
            rs = psmt.executeQuery();// select 문에서는 결과가 나와야하므로
            if (rs.next()) { // 값을 가져와 매핑해 줌
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, psmt, rs);
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

        if (stmt != null) {            try {
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
