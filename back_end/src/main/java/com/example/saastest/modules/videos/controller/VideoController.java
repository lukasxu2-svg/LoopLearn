package com.example.saastest.modules.videos.controller;

import com.example.saastest.modules.videos.dto.response.GetVideosResponse;
import com.example.saastest.modules.videos.service.VideoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService service;

    public VideoController(VideoService service) {
        this.service = service;
    }

    @GetMapping
    public GetVideosResponse getVideos(Authentication authentication) {
        Long benutzerId = Long.valueOf(authentication.getName());

        return service.getVideos(benutzerId);
    }
}
