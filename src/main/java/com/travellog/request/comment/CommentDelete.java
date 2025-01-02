package com.travellog.request.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDelete {

    private String password;

    public CommentDelete(String password) {
        this.password = password;
    }
}
