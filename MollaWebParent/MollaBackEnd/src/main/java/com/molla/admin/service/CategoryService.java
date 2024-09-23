package com.molla.admin.service;

import com.molla.admin.repository.CategoryRepository;
import com.molla.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository repo;
    public List<Category> listAll(){

    List<Category> rootCategories = repo.findRootCategories();
    return listHierarchicalCategories(rootCategories);
    }
    private List<Category> listHierarchicalCategories(List<Category> rootCategories){
        List<Category> hierarchicalCategories = new ArrayList<>();
        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = rootCategory.getChildren();
            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.coppyFull(subCategory,name));
            }

        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(){

    }
    public  Category save (Category category) {
        return repo.save(category);

    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = repo.findAll();
        for (Category category : categoriesInDB) {
            if(category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));
                Set<Category> children = category.getChildren();
                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
                    listChildren(categoriesUsedInForm, subCategory, 1);

                }
            }
        }

        return categoriesUsedInForm;
    }
    private void listChildren(List<Category> categoriesUsedInForm ,Category parent, int sublevel) {
        int newSublevel = sublevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children ){
            String name  = "";
            for (int i = 0 ; i < newSublevel; i ++) {
                name += "--";
            }
            name += subCategory.getName();
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
            listChildren(categoriesUsedInForm, subCategory, newSublevel);
        }
    }
}