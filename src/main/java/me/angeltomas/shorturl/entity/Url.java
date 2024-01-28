package me.angeltomas.shorturl.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.angeltomas.shorturl.entity.user.User;

import java.time.LocalDate;

@Entity
@Table(name = "url")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String completeUrl;

    private String shortUrl;

    private String name;

    private String description;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

