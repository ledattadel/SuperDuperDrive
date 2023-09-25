package com.udacity.jwdnd.course1.cloudstorage.controller.impl;

import com.udacity.jwdnd.course1.cloudstorage.controller.ILoginController;
import com.udacity.jwdnd.course1.cloudstorage.controller.ISignupController;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController implements ISignupController {

    private final IUserService userService;

    @GetMapping()
    public String showSignupForm() {
        return showSignupView();
    }

    @Override
    public String processSignup(User user, Model model) {
        return null;
    }


    @PostMapping()
    public String processSignup(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {

        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = "The username is not available.";
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
            } else {
                redirectAttributes.addFlashAttribute("signupSuccess", true);
                return "redirect:/login";
            }
        } else {
            model.addAttribute("signupError", signupError);
        }

        return showSignupView();
    }

    private String showSignupView() {
        return "signup";
    }




    private void addModelAttributesAndRedirect(Model model, String errorMessage) {
        if (errorMessage == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", errorMessage);
        }
    }

    private String redirectToLogin() {
        return "redirect:/login";
    }

}


