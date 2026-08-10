package com.example.saastest.modules.subscription.repository;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByBenutzer_Id(Long id);

    Optional<Subscription> findBySubscriptionId(String id);

    Optional<Subscription> findByBenutzer_IdAndSubStatus(Long id, SubscriptionStatusDto subStatus);
}
