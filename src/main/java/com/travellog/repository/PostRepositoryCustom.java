package com.travellog.repository;

import com.travellog.domain.Post;
import com.travellog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);


}
