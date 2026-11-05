package ru.taras_buriachenko.NauJava.repository.impl;

import org.springframework.stereotype.Component;
import ru.taras_buriachenko.NauJava.entity.user.User;
import ru.taras_buriachenko.NauJava.repository.CrudRepository;

import java.util.List;

@Component
public class UserRepository implements CrudRepository<User, Long> {

    private final List<User> userContainer;

    public UserRepository(List<User> userContainer) {
        this.userContainer = userContainer;
    }


    @Override
    public void create(User user) {
        userContainer.add(user);
    }

    @Override
    public User read(Long userId) {
        for (User user : userContainer)
            if (user.getId().equals(userId))
                return user;
        return null;
    }

    @Override
    public void update(User user) {
        Long userId = user.getId();
        for (int i = 0; i < userContainer.size(); i++) {
            Long id = userContainer.get(i).getId();
            if (id.equals(userId)) {
                userContainer.set(i, user);
                break;
            }
        }
    }

    @Override
    public void delete(Long userId) {
        for (int i = 0; i < userContainer.size(); i++) {
            Long id = userContainer.get(i).getId();
            if (id.equals(userId)) {
                userContainer.remove(i);
                break;
            }
        }
    }

    public List<User> getAllUsers() {
        return userContainer;
    }
}
