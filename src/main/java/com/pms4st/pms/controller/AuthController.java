package com.pms4st.pms.controller;

import com.pms4st.pms.entity.User;
import com.pms4st.pms.service.AppService; // Use combined service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired private AppService appService;

    @GetMapping("/login") public String showLoginPage() { return "login"; }

    @GetMapping("/register") public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Use User entity for form
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, BindingResult result,
                                     Model model, RedirectAttributes redirectAttributes) {
        // Basic Manual Validation
        boolean hasErrors = false;
        if (user.getUsername() == null || user.getUsername().length() < 3) {
             model.addAttribute("usernameError", "Username must be at least 3 characters."); hasErrors = true;
        } else if (appService.findUserByUsername(user.getUsername()).isPresent()) {
             model.addAttribute("usernameError", "Username already taken."); hasErrors = true;
        }
        // Add similar basic checks for email (format, existence) and password length

        if (hasErrors) {
            return "register"; // Show form again with inline errors (need to add in template)
        }

        try {
            appService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getFullName());
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("registrationError", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}