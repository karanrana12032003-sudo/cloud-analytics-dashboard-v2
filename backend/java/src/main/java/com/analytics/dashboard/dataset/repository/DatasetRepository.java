package com.analytics.dashboard.dataset.repository;

import com.analytics.dashboard.dataset.model.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    List<Dataset> findByUserId(Long userId);
}