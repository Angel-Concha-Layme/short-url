package dev.shorturl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Link {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String url;
  private String slug;
  private String description;
  private Integer clicks;
  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;

  @ManyToMany
  @JoinTable(
      name = "link_tags",
      joinColumns = @JoinColumn(name = "link_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags;

  @ManyToOne
  @JoinColumn(name = "app_user_id")
  private AppUser appUser;
}