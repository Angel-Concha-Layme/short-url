package me.angeltomas.shorturl.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quick_urls")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuickUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String completeUrl;
    private String shortUrl;
}
