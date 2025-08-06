package vn.codegym.freelanceworkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.codegym.freelanceworkhub.dto.UserRegistrationDto;
import vn.codegym.freelanceworkhub.model.EmployerProfile;
import vn.codegym.freelanceworkhub.model.FreelancerProfile;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.repository.EmployerProfileRepository;
import vn.codegym.freelanceworkhub.repository.FreelancerProfileRepository;
import vn.codegym.freelanceworkhub.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FreelancerProfileRepository freelancerProfileRepository, EmployerProfileRepository employerProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.freelancerProfileRepository = freelancerProfileRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    public User save (UserRegistrationDto registrationDto) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setFullName(registrationDto.getFullName());
        user.setRole(registrationDto.getRole());
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        if ("FREELANCER".equals(registrationDto.getRole())) {
            FreelancerProfile profile = new FreelancerProfile();
            profile.setUser(savedUser);
            freelancerProfileRepository.save(profile);
        }else if ("EMPLOYER".equals(registrationDto.getRole())) {
            EmployerProfile profile = new EmployerProfile();
            profile.setUser(savedUser);
            employerProfileRepository.save(profile);
        }
        return savedUser;
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public List<User> findLatestSignUp(){
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");

        }
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,true,true,
                Collections.singleton(authority)
        );

    }
    public FreelancerProfile findFreelancerProfileByUser(User user) {
        return freelancerProfileRepository.findByUser(user);
    }
    public EmployerProfile findEmployerProfileByUser(User user) {
        return employerProfileRepository.findByUser(user);
    }
    public FreelancerProfile saveFreelancerProfile(FreelancerProfile freelancerProfile) {
        return freelancerProfileRepository.save(freelancerProfile);
    }
    public EmployerProfile saveEmployerProfile(EmployerProfile employerProfile) {
        return employerProfileRepository.save(employerProfile);
    }

}
