package com.example.week2.dto;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BlogListResponseDto extends ResponseDto {
    List<BlogToDto> blogList = new ArrayList<>();

    public BlogListResponseDto(StatusEnum status){        // ListResponse return시 msg + http Status Code 같이 반환
        super(status);
    }

    public void add(BlogToDto blogToDto){

        blogList.add(blogToDto);
    }

}
