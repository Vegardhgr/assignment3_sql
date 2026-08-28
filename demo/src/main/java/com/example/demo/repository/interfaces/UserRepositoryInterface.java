package com.example.demo.repository.interfaces;

import com.example.demo.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryInterface {
    public List<User> getAllUsers();
    public Optional<User> getUserById(int id);
    public List<User> getUsersByName(String name);
    public List<User> getUsersPage(int limit, int offset);

    public boolean addUser(
        String fn,
        String ln,
        String em,
        String pw,
        String st
    );

    public boolean updateUserFirstNameLastName(
        String email,
        String fn,
        String ln
    );
}
