package ru.kazimir.bortnik.online_market.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_403_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_403_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_404_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_404_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_422_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_422_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_500_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_500_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_URL;

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

    @GetMapping(ERROR_422_PAGE_URL)
    public String error422() {
        return ERROR_422_PAGE;
    }
}
