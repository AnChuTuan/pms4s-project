package com.pms4st.pms.controller; // Use your correct base package

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Marks this as a controller bean
public class HomeController {

    /**
     * Handles GET requests to the root path "/".
     * Always redirects to the main projects listing page "/projects".
     * Spring Security will intercept if user is not logged in and redirect to /login.
     */
    @GetMapping("/")
    public String home() {
        // Redirect to the main projects list page
        return "redirect:/projects";
    }
}