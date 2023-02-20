package SpringDB.demo.repository;

import SpringDB.demo.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
class MemberRepositoryV0Test {
    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberV4", 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);

        Assertions.assertThat(findMember).isEqualTo(member); // 인스턴스 두 개는 다름(값만 같음-> lombok의 Data에 equalsAndHashCode 포함)
    }

}