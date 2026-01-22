package com.nagarro.rbacdemo.controller;

import com.nagarro.rbacdemo.dto.ApiResponse;
import com.nagarro.rbacdemo.dto.ResetPasswordRequest;
import com.nagarro.rbacdemo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {

        Object loginError = session.getAttribute("LOGIN_ERROR");
        Object logoutSuccess = session.getAttribute("LOGOUT_SUCCESS");

        if (loginError != null) {
            model.addAttribute("loginError", true);
            session.removeAttribute("LOGIN_ERROR");
        }

        if (logoutSuccess != null) {
            model.addAttribute("logoutSuccess", true);
            session.removeAttribute("LOGOUT_SUCCESS");
        }

        return "login";
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Password reset successful"));
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

}