package com.molla.admin.service;

import com.molla.admin.controller.CategoryPageInfo;
import com.molla.admin.repository.CategoryRepository;
import com.molla.common.entity.Category;
import com.molla.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CategoryService {
    public static final int ROOT_CATEGORIES_PER_PAGE = 4;
    @Autowired
    private CategoryRepository repo;
    public  List<Category> listByPage(CategoryPageInfo pageInfo,  int pageNum, String sortDir, String keyword){
        Sort sort = Sort.by("name");
        if(sortDir == null || sortDir.isEmpty()){
            sort = sort.ascending();
        } else if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }
        Pageable pageable =  PageRequest.of(pageNum -1, ROOT_CATEGORIES_PER_PAGE, sort);
        Page<Category> pageCategories = null;
        if(keyword !=null && !keyword.isEmpty()) {
            pageCategories = repo.search(keyword, pageable);
        } else {
            pageCategories = repo.findRootCategories(pageable);
        }


        List<Category> rootCategories = pageCategories.getContent();
        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());
        if(keyword !=null && !keyword.isEmpty()) {
            List<Category> searchResult = pageCategories.getContent();
            for(Category category : searchResult) {
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return searchResult;
        } else {
            return listHierarchicalCategories(rootCategories);
        }
    }
    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicacalCategories = new ArrayList<>();
        for (Category rootCategory : rootCategories)  {
            hierarchicacalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = sortSubCategoeries(rootCategory.getChildren());
            for(Category subCagory : children ) {
                String name = "--" + subCagory.getName();
                hierarchicacalCategories.add(Category.coppyFull(subCagory, name));
                listSubHierarchicalCategries(hierarchicacalCategories, subCagory, 1);
            }
        }
        return hierarchicacalCategories;
    }
    private void listSubHierarchicalCategries(List<Category> hirarchicalCategories, Category parent, int subLevel){
        Set<Category> children = sortSubCategoeries(parent.getChildren());
        int newSubLevel = subLevel + 1;
        for (Category subCategory : children) {
            String name = "";
            for (int i =0 ; i <newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            hirarchicalCategories.add(Category.coppyFull(subCategory, name));
            listSubHierarchicalCategries(hirarchicalCategories, subCategory, newSubLevel);
        }
    }
    public Category save(Category category) {
        return  repo.save(category);
    }
    public List<Category> listCategoriesUsedInForm(){
//        List<Category> categoriesUsedInForm = new ArrayList<>();
//        Iterable<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());
//        for (Category category : categoriesInDB){
//            if(category.getParent() == null) {
//                categoriesUsedInForm.add(Category.copyIdAndName(category));
//                Set<Category> children = sortSubCategoeries(category.getChildren());
//                for (Category subCategory : children) {
//                    String name = "--" + subCategory.getName();
//                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
//                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
//                }
//            }
//        }
//        return categoriesUsedInForm;
        List<Category> categoriesUsedInForm = new ArrayList<>();
        List<Category> categoriesInDB = repo.findRootCategories();
        System.out.println(categoriesInDB);
        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(category);
                listSubCategoriesUsedInForm(categoriesUsedInForm, category, 0);
            }
        }

        return categoriesUsedInForm;
    }
    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int sublevel) {

        int newSubLvel = sublevel + 1;
        Set<Category> children = sortSubCategoeries(parent.getChildren());
        for(Category subCategory : children) {
            String name = "";
            for (int i=0; i< newSubLvel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLvel);
        }
    }
    public Category get(Integer id ) throws  CategoryNotFoundException {
        try{
            return repo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new CategoryNotFoundException("could not find any category with Id" + id);

        }
    }
    public  String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = repo.findByName(name);
        if(isCreatingNew) {
            if(categoryByName != null) {
                return "DuplicateName";
            }else {
                Category categoryByAlias = repo.findByAlias(alias);
                if(categoryByAlias != null ) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicateName";
            }
            Category categoryByAlias = repo.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "DuplicateAlias";
            }
        }
        return "OK";

    }
    private  SortedSet<Category> sortSubCategoeries(Set<Category> children) {
        return sortSubCategoeries(children, "asc");
    }
    private SortedSet<Category> sortSubCategoeries(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                if (sortDir.equals("asc")) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return  o2.getName().compareTo(o1.getName());
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }
    public void updateCategoryEnabledStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id, enabled);
    }
    public void delete (Integer id) throws CategoryNotFoundException {
        Long countByid = repo.countById(id);
        if(countByid == null || countByid ==0) {
            throw new CategoryNotFoundException("could not find any category with ID" + id);

        }
        repo.deleteById(id);
    }
}
