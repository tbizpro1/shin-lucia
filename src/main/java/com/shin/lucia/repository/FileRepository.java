package com.shin.lucia.repository;

import com.shin.lucia.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByIdeaId(Long ideaId);

    List<File> findAllByUserId(Long userId);

    List<File> findAllByCompanyId(Long companyId);




}
