package com.personal.app.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "missives")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Missive {

     @Value("${missive.default.sort.by}")
     public static String DEFAULT_SORT_BY;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY)
     private User sender;

     @ManyToOne(fetch = FetchType.LAZY)
     private Chat chat;

     @Column(name = "created_at")
     private LocalDateTime createdAt;

     @Column(name = "updated_at")
     private LocalDateTime updatedAt;

     @Column(name = "deleted_at")
     private LocalDateTime deletedAt;

     @Column(name = "sent_at")
     private LocalDateTime sentAt;

     @Column(name = "read_at")
     private LocalDateTime readAt;

     @Column(name = "text")
     @Lob
     private String text;

     @Column(name = "is_read")
     private boolean read;



}
