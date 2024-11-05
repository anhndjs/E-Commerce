package com.molla.admin.controller;

import com.molla.admin.service.BrandService;
import com.molla.admin.service.CategoryService;
import com.molla.admin.util.FileUploadUtil;
import com.molla.common.entity.Brand;
import com.molla.common.entity.Category;
import com.molla.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Controller
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private BrandService service;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listAll(Model model){
        List<Brand> listBrands = service.listAll();
        model.addAttribute("listBrands", listBrands);
        return "brands/brands";
    }
    @GetMapping("/new")
    public String newBrand(Model model){
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        System.out.println("************************");
        System.out.println(listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Brand");
        return "brands/brand_form";
    }
    @PostMapping("/categories/save")
    public String saveCategory(Brand brand,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes ra) throws IOException {

      if(!multipartFile.isEmpty()) {
          String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
          brand.setLogo(fileName);

          Brand saveBrand = service.save(brand);
          String  uploadDir = "../brand-logos/" + saveBrand.getId();
          FileUploadUtil.cleanDir(uploadDir);
          FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
      } else {
          service.save(brand);
      }
      ra.addFlashAttribute("message", "the brand has been saved successfully");
        return "redirect:/categories";
    }
    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes ra) {

        LOGGER.info("CategoryController | editCategory is started");

        try {
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            LOGGER.info("CategoryController | editCategory | category : " + category.toString());
            LOGGER.info("CategoryController | editCategory | listCategories : " + listCategories.toString());


            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

            return "categories/category_form";

        } catch (CategoryNotFoundException ex) {

            LOGGER.info("CategoryController | editCategory | messageError : " + ex.getMessage());
            ra.addFlashAttribute("messageError", ex.getMessage());
            return "redirect:/categories";
        }
    }
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        LOGGER.info("CategoryController | deleteCategory is started");

        LOGGER.info("CategoryController | deleteCategory | id : " + id);

        try {
            categoryService.delete(id);

            LOGGER.info("CategoryController | deleteCategory | category deleted");

            String categoryDir = "../category-images/" + id;

            LOGGER.info("CategoryController | deleteCategory | categoryDir : " + categoryDir);

            FileUploadUtil.removeDir(categoryDir);

            LOGGER.info("CategoryController | deleteCategory | FileUploadUtil.removeDir is over");

            LOGGER.info("CategoryController | deleteCategory | categoryDir : " + categoryDir);

            redirectAttributes.addFlashAttribute("messageSuccess",
                    "The category ID " + id + " has been deleted successfully");


        } catch (CategoryNotFoundException ex) {
            LOGGER.info("CategoryController | deleteCategory | messageError : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }

        return "redirect:/categories";
    }

}
