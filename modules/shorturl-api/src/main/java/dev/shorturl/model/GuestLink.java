package dev.shorturl.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "guest_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestLink {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String url;
  private String slug;
  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;

}
