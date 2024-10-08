package com.molla.admin.service;

import com.molla.admin.error.UserNotFoundException;
import com.molla.admin.repository.RoleRepository;
import com.molla.admin.repository.UserRepository;
import com.molla.common.entity.Role;
import com.molla.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    public static final int USERS_PER_PAGE = 4;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
        System.out.println("sfs" + keyword);
        if(keyword != null) {
            System.out.println(keyword + pageable);
            return userRepo.findAll(keyword, pageable);
        }
        System.out.println("search"+ pageable);
        return userRepo.findAll(pageable);
    }
    public  User getByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }
    public List<User> listAll(){
        return (List<User>) userRepo.findAll();
     }
    public  List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser) {
            User existingUser = userRepo.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                enCodePassword(user);
            }
        }else {
            enCodePassword(user);
        }

     return  userRepo.save(user);
    }
    public  User updateAccount(User userInform) {
        User userInDB = userRepo.findById(userInform.getId()).get();
        if (!userInform.getPassword().isEmpty()){
            userInDB.setPassword(userInform.getPassword());
            enCodePassword(userInDB);
        }
        if (userInform.getPhoto() != null) {
            userInDB.setPhoto(userInform.getPhoto());
        }
        userInDB.setFirstName(userInform.getFirstName());
        userInDB.setLastName(userInform.getLastName());
        return  userRepo.save(userInDB);
    }
    private void enCodePassword(User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }
    public boolean isEmailUnique(Integer id, String email ) {
        User userByEmail = userRepo.getUserByEmail(email);
        if (userByEmail == null ) return true;
        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            if (userByEmail != null) return false;
        } else {
            if (userByEmail.getId() != id) {
                return false;
            }
        }
            return true;

    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("could not find any user with Id" + id);
        }
    }

    public  void delete (Integer id ) throws UserNotFoundException {
        Long countById = userRepo.countById(id);
        if(countById == null  || countById == 0) {
            throw new UserNotFoundException("could not find any user with ID" + id);

        }
        userRepo.deleteById(id);
    }
    public void updateUserEnabledStatus(Integer id, boolean enabled ) {
        userRepo.updateEnabledStatus(id,enabled);
    }
}
