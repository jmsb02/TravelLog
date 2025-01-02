package com.travellog.repository.post;

import com.travellog.domain.Post;
import com.travellog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);


}
