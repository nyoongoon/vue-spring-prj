package com.nyoongoon.fullstackjava.repository;

import com.nyoongoon.fullstackjava.domain.Post;
import com.nyoongoon.fullstackjava.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
