package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.ArticleRepository;
import ru.kazimir.bortnik.online_market.repository.CommentRepository;
import ru.kazimir.bortnik.online_market.repository.ThemeRepository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.model.Article;
import ru.kazimir.bortnik.online_market.repository.model.Comment;
import ru.kazimir.bortnik.online_market.repository.model.Theme;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.ArticleService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.ArticleServiceException;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.CommentDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterNewsPage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ARTICLE;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ARTICLES;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ARTICLE_TOP;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ERROR_DELETE_ARTICLE_BY_ID;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ERROR_DELETE_COMMENT_BY_ID;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ERROR_GET_ARTICLE_BY_ID;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;
    private final Converter<ArticleDTO, Article> newsPageConverter;
    private final Converter<ArticleDTO, Article> moreArticleConverter;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ThemeRepository themeRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              @Qualifier("newsPageConverterImpl") Converter<ArticleDTO, Article> newsPageConverter,
                              @Qualifier("moreArticleConverterImpl") Converter<ArticleDTO, Article> moreArticleConverter,
                              UserRepository userRepository,
                              CommentRepository commentRepository,
                              ThemeRepository themeRepository) {
        this.articleRepository = articleRepository;
        this.newsPageConverter = newsPageConverter;
        this.moreArticleConverter = moreArticleConverter;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.themeRepository = themeRepository;
    }

    private final int MAX_SIZE_SUMMARY = 200;
    private final String END_SUMMARY = "...";

    @Transactional
    @Override
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

    @Transactional
    @Override
    public List<ArticleDTO> getArticles(int offset, int limit) {
        List<Article> article = articleRepository.findAll(offset, limit);
        logger.info(MESSAGES_GET_ARTICLES, article);
        return article.stream().map(moreArticleConverter::toDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ArticleDTO getById(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            return moreArticleConverter.toDTO(article);
        } else {
            logger.error(ERROR_GET_ARTICLE_BY_ID, id);
            throw new ArticleServiceException(String.format(ERROR_GET_ARTICLE_BY_ID, id));
        }
    }

    @Transactional
    @Override
    public List<ArticleDTO> getTopArticle(int amountTopArticles) {
        List<Article> article = articleRepository.getTop(amountTopArticles);
        logger.info(MESSAGES_GET_ARTICLE_TOP, article, amountTopArticles);
        return article.stream().map(newsPageConverter::toDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            articleRepository.remove(article);
        } else {
            logger.error(ERROR_DELETE_ARTICLE_BY_ID, id);
            throw new ArticleServiceException(String.format(ERROR_DELETE_ARTICLE_BY_ID, id));
        }
    }

    @Transactional
    @Override
    public void add(ArticleDTO articleDTO) {
        Article article = newsPageConverter.fromDTO(articleDTO);
        User author = userRepository.findById(article.getAuthor().getId());
        article.setAuthor(author);
        article.setDataCreate(new Date(System.currentTimeMillis()));
        article.setSummary(getSummary(article.getContent()));
        articleRepository.persist(article);
    }

    @Transactional
    @Override
    public void deleteCommentByd(Long idComment) {
        Comment comment = commentRepository.findById(idComment);
        if (comment != null) {
            commentRepository.remove(comment);
        } else {
            throw new ArticleServiceException(String.format(ERROR_DELETE_COMMENT_BY_ID, idComment));
        }
    }

    @Transactional
    @Override
    public void update(ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleDTO.getId());
        if (article != null) {
            article.setContent(articleDTO.getContent());
            article.setTitle(articleDTO.getTitle());
            article.setSummary(getSummary(articleDTO.getContent()));
            article.setDataCreate(new Date(System.currentTimeMillis()));
            Theme theme = themeRepository.findById(articleDTO.getThemeDTO().getId());
            article.setTheme(theme);
            articleRepository.merge(article);
        } else {
            logger.error(ERROR_DELETE_ARTICLE_BY_ID, articleDTO.getId());
            throw new ArticleServiceException(String.format(ERROR_DELETE_ARTICLE_BY_ID, articleDTO.getId()));
        }
    }

    @Override
    @Transactional
    public void addComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        User author = userRepository.findByIdNotDeleted(commentDTO.getUserDTO().getId());
        if (author != null) {
            comment.setUser(author);
            comment.setDataCreate(new Timestamp(System.currentTimeMillis()));
            Article article = articleRepository.findById(commentDTO.getArticleDTO().getId());
            if (article != null) {
                comment.setArticle(article);
                commentRepository.persist(comment);
            } else {
                throw new ArticleServiceException();
            }
        } else {
            throw new ArticleServiceException();
        }
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
