package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.filter.FilterNewsPage;

import java.util.List;

public interface ArticleService {

    PageDTO<ArticleDTO> getArticles(FilterNewsPage filterNewsPage, Long currentPage);

    List<ArticleDTO> getArticles(int offset, int limit);

    List<ThemeDTO> getThemes();

    ArticleDTO getArticleById(Long id);

    List<ArticleDTO> getTop(int sizeTop);

    void deleteArticle(Long id);

    void save(ArticleDTO articleDTO);
}
