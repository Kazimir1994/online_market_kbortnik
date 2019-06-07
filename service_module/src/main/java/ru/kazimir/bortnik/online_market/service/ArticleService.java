package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.CommentDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterNewsPage;

import java.util.List;

public interface ArticleService {

    PageDTO<ArticleDTO> getArticles(FilterNewsPage filterNewsPage, Long currentPage);

    List<ArticleDTO> getArticles(int offset, int limit);

    ArticleDTO getById(Long id);

    List<ArticleDTO> getTopArticle(int amountTopArticles);

    void deleteArticle(Long id);

    void add(ArticleDTO articleDTO);

    void deleteCommentByd(Long idComment);

    void update(ArticleDTO articleDTO);

    void addComment(CommentDTO commentDTO);
}
