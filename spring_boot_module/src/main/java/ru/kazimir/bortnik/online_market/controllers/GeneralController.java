package ru.kazimir.bortnik.online_market.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static ru.kazimir.bortnik.online_market.constant.URLConstants.*;

@Controller
public class GeneralController {

    @GetMapping({HOME_PAGE_URL, DEFAULT_PAGE_URL})
    public String home() {
        return HOME_PAGE;
    }

    @GetMapping(LOGIN_PAGE_URL)
    public String login() {
        return LOGIN_PAGE;
    }
}
