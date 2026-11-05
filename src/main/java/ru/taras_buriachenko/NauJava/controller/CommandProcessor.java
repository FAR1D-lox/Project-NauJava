package ru.taras_buriachenko.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.taras_buriachenko.NauJava.service.UserService;
import ru.taras_buriachenko.NauJava.entity.user.User;

@Component
public class CommandProcessor
{
    private final UserService userService;

    @Autowired
    public CommandProcessor(UserService userService)
    {
        this.userService = userService;
    }

    public void processCommand(String input)
    {
        String[] cmd = input.split(" ");
        switch (cmd[0])
        {
            case "create" ->
            {
                userService.createUser(Long.valueOf(cmd[1]), cmd[2], cmd[3]);
                System.out.println("Пользователь успешно добавлен");
            }
            case "find" ->
            {
                User user = userService.findById(Long.valueOf(cmd[1]));
                System.out.println("Пользователь найден: " + user);
            }
            case "getAllUsers" ->
            {
                System.out.println("Пользователь найден: " + userService.getAllUsers().toString());
            }

            case "update" ->
            {
                userService.updateUser(Long.valueOf(cmd[1]), cmd[2], cmd[3]);
                System.out.println("Пользователь успешно обновлен...");
            }
            case "delete" ->
            {
                userService.deleteById(Long.valueOf(cmd[1]));
                System.out.println("Пользователь успешно удален");
            }
            default -> System.out.println("Введена неизвестная команда");
        }
    }

}

