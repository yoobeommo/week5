package com.example.week2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDto {
    private String msg;
    private int statusCode;

    public ResponseDto(StatusEnum status){
        this.msg = status.msg;
        this.statusCode = status.statusCode;
    }


}
