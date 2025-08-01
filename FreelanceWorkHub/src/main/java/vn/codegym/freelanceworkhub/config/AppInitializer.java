package vn.codegym.freelanceworkhub.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Khởi tạo các cấu hình "root" như AppConfig (DB, Hibernate...)
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                AppConfig.class,
                SecurityConfig.class
        };
    }

    // Khởi tạo cấu hình Web MVC
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebMvcConfig.class
        };
    }

    // Cấu hình DispatcherServlet mapping
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
