package com.example.saastest.modules.videos.dto.response;

import com.example.saastest.modules.videos.dto.VideoCategory;

import java.util.List;

public record GetVideosResponse(
        List<VideoCategory> videoCatalog
) {
}
