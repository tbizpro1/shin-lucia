package com.shin.lucia.repository;

import com.shin.lucia.entity.LuciaIdea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LuciaIdeaRepository extends JpaRepository<LuciaIdea, Long> {

    List<LuciaIdea> findByCompanyId(Long companyId);
}
