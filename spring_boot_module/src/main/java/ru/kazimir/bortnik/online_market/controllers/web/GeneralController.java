package ru.kazimir.bortnik.online_market.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kazimir.bortnik.online_market.service.ArticleService;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterGeneralPage;

import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.DEFAULT_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.HOME_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.HOME_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.LOGIN_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.LOGIN_PAGE_URL;

@Controller
public class GeneralController {
    private final ArticleService articleService;
    private final FilterGeneralPage filterGeneralPage = new FilterGeneralPage();

    @Autowired
    public GeneralController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping({HOME_PAGE_URL, DEFAULT_PAGE_URL})
    public String home(Model mode) {
        List<ArticleDTO> articleDTOS = articleService.getTop(filterGeneralPage.getAmountOfNewsDisplayed());
        mode.addAttribute("Article", articleDTOS);
        return HOME_PAGE;
    }

    @GetMapping(LOGIN_PAGE_URL)
    public String login() {
        return LOGIN_PAGE;
    }
}
