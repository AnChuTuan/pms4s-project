package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private final Map<Long, User> userMap = new HashMap<>();
    private long nextId = 1;

    public User create(User user) {
        user.setId(nextId++);
        userMap.put(user.getId(), user);
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    public void delete(Long id) {
        userMap.remove(id);
    }
}