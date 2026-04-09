package com.example.demo.responses;

import com.example.demo.entities.Post;
import lombok.Data;
import java.util.List;

@Data
public class PostResponse {
    Long id;
    Long userId;
    String userName;
    String title;
    String text;
    List<LikeResponse> postLikes; // Beğenileri taşıyacak yeni alan

    // Constructor'ı bu listeyi de alacak şekilde güncelliyoruz
    public PostResponse(Post entity, List<LikeResponse> likes) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.postLikes = likes;
    }
}