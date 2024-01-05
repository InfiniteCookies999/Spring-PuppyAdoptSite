package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adopt_post")
@Getter
@Setter
public class AdoptPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "puppy_name", nullable = false, length = 100)
    private String puppyName;

    /* This is in months */
    @Column(name = "puppy_age", nullable = false)
    private Integer puppyAge;

    @Column(nullable = false, length = 400)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PuppyBreed breed;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private WebUser user;

}
