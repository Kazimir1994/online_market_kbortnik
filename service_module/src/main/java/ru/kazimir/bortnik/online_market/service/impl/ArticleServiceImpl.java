package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.ArticleRepository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.model.Article;
import ru.kazimir.bortnik.online_market.repository.model.Theme;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.ArticleService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.ArticleServiceException;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterNewsPage;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ARTICLE;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ARTICLES;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ARTICLE_TOP;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_THEME;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ERROR_DELETE_ARTICLE_BY_ID;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ERROR_GET_ARTICLE_BY_ID;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final Converter<ArticleDTO, Article> newsPageConverter;
    private final Converter<ThemeDTO, Theme> themeConverter;
    private final Converter<ArticleDTO, Article> moreArticleConverter;
    private final UserRepository userRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              @Qualifier("newsPageConverterImpl") Converter<ArticleDTO, Article> newsPageConverter,
                              Converter<ThemeDTO, Theme> themeConverter,
                              @Qualifier("moreArticleConverterImpl") Converter<ArticleDTO, Article> moreArticleConverter,
                              UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.newsPageConverter = newsPageConverter;
        this.themeConverter = themeConverter;
        this.moreArticleConverter = moreArticleConverter;
        this.userRepository = userRepository;
    }

    private final int MAX_SIZE_SUMMARY = 200;
    private final String END_SUMMARY = "...";

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticles(FilterNewsPage filterNewsPage, Long currentPage) {
        PageDTO<ArticleDTO> pageDTO = new PageDTO<>();
        pageDTO.setCurrentPage(currentPage);
        Long countOfArticle = articleRepository.getCountOfArticle(filterNewsPage.getTheme());
        Long countOfPages = calculationCountOfPages(countOfArticle, filterNewsPage.getLimitPositions());
        pageDTO.setCountOfPages(countOfPages);

        if (currentPage > countOfPages) {
            currentPage = countOfPages;
        } else if (currentPage < countOfPages) {
            currentPage = 1L;
        }
        Long offset = getPosition(filterNewsPage.getLimitPositions(), currentPage);
        logger.info(MESSAGES_GET_ARTICLE, filterNewsPage.getLimitPositions(), offset, filterNewsPage.getTheme());
        List<Article> articleList = articleRepository.getThemeArticles(filterNewsPage.getLimitPositions(), offset, filterNewsPage.getTheme());
        List<ArticleDTO> articleDTOList = articleList.stream().map(newsPageConverter::toDTO).collect(Collectors.toList());
        pageDTO.setList(articleDTOList);
        return pageDTO;
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticles(int offset, int limit) {
        List<Article> article = articleRepository.findAll(offset, limit);
        logger.info(MESSAGES_GET_ARTICLES, article);
        return article.stream().map(moreArticleConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ThemeDTO> getThemes() {
        List<Theme> themeList = articleRepository.findAllTheme();
        List<ThemeDTO> themeDTOList = themeList.stream().map(themeConverter::toDTO).collect(Collectors.toList());
        logger.info(MESSAGES_GET_THEME, themeDTOList);
        return themeDTOList;
    }

    @Override
    @Transactional
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            return moreArticleConverter.toDTO(article);
        } else {
            logger.error(ERROR_GET_ARTICLE_BY_ID, id);
            throw new ArticleServiceException(String.format(ERROR_GET_ARTICLE_BY_ID, id));
        }
    }

    @Override
    @Transactional
    public List<ArticleDTO> getTop(int sizeTop) {
        List<Article> article = articleRepository.getTop(sizeTop);
        logger.info(MESSAGES_GET_ARTICLE_TOP, article, sizeTop);
        return article.stream().map(newsPageConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            articleRepository.remove(article);
        } else {
            logger.error(ERROR_DELETE_ARTICLE_BY_ID, id);
            throw new ArticleServiceException(String.format(ERROR_DELETE_ARTICLE_BY_ID, id));
        }
    }

    @Override
    public void save(ArticleDTO articleDTO) {
        Article article = newsPageConverter.fromDTO(articleDTO);
        User author = userRepository.findById(article.getAuthor().getId());
        article.setAuthor(author);
        article.setDataCreate(new Timestamp(System.currentTimeMillis()));
        article.setSummary(getSummary(article.getContent()));
        articleRepository.persist(article);
    }

    private Long getPosition(Long limitPositions, Long positions) {
        if (positions == 0) {
            positions++;
        }
        return limitPositions * (positions - 1);
    }

    private String getSummary(String summary) {
        if (summary.length() > MAX_SIZE_SUMMARY) {
            return summary.substring(0, MAX_SIZE_SUMMARY - END_SUMMARY.length()) + END_SUMMARY;
        }
        return summary;
    }

    private Long calculationCountOfPages(Long countOfArticle, Long limitPositions) {
        Long countOfPages;
        if (countOfArticle % limitPositions > 0) {
            countOfPages = (countOfArticle / limitPositions) + 1;
        } else {
            countOfPages = countOfArticle / limitPositions;
        }
        return countOfPages;
    }
}
