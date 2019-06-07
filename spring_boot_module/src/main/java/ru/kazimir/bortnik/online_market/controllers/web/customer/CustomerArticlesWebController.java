package ru.kazimir.bortnik.online_market.controllers.web.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.controllers.web.admin.AdminUsersWebController;
import ru.kazimir.bortnik.online_market.service.ArticleService;
import ru.kazimir.bortnik.online_market.service.ThemeService;
import ru.kazimir.bortnik.online_market.service.exception.ArticleServiceException;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.CommentDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDetail;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterNewsPage;

import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ADD_COMMENT_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ARTICLES_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ARTICLES_SHOWING_MORE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ARTICLES_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ARTICLES_UPDATE_FILTER_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ARTICLES_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ARTICLE_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_REDIRECT_ARTICLES_SHOWING_MORE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_REDIRECT_ARTICLE_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_404;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_422;

@Controller
@RequestMapping(PUBLIC_CUSTOMER_ARTICLES_URL)
public class CustomerArticlesWebController {
    private final static Logger logger = LoggerFactory.getLogger(AdminUsersWebController.class);
    private final ArticleService articleService;
    private final ThemeService themeService;
    private final FilterNewsPage filterNewsPage = new FilterNewsPage(10L, 1L);

    @Autowired
    public CustomerArticlesWebController(ArticleService articleService, ThemeService themeService) {
        this.articleService = articleService;
        this.themeService = themeService;
    }

    @GetMapping(PUBLIC_CUSTOMER_ARTICLES_SHOWING_URL)
    public String showArticles(@RequestParam(defaultValue = "1") Long currentPage, Model mode) {
        PageDTO<ArticleDTO> articleDOS = articleService.getArticles(filterNewsPage, currentPage);
        List<ThemeDTO> themeDTOS = themeService.getThemes();
        mode.addAttribute("ThemesDTO", themeDTOS);
        mode.addAttribute("PageDTO", articleDOS);
        mode.addAttribute("pageable", filterNewsPage);
        logger.info("List of Themes for submission {}.", themeDTOS);
        logger.info("List of Articles for submission {}.", articleDOS.getList());
        return PUBLIC_CUSTOMER_ARTICLES_PAGE;
    }

    @PostMapping(PUBLIC_CUSTOMER_ARTICLES_UPDATE_FILTER_URL)
    public String updateFilter(@RequestParam(defaultValue = "1") Long theme, @RequestParam(defaultValue = "10") Long limitPositions,
                               RedirectAttributes redirectAttributes) {
        filterNewsPage.setTheme(theme);
        filterNewsPage.setLimitPositions(limitPositions);
        redirectAttributes.addFlashAttribute("messageFilter", "Filter settings applied");
        return PUBLIC_CUSTOMER_REDIRECT_ARTICLE_SHOWING_URL;
    }

    @GetMapping(PUBLIC_CUSTOMER_ARTICLES_SHOWING_MORE_URL)
    public String showMoreArticle(@RequestParam(required = false) Long articlesId, Model mode) {
        if (articlesId != null) {
            try {
                ArticleDTO articleDTO = articleService.getById(articlesId);
                logger.info("New to display := {} ", articleDTO);
                mode.addAttribute("Article", articleDTO);
            } catch (ArticleServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_ERROR_404;
            }
            return PUBLIC_CUSTOMER_ARTICLE_PAGE;
        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @PostMapping(PUBLIC_CUSTOMER_ADD_COMMENT_URL)
    public String addComment(@RequestParam(required = false) Long articlesId,
                             @RequestParam(required = false) String comment,
                             @AuthenticationPrincipal UserDetail UserDetail,
                             RedirectAttributes redirectAttributes) {
        logger.info("Comment creation request. Id Article:= {}, comment:= {}, User:=",
                articlesId, comment, UserDetail.getUserDTO());

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(comment);
        commentDTO.setArticleDTO(new ArticleDTO(articlesId));
        commentDTO.setUserDTO(UserDetail.getUserDTO());

        articleService.addComment(commentDTO);
        redirectAttributes.addFlashAttribute("message", "Your comment has been successfully added.");
        return String.format(PUBLIC_CUSTOMER_REDIRECT_ARTICLES_SHOWING_MORE_URL, articlesId);
    }
}
