package vn.codegym.freelanceworkhub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.codegym.freelanceworkhub.model.UserRole;

@Controller
public class HomeController {
    @GetMapping
    public String home() {
        return "index";
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(UserRole.ADMIN))){
                return "redirect:/admin/dashboard";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(UserRole.EMPLOYER))) {
                return "redirect:/employer/dashboard";

            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(UserRole.FREELANCER))) {
                return "redirect:/freelancer/dashboard";
            }
        }return "redirect:/";
    }
}
