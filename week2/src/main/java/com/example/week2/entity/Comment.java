package com.example.week2.entity;

import com.example.week2.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BLOG_ID", nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikeList = new ArrayList<>();

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    public Comment(CommentRequestDto commentRequestDto, Blog blog, User user) {
        this.comment = commentRequestDto.getComment();
        this.username = user.getUsername();
        this.blog = blog;
        this.user = user;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}
