package com.molla.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
@NoArgsConstructor
@Getter
@Setter
public class Brand {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Integer id;
    @Column(nullable = false, length = 45, unique = true)
    private  String name;

    @Column(nullable = false, length = 128)
    private String logo;

    @ManyToMany
    @JoinTable(name= "brands_categories",
    joinColumns = @JoinColumn(name="brand_id"),
            inverseJoinColumns = @JoinColumn(name= "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Brand(String name) {
        this.name = name;
        this.logo = name;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", categorySet=" + categories +
                '}';
    }
    @Transient
    public String getLogoPath(){
        if(this.id == null || this.logo == null) return "/images/image-thumbnail.png";
        return "/brand-images/" + this.id + "/" + this.logo;
    }
}
