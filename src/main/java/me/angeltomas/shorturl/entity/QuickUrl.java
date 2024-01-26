package me.angeltomas.shorturl.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quick_url")
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
