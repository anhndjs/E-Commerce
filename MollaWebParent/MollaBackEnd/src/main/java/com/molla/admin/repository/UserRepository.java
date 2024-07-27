package com.molla.admin.repository;

import com.molla.common.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    @Query("SELECT u FROM  User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public  Long countById(Integer id);

    @Query("UPDATE User  u set u.enabled = ?2 where  u.id =  ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

}