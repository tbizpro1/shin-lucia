package com.shin.lucia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "lucia_response")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double relatedStep;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String urlHistory;

    private String objectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id")
    private LuciaIdea idea;
}
