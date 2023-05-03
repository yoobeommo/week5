package com.example.week2.entity;

import com.example.week2.dto.BlogRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name="Blog")
@NoArgsConstructor
public class Blog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt DESC")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
    private List<BlogLike> blogLikeList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;


    public Blog(BlogRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = user.getUsername();
        this.user = user;
    }

    public void update(BlogRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}