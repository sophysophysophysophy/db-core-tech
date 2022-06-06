package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * JDBC - DriverManager 사용
 */

@Slf4j
public class MemberRepositoryV0 {


    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (? ,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            // 파라미터 바인딩을 해야 단순 데이터로 취급 됨. sql insection 공격 문제 해결 가능
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            int count = pstmt.executeUpdate();  // 영향받은 row 수만큼 반환
            return member;
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
//            pstmt.close();  // Exception : con.close 호출 안될 수 있음 -> try catch 로 각각 묶어야 함
//            con.close();

            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error ", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error ", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error ", e);
            }
        }


    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
