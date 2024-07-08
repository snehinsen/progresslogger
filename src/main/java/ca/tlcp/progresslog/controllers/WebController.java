package ca.tlcp.progresslog.controllers;

import ca.tlcp.progresslog.ServerUtils;
import ca.tlcp.progresslog.entities.messages.Message;
import ca.tlcp.progresslog.entities.messages.MessageRepository;
import ca.tlcp.progresslog.entities.roles.Role;
import ca.tlcp.progresslog.entities.roles.RoleRepository;
import ca.tlcp.progresslog.entities.threads.ThreadRepository;
import ca.tlcp.progresslog.entities.users.UserAccount;
import ca.tlcp.progresslog.entities.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping(path = "settings/update")
    public String updateUser(@RequestParam String email, @RequestParam String name, @RequestParam MultipartFile pfp) {
        UserAccount account = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Generate profile picture location
        String pfpLocation = account.getFullName().replaceAll(" ", "").toLowerCase().trim() + ".png";

        // Save the profile picture
        try {
            if (!pfp.isEmpty()) {
                String pfpSaveLocation = ServerUtils.absolutePFPPath(pfpLocation);
                Files.write(Paths.get(pfpSaveLocation), pfp.getBytes());
                System.out.println(pfpSaveLocation);
                System.out.println(pfpLocation);
                account.setPFPLocation(pfpLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/settings?error=pfp_upload_failed";
        }
        account.setFullName(name);
        userRepository.save(account);
        return "redirect:/settings";
    }


    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(path = "/admin")
    public ModelAndView admin() {
        return new ModelAndView("admin");
    }


    @GetMapping(path = "/app")
    public ModelAndView app(@RequestParam(name = "thread", required = false) String selectedThread) {
        ModelAndView mav = new ModelAndView("app");
        if (selectedThread != null) {
            mav.addObject("selectedThread", selectedThread);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserAccount user;
            Role role;
            String pfp = "https://cdn-icons-png.flaticon.com/512/1144/1144760.png";
            mav.addObject("pfp", pfp);
            Object principal = authentication.getPrincipal();
            String email;
            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else {
                email = "user@example.com";
            }
            System.out.println("Logged in as " + email);
            Optional<UserAccount> optionalUserAccount = userRepository
                    .findByEmail(email);
            if (optionalUserAccount.isPresent()) {
                user = optionalUserAccount.get();
                System.out.println(user);
                Optional<Role> optionalRole = roleRepository.getRoleByRoleName(
                        user.getRole());
                mav.addObject("user", user);
                if (optionalRole.isPresent()) {
                    role = optionalRole.get();
                    mav.addObject("role", role);
                }
            } else {
                mav.addObject("user",
                        new UserAccount()
                                .builder()
                                .email(email)
                                .fullName("System User")
                                .password(new BCryptPasswordEncoder(12).encode("password"))
                                .role("USER")
                                .build());
            }
        } else {
            throw new RuntimeException("No user was retrieved");
        }
        mav.addObject("selectedThread", "New Thread");
        mav.addObject("threadMessages",
                List.of(
                        new Message(1,"message", 5, "Snehin Sen", LocalDateTime.now())
                ));
        mav.addObject("me", "me");
        return mav;
    }

    @GetMapping(path = "/settings")
    public ModelAndView settings() {
        ModelAndView mav = new ModelAndView("settings");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserAccount user;
            Role role;
            String pfp = "https://cdn-icons-png.flaticon.com/512/1144/1144760.png";
            Object principal = authentication.getPrincipal();
            String email;
            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else {
                email = "user@example.com";
            }
            Optional<UserAccount> optionalUserAccount = userRepository
                    .findByEmail(email);
            if (optionalUserAccount.isPresent()) {
                user = optionalUserAccount.get();
                mav.addObject("user", user);
                mav.addObject("pfp", pfp);
            } else {
                mav.addObject("user",
                        new UserAccount()
                                .builder()
                                .email(email)
                                .fullName("System User")
                                .password(new BCryptPasswordEncoder(12).encode("password"))
                                .role("USER")
                                .build());
            }
        } else {
            throw new RuntimeException("No user was retrieved");
        }
        return mav;
    }

    @GetMapping(path = "/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping(path = "/signup")
    public ModelAndView signup() {
        return new ModelAndView("signup");
    }

    @PostMapping(path = "/register")
    public String addUser(
            @RequestParam(name = "accountName") String name,
            @RequestParam String email,
            @RequestParam String password
    ) {
        if (userRepository.findByEmail(email).isEmpty()) {
            System.out.println("Adding User " + name);
            UserAccount userAccount = new UserAccount();
            userAccount.setEmail(email);
            userAccount.setFullName(name);
            userAccount.setPassword(
                    new BCryptPasswordEncoder(12)
                            .encode(password)
            );
            userAccount.setRole("USER");
            userRepository.save(userAccount);
            return "redirect:/login";
        } else {
            return "redirect:/signup?err";
        }
    }


}
