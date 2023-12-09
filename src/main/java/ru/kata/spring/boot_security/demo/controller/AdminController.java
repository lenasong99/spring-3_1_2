package ru.kata.spring.boot_security.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("")
    public String showAllUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "all-users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        User user = new User();
        ArrayList<Role> role = new ArrayList<>();
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        return "user-create";
    }

   @PostMapping("/new")
    public String newUser(@ModelAttribute("user") User user, @RequestParam(value = "role") String[] roles) {
       user.setRoles(getRoles(roles));
       userService.saveUser(user);
       return "redirect:/admin";
   }
    public Set<Role> getRoles(String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleRepository.findByName(role));
        }
        return roleSet;
    }

    @GetMapping("/edit-user")
    public String updateUserForm(@RequestParam("id") Integer id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/edit-user";
    }
    @PostMapping("/edit-user")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "role") String[] roles) {
        user.setRoles(getRoles(roles));
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @RequestMapping("/delete-user")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
