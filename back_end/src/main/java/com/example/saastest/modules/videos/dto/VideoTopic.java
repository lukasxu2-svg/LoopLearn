package com.example.saastest.modules.videos.dto;

import com.example.saastest.modules.videos.entity.Video;

public record VideoTopic(
        String id,
        String title,
        String duration,
        String src,
        String description
) {
    public VideoTopic(Video video) {
        this(video.getTitle().toLowerCase(), video.getTitle(), video.getDuration(), video.getVideoPath(), video.getDescription());
    }
}
