package com.home.sarafan.controllers;

import com.home.sarafan.domain.User;
import com.home.sarafan.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private static final String PROFILE = "profile";
    private static final String MESSAGES = "messages";

    private final MessageRepository messageRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) {
        Map<Object, Object> data = new HashMap<>();

        if (user != null) {
            data.put(PROFILE, user);
            data.put(MESSAGES, messageRepository.findAll());
        }

        model.addAttribute("frontEndData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }
}
