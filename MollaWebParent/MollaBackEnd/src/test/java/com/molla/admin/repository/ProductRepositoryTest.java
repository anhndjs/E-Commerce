package com.molla.admin.repository;

import com.molla.common.entity.Brand;
import com.molla.common.entity.Category;
import com.molla.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreteProduct() {
        Brand brand = entityManager.find(Brand.class, 9);
        Category category = entityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Samsung Galaxy A312sd");
        product.setAlias("Samsung_galaxy_a2dsfd");
        product.setShortDescription("A good smarphone from samsung2");
        product.setFullDescription("this is a very good smartphone full description2");

        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(456);
        product.setCost(400);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product saveProduct = repository.save(product);
        assertThat(saveProduct).isNotNull();
        assertThat(saveProduct.getId()).isGreaterThan(0);

    }
    @Test
    public void testListAllProducts(){
        Iterable<Product> iterableProducts = repository.findAll();

        iterableProducts.forEach(System.out::println);
    }
    @Test
    public  void testGetProduct(){
        Integer id = 2;
        Product product = repository.findById(id).get();
        System.out.println(product);
        assertThat(product).isNotNull();
    }
    @Test
    public void updateProduct(){
        Integer id = 2 ;
        Product product = repository.findById(id).get();
        product.setPrice(499);
        repository.save(product);
        Product updatedProduct = entityManager.find(Product.class, id);
        assertThat(updatedProduct.getPrice()).isEqualTo(499);
    }

    @Test
    public void testDeleProduct(){
        Integer id = 3;
        repository.deleteById(id);
        Optional<Product> result = repository.findById(id);
        assertThat(!result.isPresent());
    }
}
