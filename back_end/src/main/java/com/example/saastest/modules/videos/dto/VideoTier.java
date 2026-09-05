package com.example.saastest.modules.videos.dto;

import java.util.List;

public record VideoTier(
        String id,
        String label,
        Integer rank,
        List<VideoTopic> topics
) {
}
