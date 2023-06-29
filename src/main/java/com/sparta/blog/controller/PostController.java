package com.sparta.blog.controller;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 컨트롤러 역할 : 클라이언트의 요청을 받아 알맞은 서비스의 메서드로 데이터를 dto에 담아 함께 전달

// 컨트롤러는 Service 타입의 멤버를 가진다.
// 컨트롤러가 생성될 때 생성자를 통해 Service를 주입받는다. (주입은 main이 아닌 bean이 해준다)
// HTTP 요청 시 해당 요청에 맞는 Service의 메서드에 requestDto를 담아 전달한다.
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성 api
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto){
        return postService.create(requestDto);
    }

    // 전체 게시글 조회 api
    @GetMapping("/posts")
    public List<PostResponseDto> getAllPosts(){
        return postService.getAllPosts();
    }

    // 선택한 게시글 조회 api
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    // 선택한 게시글 수정 api
    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto){
        return postService.updatePost(id, requestDto);
    }

    // 선택한 게시글 삭제 api
    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable Long id, @RequestBody PostRequestDto passwordDto) {
        Long password = passwordDto.getPassword();
        return postService.deletePost(id,password);
    }
}
