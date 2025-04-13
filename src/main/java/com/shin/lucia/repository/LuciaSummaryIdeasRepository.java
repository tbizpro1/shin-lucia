package com.shin.lucia.repository;

import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.entity.LuciaSummaryIdeas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LuciaSummaryIdeasRepository extends JpaRepository<LuciaSummaryIdeas, Long> {

    Optional<LuciaSummaryIdeas> findByIdea(LuciaIdea idea);
}
