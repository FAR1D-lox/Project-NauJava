package ru.taras_buriachenko.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;

@Controller
@RequestMapping("/custom/users/view")
public class UserControllerView {

    private final UserRepository userRepository;

    public UserControllerView(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/list")
    public String userListView(Model model) {
        Iterable<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "userList";
    }
}
