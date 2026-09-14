package net.likelion.bebc25.sns.security.oauth;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.sns.domain.Member;
import net.likelion.bebc25.sns.mapper.MemberMapper;
import net.likelion.bebc25.sns.security.principal.CustomUserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;

    public CustomOAuth2UserService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 스프링 기본 구현체를 통해 구글 UserInfo 엔드포인트에서 프로필 JSON 정보 조회
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 구글 응답 JSON 속성 획득 (단순 1차원 구조)
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("구글 OAuth2 사용자 프로필 속성: {}", attributes);
        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name");

        // 3. DB 조회 후 최초 로그인이면 자동 회원가입 진행
        Member member = saveOrUpdate(email, nickname);

        // 4. 통합 인증 주체 반환
        return new CustomUserDetails(member, attributes);
    }

    private Member saveOrUpdate(String email, String nickname) {
        Member existingMember = memberMapper.findByEmail(email);
        if (existingMember == null) {
            Member newMember = Member.builder()
                    .email(email)
                    .password("") // 소셜 회원은 자체 비밀번호가 없으므로 빈 문자열 저장
                    .nickname(nickname)
                    .role("ROLE_USER")
                    .build();
            memberMapper.save(newMember);
            log.info("신규 구글 소셜 회원 DB 자동 가입 완료: ID={}, Email={}", newMember.getId(), newMember.getEmail());
            return newMember;
        }
        return existingMember;
    }
}