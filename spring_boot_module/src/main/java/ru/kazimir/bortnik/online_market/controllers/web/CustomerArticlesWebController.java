package ru.kazimir.bortnik.online_market.controllers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.service.ArticleService;
import ru.kazimir.bortnik.online_market.service.exception.ArticleServiceException;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterNewsPage;

import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ARTICLES_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ARTICLES_SHOWING_MORE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ARTICLES_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ARTICLES_UPDATE_FILTER_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ARTICLES_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ARTICLE_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_REDIRECT_ARTICLE_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_404;

@Controller
@RequestMapping(PUBLIC_SALE_ARTICLES_URL)
public class CustomerArticlesWebController {
    private final static Logger logger = LoggerFactory.getLogger(AdminUsersWebController.class);

    private final ArticleService articleService;
    private final FilterNewsPage filterNewsPage = new FilterNewsPage(10L, 1L);
    @Autowired
    public CustomerArticlesWebController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(PUBLIC_SALE_ARTICLES_SHOWING_URL)
    public String showArticles(@RequestParam(defaultValue = "1") Long currentPage, Model mode) {
        PageDTO<ArticleDTO> articleDOS = articleService.getArticles(filterNewsPage, currentPage);
        List<ThemeDTO> themeDTOS = articleService.getThemes();
        mode.addAttribute("ThemesDTO", themeDTOS);
        mode.addAttribute("PageDTO", articleDOS);
        mode.addAttribute("pageable", filterNewsPage);
        logger.info("List of Themes for submission {}.", themeDTOS);
        logger.info("List of Articles for submission {}.", articleDOS.getList());
        return PUBLIC_SALE_ARTICLES_PAGE;
    }

    @PostMapping(PUBLIC_SALE_ARTICLES_UPDATE_FILTER_URL)
    public String updateFilter(@RequestParam(defaultValue = "1") Long theme, @RequestParam(defaultValue = "10") Long limitPositions,
                               RedirectAttributes redirectAttributes) {
        filterNewsPage.setTheme(theme);
        filterNewsPage.setLimitPositions(limitPositions);
        redirectAttributes.addFlashAttribute("messageFilter", "Filter settings applied");
        return PUBLIC_SALE_REDIRECT_ARTICLE_SHOWING_URL;
    }

    @GetMapping(PUBLIC_SALE_ARTICLES_SHOWING_MORE_URL)
    public String showMoreArticle(@RequestParam(required = false) Long articlesId, Model mode) {
        if (articlesId != null) {
            try {
                ArticleDTO articleDTO = articleService.getArticleById(articlesId);
                logger.info("New to display := {} ", articleDTO);
                mode.addAttribute("Article", articleDTO);
            } catch (ArticleServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_ERROR_404;
            }
            return PUBLIC_SALE_ARTICLE_PAGE;
        } else {
            return REDIRECT_ERROR_404;
        }
    }
}
