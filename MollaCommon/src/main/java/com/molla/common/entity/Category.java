package com.molla.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 123, nullable = false, unique = true)
    private String name;
    @Column(length = 123, nullable = false, unique = true)
    private String alias;
    @Column(length = 123, nullable = false)
    private String image;
    private boolean enabled;
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();


    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public Category(Integer id, String name, String alias) {
        super();
        this.id = id;
        this.name = name;
        this.alias = alias;
    }
    public static Category copyFull(Category category) {
        Category coppyCategory = new Category();
        coppyCategory.setId(category.getId());
        coppyCategory.setName(category.getName());
        coppyCategory.setImage(category.getImage());
        coppyCategory.setAlias(category.getAlias());
        coppyCategory.setEnabled(category.isEnabled());
        return coppyCategory;

    }
    public static  Category coppyFull(Category category, String name ) {
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);
        return copyCategory;
    }

    public static Category copyIdAndName(Category category) {
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());

        return copyCategory;
    }

    public static Category copyIdAndName(Integer id, String name) {
        Category copyCategory = new Category();
        copyCategory.setId(id);
        copyCategory.setName(name);

        return copyCategory;
    }


//    public boolean isenabled() {
//        return enabled;
//    }
//
//    public void setenabled(boolean enabel) {
//        this.enabled = enabel;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAlias() {
//        return alias;
//    }
//
//    public void setAlias(String alias) {
//        this.alias = alias;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public Category getParent() {
//        return parent;
//    }
//
//    public void setParent(Category parent) {
//        this.parent = parent;
//    }
//
//    public Set<Category> getChildren() {
//        return children;
//    }
//
//    public void setChildren(Set<Category> children) {
//        this.children = children;
//    }
}