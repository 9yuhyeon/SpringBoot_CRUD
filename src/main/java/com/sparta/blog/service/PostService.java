package com.sparta.blog.service;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.respository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

// Service의 역할 : 컨트롤러가 전달한 데이터를 받아 특정 로직을 수행 후 저장을 위해 Respository로 전달 및 컨트롤러에 다시 응답값 전달

// Service는 Repository 타입의 멤버를 가진다.
// Service가 생성될 때 생성자를 통해 Respository를 주입받는다. (주입은 bean에 의해서)
// 컨트롤러로부터 전달받은 RequestDto를 통해 Entity 객체를 만들어 DB에 저장
// 저장 후 다시 생성한 Entity 객체를 ResponseDto 객체에 담아 컨트롤러로 보낸다.
@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 작성 서비스 로직
    public PostResponseDto create(PostRequestDto requestDto) {
        // RequestDto -> Entity
        Post post = new Post(requestDto); // 전달받은 Dto로 Entity 객체 생성
        // Entity -> DB SAVE
        Post savePost = postRepository.save(post); // Dto로 생성한 Entity를 DB에 저장
        // Entity -> ResponseDto
        PostResponseDto responseDto = new PostResponseDto(savePost);
        return responseDto;
    }

    // 전체 게시글 조회 서비스 로직
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    // 선택한 게시글 조회 서비스 로직
    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        PostResponseDto responseDto = new PostResponseDto(post);

        return responseDto;
    }

    // 선택한 게시글 수정 서비스 로직
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = findPost(id);
        if (validate(post.getPassword(), requestDto.getPassword())){
            post.update(requestDto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 맞지 않습니다.");
        }
        return new PostResponseDto(post);
    }

    // 선택한 게시글 삭제 서비스 로직
    public String deletePost(Long id, Long password) {
        Post post = findPost(id);
        if (validate(post.getPassword(),password)){
            postRepository.delete(post);
            return "Success";
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 맞지 않습니다.");
        }
    }

    // 해당 id의 게시글이 존재하는지 확인
    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND,"게시글이 존재하지 않습니다.")
        );
    }

    // 비밀번호 검증 메서드
    private boolean validate(Long password1, Long password2){
        if (Objects.equals(password1,password2)) return true;
        return false;
    }
}
