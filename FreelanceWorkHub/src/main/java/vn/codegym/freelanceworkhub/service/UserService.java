package vn.codegym.freelanceworkhub.service;

import vn.codegym.freelanceworkhub.dto.UserRegistrationDto;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.entity.FreelancerProfile;
import vn.codegym.freelanceworkhub.entity.EmployerProfile;
import vn.codegym.freelanceworkhub.repository.UserRepository;
import vn.codegym.freelanceworkhub.repository.FreelancerProfileRepository;
import vn.codegym.freelanceworkhub.repository.EmployerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FreelancerProfileRepository freelancerProfileRepository, EmployerProfileRepository employerProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.freelancerProfileRepository = freelancerProfileRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    public User save(UserRegistrationDto registrationDto) {
        User user = new User(
                registrationDto.getEmail(), // Using email as username
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getFullName(),
                registrationDto.getEmail(),
                registrationDto.getRole(),
                true // enabled by default
        );
        // For simplicity, using email as username for now
        user.setUsername(registrationDto.getEmail());
        User savedUser = userRepository.save(user);

        if ("FREELANCER".equals(registrationDto.getRole())) {
            FreelancerProfile profile = new FreelancerProfile();
            profile.setUser(savedUser);
            freelancerProfileRepository.save(profile);
        } else if ("EMPLOYER".equals(registrationDto.getRole())) {
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

    public long countNewUsersToday() {
        LocalDate today = LocalDate.now();
        Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return userRepository.countByCreatedAtAfter(startOfDay);
    }

    public List<User> findLatestSignups() {
        // For now, returning all users, but ideally would query by creation date and limit
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String role) {
        return Arrays.stream(role.split(","))
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.trim().toUpperCase()))
                .collect(Collectors.toList());
    }

    public FreelancerProfile findFreelancerProfileByUser(User user) {
        return freelancerProfileRepository.findByUser(user);
    }

    public EmployerProfile findEmployerProfileByUser(User user) {
        return employerProfileRepository.findByUser(user);
    }

    public FreelancerProfile saveFreelancerProfile(FreelancerProfile profile) {
        return freelancerProfileRepository.save(profile);
    }

    public EmployerProfile saveEmployerProfile(EmployerProfile profile) {
        return employerProfileRepository.save(profile);
    }
}
