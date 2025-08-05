package vn.codegym.freelanceworkhub.controller;

import vn.codegym.freelanceworkhub.dto.UserRegistrationDto;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
            BindingResult result,
            Model model) {

        // Kiểm tra email đã tồn tại
        User existingUser = userService.findByEmail(registrationDto.getEmail());
        if (existingUser != null) {
            result.rejectValue("email", null, "Địa chỉ email này đã được đăng ký.");
        }

        // Kiểm tra mật khẩu nhập lại có khớp không
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Mật khẩu nhập lại không khớp.");
        }

        // Nếu có lỗi thì quay lại form đăng ký
        if (result.hasErrors()) {
            return "register";
        }

        // Lưu người dùng mới
        userService.save(registrationDto);

        // Chuyển hướng đến trang đăng nhập với thông báo thành công (tùy chỉnh sau)
        return "redirect:/login?registered";
    }
}
