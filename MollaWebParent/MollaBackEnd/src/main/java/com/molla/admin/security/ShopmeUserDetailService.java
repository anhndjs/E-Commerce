package com.molla.admin.security;

import com.molla.admin.repository.UserRepository;
import com.molla.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ShopmeUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user != null ) {
            return  new ShopmeUserDetails(user);

        }
        throw new UsernameNotFoundException("could not find user with email " + email);
    }
}
