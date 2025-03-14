package dev.shorturl.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;
  private String color;

  @ManyToMany(mappedBy = "tags")
  private Set<Link> links;

  @ManyToOne
  @JoinColumn(name = "app_user_id")
  private AppUser appUser;
}
