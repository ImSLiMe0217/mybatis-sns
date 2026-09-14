package net.likelion.bebc25.sns.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PostCreateRequest {
    @Schema(hidden = true)
    private Long id;

    //    @NotNull
    @Schema(hidden = true)
    private Long memberId;

    @Schema(description = "게시글 본문 내용", example = "스프링 부트 학습중...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "본문을 입력해주세요.")
    @Size(max = 1000, message = "본문은 최대 1000자 이하로만 입력해주세요")
    private String content;

    @Schema(description = "첨부 이미지 URL", example = "https://sample.com/images/hello.png")
    private String imageUrl;

    public PostCreateRequest(Long memberId, String content, String imageUrl) {
        this.memberId = memberId;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
