package net.likelion.bebc25.sns.controller;

import jakarta.validation.Valid;
import net.likelion.bebc25.sns.dto.*;
import net.likelion.bebc25.sns.security.principal.CustomUserDetails;
import net.likelion.bebc25.sns.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {
    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> getPostList(
            @ModelAttribute PostSearchRequest searchRequest
    ) {
        // 검색어에 해당하는 게시글 목록 조회
        List<PostResponse> posts = postService.searchPosts(searchRequest);
        return ResponseEntity.ok(posts);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
//            @RequestHeader("X-Member-Id") Long memberId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PostCreateRequest request
    ) {
        request.setMemberId(userDetails.getId());
        PostResponse createdPost = postService.createPost(request);
        URI location = URI.create("/api/v1/posts" + createdPost.id());
        return ResponseEntity.created(location).body(createdPost);
    }

    // 게시글 한 건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable("id") Long id
    ) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("id") Long id,
//            @RequestHeader("X-Member-Id") Long memberId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        // 게시글 수정 및 수정된 상태의 게시글 정보 반환
        postService.updatePost(id, request);
        PostResponse updatedPost = postService.getPostById(id);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<PostResponse> deletePost(
            @PathVariable("id") Long id,
//            @RequestHeader("X-Member-Id") Long memberId
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 게시글 수정 및 수정된 상태의 게시글 정보 반환
        postService.deletePost(id);

        return ResponseEntity.status(204).build();
    }
}
