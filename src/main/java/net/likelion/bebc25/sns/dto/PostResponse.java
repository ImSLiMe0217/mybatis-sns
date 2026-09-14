package net.likelion.bebc25.sns.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "저장된 게시글 본문 내용", example = "스프링 부트 학습중")
        String content,

        @Schema(description = "저장된 첨부 이미지 URL", example = "https://sample.com/image/hello.png")
        String imageUrl,

        @Schema(description = "좋아요 수", example = "201")
        int likeCount,

        @Schema(description = "생성된 날짜", example = "2026-09-08 13:23:00")
        LocalDateTime createdAt,

        @Schema(description = "수정된 날짜", example = "2026-09-08 14:23:00")
        LocalDateTime updateAt
) {
    // 신규 게시글 등록 요청 DTD로부터 게시글 응답 DTD를 생성하는 팩토리 메서드
    public static PostResponse from(PostCreateRequest dto) {
        return new PostResponse(
                dto.getId(),
                dto.getMemberId(),
                dto.getContent(),
                dto.getImageUrl(),
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
