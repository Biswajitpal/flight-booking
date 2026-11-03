package com.example.flight.booking;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.flight.booking.config.RedisConfig;
import com.example.flight.booking.dto.UserRequest;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames = RedisConfig.USER_CACHE, key = "#id")
    public User getUser(Long id) {
        return findUser(id);
    }

    @CachePut(cacheNames = RedisConfig.USER_CACHE, key = "#result.id")
    public User createUser(UserRequest request) {
        return userRepository.save(new User(request.getName(), request.getEmail()));
    }

    @CachePut(cacheNames = RedisConfig.USER_CACHE, key = "#id")
    public User updateUser(Long id, UserRequest request) {
        User user = findUser(id);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    @CacheEvict(cacheNames = RedisConfig.USER_CACHE, key = "#id")
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }
}
