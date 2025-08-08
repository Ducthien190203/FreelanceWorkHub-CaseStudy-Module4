package vn.codegym.freelanceworkhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.codegym.freelanceworkhub.model.JobCategory;
import vn.codegym.freelanceworkhub.repository.JobCategoryRepository;

import javax.persistence.Column;
import java.util.Optional;

@Controller
@RequestMapping("/jobCategory")
public class JobCategoryController {
    @Autowired
    private JobCategoryRepository categoryRepository;

    // Hiển thị danh sách
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "category/list"; // View: category/list.html hoặc .jsp
    }

    // Hiển thị form thêm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new JobCategory());
        return "category/form"; // View: form nhập tên category
    }

    // Lưu mới hoặc cập nhật
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") JobCategory category) {
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    // Hiển thị form cập nhật
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<JobCategory> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "category/form";
        } else {
            return "redirect:/categories";
        }
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }
}
