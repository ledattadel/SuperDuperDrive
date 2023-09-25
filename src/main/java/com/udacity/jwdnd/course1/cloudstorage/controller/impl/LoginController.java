package com.udacity.jwdnd.course1.cloudstorage.controller.impl;

import com.udacity.jwdnd.course1.cloudstorage.controller.ILoginController;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController implements ILoginController {

    @GetMapping()
    public String showLoginForm(@ModelAttribute("signupSuccess") String signupSuccess, Model model) {
        if ("true".equals(signupSuccess)) {
            model.addAttribute("signupSuccess", true);
        }

        return "login";
    }
}
