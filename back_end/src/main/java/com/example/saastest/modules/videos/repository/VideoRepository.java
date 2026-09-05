package com.example.saastest.modules.videos.repository;

import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.videos.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByRankLessThanEqual(int rank);
}
