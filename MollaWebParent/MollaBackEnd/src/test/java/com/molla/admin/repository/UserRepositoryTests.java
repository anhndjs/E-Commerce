package com.molla.admin.repository;

import com.molla.common.entity.Role;
import com.molla.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;
    @Test
    public  void testCreateUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class,1);
        User userAnh = new User("anh@gmail.com","2002","Nam","ninh thuan");
        userAnh.addRole(roleAdmin);

        User savedUser = repo.save(userAnh);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userRavi = new User("ravi@gmail.com","ravi2020", "ravi", "kumar");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role (5);
        userRavi.addRole(roleEditor);
        userRavi.addRole(roleAssistant);
        User savedUser = repo.save(userRavi);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void testListAllUsers(){
        Iterable<User> listUser = repo.findAll();
        listUser.forEach(user -> System.out.println(user));
    }
    @Test
    public void testGetUserId(){
        User userNam = repo.findById(1).get();
        System.out.println(userNam);
        assertThat(userNam).isNotNull();
    }
    @Test
    public void testUpdateUserDetails(){
        User userNam = repo.findById(1).get();
        userNam.setEnabled(true);
        userNam.setEmail("ducanh01@gmail.com");
        repo.save(userNam);
    }
    @Test
    public void testUpdateUserRoles(){
        User userRavi = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        userRavi.getRoleSet().remove(roleEditor);
        userRavi.addRole(roleSalesperson);
        repo.save(userRavi);
    }
    @Test
    public  void testDeleteUser(){
        Integer userID = 2;
        repo.deleteById(userID);
    }
    @Test
    public void testGetUserByEmail(){
        String email = "ravi@gmail.com";
        User user = repo.getUserByEmail(email);
        assertThat(user).isNotNull();

    }
    @Test
    public void testCountById(){
        Integer id = 1;
        Long countById = repo.countById(1);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }
    @Test
    public void testDisableUser(){
        Integer id = 21;
        repo.updateEnabledStatus(id,false);
    }
    @Test
    public void testEnableUser(){
        Integer id = 21;
        repo.updateEnabledStatus(id,true);
    }
    @Test
    public void testListFirstPage() {
        int pageNumber = 1;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user ->  System.out.println(user));
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }
    @Test
    public  void testSearchUsers() {
        String keyword = "bruce";
        int pageSize = 4;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword,pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));
        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
