package com.molla.admin.repository;

import com.molla.admin.service.CategoryService;
import com.molla.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
    @MockBean
    public  CategoryRepository repo;
    @InjectMocks
    private CategoryService service;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName(){
        Integer id = null;
        String name = "Computers";
        String alias = "abc";
        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(category);
        String result = service.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateName");
    }

}
