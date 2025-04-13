package com.shin.lucia.repository;

import com.shin.lucia.entity.LuciaResponse;
import com.shin.lucia.entity.LuciaIdea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LuciaResponseRepository extends JpaRepository<LuciaResponse, Long> {

    List<LuciaResponse> findByIdea(LuciaIdea idea);

    Optional<LuciaResponse> findByIdeaAndRelatedStep(LuciaIdea idea, Double relatedStep);

    List<LuciaResponse> findAllByIdea_Id(Long ideaId);

}
