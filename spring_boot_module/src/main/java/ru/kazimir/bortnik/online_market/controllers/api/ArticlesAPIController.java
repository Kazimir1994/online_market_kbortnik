package ru.kazimir.bortnik.online_market.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kazimir.bortnik.online_market.service.ArticleService;
import ru.kazimir.bortnik.online_market.service.exception.ArticleServiceException;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDetail;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ARTICLES_DELETE_ID_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ARTICLES_SAVE_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ARTICLES_SHOWING_ID_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ARTICLES_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ARTICLES_URL;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_GET_UNAUTHORIZED_USER;

@RestController
@RequestMapping(API_ARTICLES_URL)
public class ArticlesAPIController {
    private final static Logger logger = LoggerFactory.getLogger(ArticlesAPIController.class);

    private final ArticleService articleService;
    private final Validator themeValidator;

    public ArticlesAPIController(ArticleService articleService,
                                 @Qualifier("themeValidatorImpl") Validator themeValidator) {
        this.articleService = articleService;
        this.themeValidator = themeValidator;
    }

    @GetMapping(API_ARTICLES_SHOWING_URL)
    public ResponseEntity<List<ArticleDTO>> getArticles(
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "offset", defaultValue = "0") int offset) {
        logger.info("Request for receiving articles from the position := {}, quantity := {}.", limit, offset);
        List<ArticleDTO> articleDTOS = articleService.getArticles(offset, limit);
        logger.info("Send a list of articles. := {}.", articleDTOS);
        return new ResponseEntity<>(articleDTOS, HttpStatus.OK);
    }

    @GetMapping(API_ARTICLES_SHOWING_ID_URL)
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable("id") Long id) {
        logger.info("Request for article by id := {}.", id);
        try {
            ArticleDTO articleDTO = articleService.getById(id);
            logger.info("Send a list of article. := {}.", articleDTO);
            return new ResponseEntity<>(articleDTO, HttpStatus.OK);
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(API_ARTICLES_DELETE_ID_URL)
    public ResponseEntity deleteArticle(@PathVariable("id") Long id) {
        try {
            logger.info("Requests delete by id := {}.", id);
            articleService.deleteArticle(id);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(API_ARTICLES_SAVE_URL)
    public ResponseEntity addArticle(Authentication authentication,
                                     @RequestBody @Valid ArticleDTO articleDTO,
                                     BindingResult bindingResult) {
        themeValidator.validate(articleDTO.getThemeDTO(), bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("error := {}.", bindingResult.getAllErrors());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Long idUser;
        try {
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            logger.info("Get user out of session := {}.", userDetail);
            if (Objects.isNull(idUser = userDetail.getUserDTO().getId())) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            logger.error("Request denied. Error code := {}.", ERROR_GET_UNAUTHORIZED_USER);
            logger.error(e.getMessage(), e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        articleDTO.setAuthor(new UserDTO(idUser));
        logger.error("Save new news {}", articleDTO);
        articleService.add(articleDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
