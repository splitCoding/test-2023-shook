package shook.shook.member.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shook.shook.member.domain.Email;
import shook.shook.member.domain.Member;
import shook.shook.member.domain.repository.MemberRepository;
import shook.shook.member.exception.MemberException;
import shook.shook.support.UsingJpaTest;

class MemberServiceTest extends UsingJpaTest {

    private static Member savedMember;

    @Autowired
    private MemberRepository memberRepository;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
        savedMember = memberRepository.save(new Member("woowa@wooteco.com", "shook"));
    }

    @DisplayName("회원을 등록한다.")
    @Test
    void register() {
        //given
        final String email = "shook@wooteco.com";

        //when
        final Member result = memberService.register(email);

        //then
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(email);
    }

    @DisplayName("중복된 이메일로 회원을 등록되는 경우 예외를 던진다.")
    @Test
    void register_fail_alreadyExistMember() {
        // given
        final String email = "woowa@wooteco.com";

        // when
        // then
        assertThatThrownBy(() -> memberService.register(email))
            .isInstanceOf(MemberException.ExistMemberException.class);
    }

    @DisplayName("회원을 이메일로 조회한다.")
    @Test
    void findByEmail() {
        //given
        //when
        final Optional<Member> result = memberService.findByEmail(
            new Email(savedMember.getEmail()));

        //then
        assertThat(result.get().getId()).isEqualTo(savedMember.getId());
        assertThat(result.get().getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(result.get().getNickname()).isEqualTo(savedMember.getNickname());
    }
}
