//package com.molla.admin.repository;
//
//import com.molla.admin.repository.RoleRepository;
//import com.molla.common.entity.Role;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false)
//public class RoleRepositoryTests {
//    @Autowired
//    private RoleRepository repo;
//
//    @Test
//    public  void testCreateFirstRole(){
//        Role roleAdmin = new Role("Admin", "manage everything");
//        Role saveRole = repo.save(roleAdmin);
//        assertThat(saveRole.getId()).isGreaterThan(0);
//    }
//    @Test
//    public void testCreateRestRole(){
//        Role roleSalesperson = new Role("Salesperson", "Mannage product price" + "customers, shopping, oders and sales report ");
//
//        Role roleEditor = new Role("Editor", "manage categories, brands, " + "products, articles and menus");
//
//        Role roleShipper = new Role("shipper", "view products, view orders" + "and update order status");
//
//        Role roleAssistant = new Role("assistant", "manage questions and reviews");
//
//        repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
//    }
//}
