package vn.codegym.freelanceworkhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.codegym.freelanceworkhub.dto.UserRegistrationDto;
import vn.codegym.freelanceworkhub.service.UserService;

import javax.validation.Valid;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String showFormLogin() {
        return "login";
    }

    @GetMapping("register")
    public String showFormRegister(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("register")
    public String processRegister(@ModelAttribute("user") @Valid UserRegistrationDto registrationDto, Model model, BindingResult bindingResult) {
        if (userService.findByUsername(registrationDto.getUsername()) != null) {
            bindingResult.rejectValue("username", null,"username.exists");
        }
        if(!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword",null, "Passwords do not match");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.save(registrationDto);
        return "redirect:/register?success";
    }


}
