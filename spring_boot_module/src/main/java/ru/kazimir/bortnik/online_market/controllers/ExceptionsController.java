package ru.kazimir.bortnik.online_market.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static ru.kazimir.bortnik.online_market.constant.URLConstants.*;

@Controller
@RequestMapping(ERROR_URL)
public class ExceptionsController {

    @GetMapping(ERROR_403_PAGE_URL)
    public String error403() {
        return ERROR_403_PAGE;
    }

    @GetMapping(ERROR_500_PAGE_URL)
    public String error500() {
        return ERROR_500_PAGE;
    }

    @GetMapping(ERROR_404_PAGE_URL)
    public String error404() {
        return ERROR_404_PAGE;
    }
}
