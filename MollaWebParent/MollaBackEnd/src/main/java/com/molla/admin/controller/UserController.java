package com.molla.admin.controller;


import com.molla.admin.error.UserNotFoundException;
import com.molla.admin.service.UserService;
import com.molla.admin.util.FileUploadUtil;
import com.molla.common.entity.Role;
import com.molla.common.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String listFirstPage(Model model) {

        return listByPage(1, model, "firstName", "asc");
    }
    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir) {
        System.out.println("sourt field " + sortField);
        System.out.println("sort order" + sortDir);
        Page<User> page = service.listByPage(pageNum, sortField, sortDir);
        List<User> listUsers = page.getContent();
        long startCount = (long) (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount  = startCount + UserService.USERS_PER_PAGE -1;
        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPage", page.getTotalPages());

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);


        return "users/users";

    }
    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> listRoles = service.listRoles();

        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "users/users_form";
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhoto(fileName);
            User saveUser = service.save(user);

            String uploadDir = "user-photos/" + saveUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else  {
            if (user.getPhoto().isEmpty()) user.setPhoto(null);
            service.save(user);
        }


        redirectAttributes.addFlashAttribute("message", "the user has been saved succesfully");
//         service.save(user);
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,Model model, RedirectAttributes redirectAttributes) {
        try {
            List<Role> listRoles = service.listRoles();

            User user = service.get(id);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute(user);
            model.addAttribute("pageTitle", "Edit User (ID: " + id +")");
            return "users/users_form";

        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }


    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The User Id" + id + "has been deleted successfully");

        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

        }
        return "redirect:/users";
    }
    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        service.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user Id" + id + "has been" + status;
        redirectAttributes.addFlashAttribute("message", message);
        return  "redirect:/users";
    }
}