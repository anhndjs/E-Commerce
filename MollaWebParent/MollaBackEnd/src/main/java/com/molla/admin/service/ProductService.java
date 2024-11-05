package com.molla.admin.service;

import com.molla.admin.repository.ProductRepository;
import com.molla.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> listAll() {
        return (List<Product>) repository.findAll();
    }


}
