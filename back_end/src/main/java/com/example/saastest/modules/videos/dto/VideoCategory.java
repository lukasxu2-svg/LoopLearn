package com.example.saastest.modules.videos.dto;

import java.util.List;

public record VideoCategory(
        String key,
        String label,
        List<VideoTier> tiers
) {
}
