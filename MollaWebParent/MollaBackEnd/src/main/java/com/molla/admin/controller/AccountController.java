//package com.molla.admin.controller;
//
//import com.molla.admin.security.ShopmeUserDetails;
//import com.molla.admin.service.UserService;
//import com.molla.admin.util.FileUploadUtil;
//import com.molla.common.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.io.IOException;
//import java.util.Objects;
//
//import static com.molla.admin.controller.UserController.getRedirectURLtoAffectedUser;
//
//@Controller
//public class AccountController {
//    @Autowired
//    private UserService userService;
//    @GetMapping("/account")
//    public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model){
//        String email = loggedUser.getUsername();
//        User user = userService.getByEmail(email);
//        model.addAttribute("user", user);
//        return "users/account_form";
//
//    }
//    @PostMapping("/account/update")
//    public String updateUser(User user, RedirectAttributes redirectAttributes,
//                             @AuthenticationPrincipal ShopmeUserDetails loggedUser,
//                             @RequestParam("image") MultipartFile multipartFile) throws IOException {
//
//        if (!multipartFile.isEmpty()) {
//            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//            user.setPhoto(fileName);
//            User saveUser = userService.updateAccount(user);
//
//            String uploadDir = "user-photos/" + saveUser.getId();
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//        } else  {
//            if (user.getPhoto().isEmpty()) user.setPhoto(null);
//            userService.updateAccount(user);
//        }
//        loggedUser.setFirstName(user.getFirstName());
//        loggedUser.setLastName(user.getLastName());
//
//
//        redirectAttributes.addFlashAttribute("message", "your account details have been updated");
//         service.save(user);
//        return "redirect:/account";
//    }
//}
