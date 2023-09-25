package com.udacity.jwdnd.course1.cloudstorage.ultis;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class notificationHandler {
    public static void addFlashAttributeMessage(RedirectAttributes redirectAttributes, int cnt, String successAttributeName, String errorAttributeName) {
        String handleMessage = cnt > 0 ? "messageSuccess" : "messageError";
        String message = cnt > 0 ? successAttributeName : errorAttributeName;
        redirectAttributes.addFlashAttribute(handleMessage, message);

    }
}

