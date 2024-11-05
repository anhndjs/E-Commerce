package com.molla.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter
@Setter
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(length = 123, nullable = false, unique = true)
    private String name;
    @Column(length = 123, nullable = false, unique = true)
    private String alias;
    @Column(length = 123, nullable = false)
    private String image;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    @ToString.Exclude
    private Set<Category> children = new HashSet<>();

    @Transient
    private boolean hasChildren;
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
        coppyCategory.setHasChildren(category.getChildren().size() > 0);
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

    @Transient
    public String parentBreadcrumb(){
        Category parent = this.getParent();
        if(parent == null) return "";

        String parentName = parent.getName();
        String parentPath = parent.parentBreadcrumb();

        if (parentPath.isEmpty()) return parentName;

        return parentPath + "/" + parentName;
    }
    @Transient
    public  String getImagePath(){
        if(this.id == null) return "/images/image-thumbnail.png";
    return "/category-images/" + this.id + "/" + this.image;
    }
    @Override
    public String toString() {
        return this.name;
    }

}
