package com.molla.admin.repository;

import com.molla.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("caccc");
        Category  saveCategory = repo.save(category);

        assertThat(saveCategory.getId()).isGreaterThan(0);
    }
    @Test
    public  void testCreateCategory(){
            Category parent = new Category(5);
            Category cameras = new Category("camerassdsf", parent);
            Category smartphones = repo.save(cameras);

            assertThat(smartphones.getId()).isGreaterThan(0);
    }
    @Test
    public void testGetCategory() {
        Category category = repo.findById(1).get();
        System.out.println(category.getName());
        Set<Category> children = category.getChildren();
        for(Category subcategory : children ) {
            System.out.println(subcategory.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }
    @Test
    public void testPrintHierarchicalCategories() {
        Iterable<Category> categories =  repo.findAll();
        for (Category rootCategory : categories) {
            if (rootCategory.getParent() == null ) {
                System.out.println(rootCategory.getName());
                Set<Category> children = rootCategory.getChildren();
                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }
    private void printChildren(Category parent, int sublevel) {
        int newSublevel = sublevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children ){
            for (int i = 0 ; i < newSublevel; i ++) {
                System.out.println("--");
            }
            System.out.println(subCategory.getName());
            printChildren(parent, newSublevel);
        }

    }
    @Test
    public void prinNull(){

    }
}
