package com.example.saastest.modules.videos.service;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import com.example.saastest.modules.videos.dto.VideoCategory;
import com.example.saastest.modules.videos.dto.VideoTier;
import com.example.saastest.modules.videos.dto.VideoTopic;
import com.example.saastest.modules.videos.dto.response.GetVideosResponse;
import com.example.saastest.modules.videos.entity.Video;
import com.example.saastest.modules.videos.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    private final SubscriptionRepository subscriptionRepository;
    private final VideoRepository videoRepository;

    public VideoService(SubscriptionRepository subscriptionRepository, VideoRepository videoRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.videoRepository = videoRepository;
    }

    public GetVideosResponse getVideos(Long benutzerId) {
        Optional<Subscription> activeSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzerId, SubscriptionStatusDto.ACTIVE);

        int rank = 0;

        if (activeSubscription.isPresent()) {
            switch (activeSubscription.get().getPlanType()) {
                case NONE -> rank = 0;
                case FREE -> rank = 1;
                case BASIC -> rank = 2;
                case PREMIUM -> rank = 3;
                default -> rank = 0;
            }
        }


        List<Video> videos = videoRepository.findByRankLessThanEqual(rank);
        List<VideoCategory> videoCatalog = new ArrayList<>();

        System.out.println(videos);
        for (Video video : videos) {
            String categoryKey = video.getLanguageType().name().toLowerCase();
            VideoCategory category = findCategory(videoCatalog, categoryKey);

            if (category == null) {
                category = new VideoCategory(
                        categoryKey,
                        video.getLanguageType().toString(),
                        new ArrayList<>());
                videoCatalog.add(category);
            }

            VideoTier tier = findTier(category.tiers(), video.getRank());
            if (tier == null) {
                tier = new VideoTier(
                        getTierId(video.getRank()),
                        getTierLabel(video.getRank()),
                        video.getRank(),
                        new ArrayList<>());
                category.tiers().add(tier);
            }

            tier.topics().add(new VideoTopic(video));
        }

        return new GetVideosResponse(videoCatalog);
    }

    private VideoCategory findCategory(List<VideoCategory> categories, String key) {
        for (VideoCategory category : categories) {
            if (category.key().equals(key)) {
                return category;
            }
        }
        return null;
    }

    private VideoTier findTier(List<VideoTier> tiers, Integer rank) {
        for (VideoTier tier : tiers) {
            if (tier.rank().equals(rank)) {
                return tier;
            }
        }
        return null;
    }

    private String getTierId(Integer rank) {
        return switch (rank) {
            case 1 -> "basic";
            case 2 -> "advanced";
            case 3 -> "premium";
            default -> "rank-" + rank;
        };
    }

    private String getTierLabel(Integer rank) {
        return switch (rank) {
            case 1 -> "Free";
            case 2 -> "Basic";
            case 3 -> "Premium";
            default -> "Rank " + rank;
        };
    }
}
