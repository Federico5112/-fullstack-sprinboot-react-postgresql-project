package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Like;
import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import com.example.demo.repository.LikeRepository;
import com.example.demo.requests.LikeCreateRequest;
import com.example.demo.responses.LikeResponse;

@Service
public class LikeService {

    // Clean Code: Değişmez (Immutable) alanlar için 'final' eklendi
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    // Circular Dependency'yi her iki taraftan da kırmak için @Lazy eklendi
    public LikeService(LikeRepository likeRepository, UserService userService,
                       @Lazy PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        if(userId.isPresent() && postId.isPresent()) {
            list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        } else if(userId.isPresent()) {
            list = likeRepository.findByUserId(userId.get());
        } else if(postId.isPresent()) {
            list = likeRepository.findByPostId(postId.get());
        } else {
            list = likeRepository.findAll();
        }
        // Clean Code: Lambda yerine Method Reference kullanıldı
        return list.stream().map(LikeResponse::new).collect(Collectors.toList());
    }

    // Clean Code: Parametre isimlendirmesi düzeltildi (LikeId -> likeId)
    public Like getOneLikeById(Long likeId) {
        return likeRepository.findById(likeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest request) {
        User user = userService.getOneUserById(request.getUserId());
        Post post = postService.getOnePostById(request.getPostId());

        if(user != null && post != null) {
            Like likeToSave = new Like();
            // MANTIK HATASI SİLİNDİ: likeToSave.setId(request.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            return likeRepository.save(likeToSave);
        }
        return null;
    }

    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}