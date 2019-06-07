package ru.kazimir.bortnik.online_market.controllers.web.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
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
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDetail;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterNewsPage;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_GET_UNAUTHORIZED_USER;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.*;

@Controller
@RequestMapping(PUBLIC_SALE_ARTICLES_URL)
public class SaleArticlesWebController {
    private final static Logger logger = LoggerFactory.getLogger(AdminUsersWebController.class);

    private final ArticleService articleService;
    private final ThemeService themeService;
    private final Validator themeValidator;
    private final FilterNewsPage filterNewsPage = new FilterNewsPage(10L, 1L);

    public SaleArticlesWebController(ArticleService articleService,
                                     ThemeService themeService,
                                     @Qualifier("themeValidatorImpl") Validator themeValidator) {
        this.articleService = articleService;
        this.themeService = themeService;
        this.themeValidator = themeValidator;
    }

    @GetMapping(PUBLIC_SALE_ARTICLES_SHOWING_URL)
    public String showArticles(@RequestParam(defaultValue = "1") Long currentPage, Model model) {
        PageDTO<ArticleDTO> articleDOS = articleService.getArticles(filterNewsPage, currentPage);
        List<ThemeDTO> themeDTOS = themeService.getThemes();
        logger.info("List of Themes for submission {}.", themeDTOS);
        logger.info("List of Articles for submission {}.", articleDOS.getList());
        model.addAttribute("ThemesDTO", themeDTOS);
        model.addAttribute("PageDTO", articleDOS);
        model.addAttribute("pageable", filterNewsPage);
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

    @GetMapping(PUBLIC_SALE_ARTICLE_SHOWING_MORE_URL)
    public String showMoreArticle(@RequestParam(required = false) Long articlesId, Model mode) {
        if (articlesId != null) {
            try {
                ArticleDTO articleDTO = articleService.getById(articlesId);
                mode.addAttribute("Article", articleDTO);
                logger.info("New to display := {} ", articleDTO);
            } catch (ArticleServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_ERROR_404;
            }
            return PUBLIC_SALE_ARTICLE_PAGE;
        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @PostMapping(PUBLIC_SALE_DELETE_ARTICLE_URL)
    public String deleteArticle(@RequestParam(required = false) Long id, RedirectAttributes redirectAttributes) {
        logger.info("Request to delete Article by ID {}.", id);
        if (id != null) {
            try {
                articleService.deleteArticle(id);
                redirectAttributes.addFlashAttribute("message", "Article successfully deleted");
                return PUBLIC_SALE_REDIRECT_ARTICLE_SHOWING_URL;
            } catch (ArticleServiceException e) {
                logger.info("Article by id:= {} not found.", id);
                return REDIRECT_ERROR_404;
            }
        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @PostMapping(PUBLIC_SALE_DELETE_COMMENT_ARTICLE_URL)
    public String deleteComment(@RequestParam(required = false) Long commentId,
                                @RequestParam(required = false) Long articleId,
                                RedirectAttributes redirectAttributes) {
        logger.info("Request to delete Comment by ID:= {}, Article:= {}", commentId, articleId);
        if (commentId != null) {
            try {
                articleService.deleteCommentByd(commentId);
                redirectAttributes.addFlashAttribute("message", "Comment successfully deleted");
                return String.format(PUBLIC_SALE_REDIRECT_ARTICLE_SHOWING_MORE_URL, articleId);
            } catch (ArticleServiceException e) {
                logger.info("Comment by id:= {} not found.", commentId);
                return REDIRECT_ERROR_404;
            }
        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @GetMapping(PUBLIC_SALE_FORM_CREATION_ARTICLE_URL)
    public String formOfArticleCreation(ArticleDTO articleDTO, BindingResult results, Model model) {
        List<ThemeDTO> themeDTOS = themeService.getThemes();
        logger.info("List of Themes for submission {}.", themeDTOS);
        model.addAttribute("themes", themeDTOS);
        articleDTO.setThemeDTO(new ThemeDTO());

        if (model.containsAttribute("error")) {
            for (ObjectError error : ((List<ObjectError>) model.asMap().get("error"))) {

                results.addError(error);
            }
        }
        return PUBLIC_SALE_NEW_ARTICLE_PAGE;
    }

    @PostMapping(PUBLIC_SALE_CREATION_ARTICLE_URL)
    public String articleCreation(Authentication authentication,
                                  @Valid ArticleDTO articleDTO,
                                  BindingResult results,
                                  RedirectAttributes redirectAttributes) {
        logger.info("Request to article Creation := {}, Article:= {}", articleDTO);
        themeValidator.validate(articleDTO.getThemeDTO(), results);
        if (results.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", results.getAllErrors());
            return PUBLIC_SALE_REDIRECT_FORM_CREATION_ARTICLE_URL;
        }
        Long idUser;
        try {
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            logger.info("Get user out of session := {}.", userDetail);
            if (Objects.isNull(idUser = userDetail.getUserDTO().getId())) {
                return REDIRECT_LOGIN_URL;
            }
        } catch (NullPointerException e) {
            logger.error("Request denied. Error code := {}.", ERROR_GET_UNAUTHORIZED_USER);
            logger.error(e.getMessage(), e);
            return REDIRECT_LOGIN_URL;
        }
        articleDTO.setAuthor(new UserDTO(idUser));
        articleService.add(articleDTO);
        redirectAttributes.addFlashAttribute("message", "Article successfully creation");
        return PUBLIC_SALE_REDIRECT_FORM_CREATION_ARTICLE_URL;
    }

    @GetMapping(PUBLIC_SALE_ARTICLES_FORM_UPDATE_ARTICLE_URL)
    public String formUpdateArticle(@RequestParam(required = false) Long articleId,
                                    ArticleDTO articleDTO,
                                    BindingResult results,
                                    Model model) {
        logger.info("Request to update article by ID:= {}", articleId);
        if (articleId != null) {
            try {
                List<ThemeDTO> themeDTOS = themeService.getThemes();
                ArticleDTO article = articleService.getById(articleId);
                logger.info("List of Themes for submission {}.", themeDTOS);
                logger.info("Article to update := {} ", article);

                articleDTO.setThemeDTO(new ThemeDTO());
                model.addAttribute("article", article);
                model.addAttribute("themes", themeDTOS);

                if (model.containsAttribute("error")) {
                    for (ObjectError error : ((List<ObjectError>) model.asMap().get("error"))) {
                        results.addError(error);
                    }
                }
                return PUBLIC_SALE_FORM_UPDATE_ARTICLE_PAGE;
            } catch (ArticleServiceException e) {
                logger.info("article by id:= {} not found.", articleId);
                return REDIRECT_ERROR_404;
            }
        } else {
            return null;
        }
    }

    @PostMapping(PUBLIC_SALE_UPDATE_ARTICLE_URL)
    public String updateArticle(@Valid ArticleDTO articleDTO,
                                BindingResult results,
                                RedirectAttributes redirectAttributes) {
        logger.info("Request to update article := {}, Article:= {}", articleDTO);
        themeValidator.validate(articleDTO.getThemeDTO(), results);
        if (results.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", results.getAllErrors());
            return String.format(PUBLIC_SALE_REDIRECT_ARTICLES_FORM_UPDATE_ARTICLE_URL, articleDTO.getId());
        }

        try {
            articleService.update(articleDTO);
            redirectAttributes.addFlashAttribute("message", "Article successfully creation");
            return String.format(PUBLIC_SALE_REDIRECT_ARTICLES_FORM_UPDATE_ARTICLE_URL, articleDTO.getId());
        } catch (ArticleServiceException e) {
            logger.info("article by id:= {} not found.", articleDTO.getId());
            return REDIRECT_ERROR_404;
        }
    }
}

