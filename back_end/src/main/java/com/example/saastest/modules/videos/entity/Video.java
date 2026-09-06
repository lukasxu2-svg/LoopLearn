package com.example.saastest.modules.videos.entity;

import com.example.saastest.modules.videos.dto.enums.LanguageType;
import jakarta.persistence.*;

@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer rank;

    private String title;

    private String videoPath;

    private String description;

    private String duration;

    @Enumerated(EnumType.STRING)
    private LanguageType languageType;

    public Video(Integer rank, String title, String videoPath, LanguageType languageType, String duration, String description) {
        this.rank = rank;
        this.title = title;
        this.videoPath = videoPath;
        this.description = description;
        this.duration = duration;
        this.languageType = languageType;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getTitle() {
        return title;
    }

    public Integer getRank() {
        return rank;
    }
}
