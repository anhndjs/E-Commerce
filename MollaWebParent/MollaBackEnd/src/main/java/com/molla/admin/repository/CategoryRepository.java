package com.molla.admin.repository;

import com.molla.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository <Category, Integer> {
    @Query("select  c from Category c where c.parent.id is Null")
    List<Category> findRootCategories(Sort sort);
    @Query("select c from Category c  where c.parent.id is not Null")
    Page<Category> findRootCategories(Pageable pageable);
    @Query("select  c from Category c where c.name like %?1%")
    Page<Category> search(String keyword, Pageable pageable);
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> findRootCategories();
    Long countById (Integer id);
    Category findByName(String name);
    Category findByAlias(String name);
    @Query("update  Category c SET c.enabled = ?2 where  c.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);
}
