package com.example.flight.booking;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.flight.booking.config.RedisConfig;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames = RedisConfig.USER_CACHE, key = "#id")
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    @CachePut(cacheNames = RedisConfig.USER_CACHE, key = "#result.id")
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
