package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.Article;
import ru.kazimir.bortnik.online_market.repository.model.Theme;

import java.util.List;

public interface ArticleRepository extends GenericRepository<Long, Article> {

    Long getCountOfArticle(Long idTheme);

    List<Article> getThemeArticles(Long limit, Long offset, Long idTheme);

    List<Theme> findAllTheme();

    List<Article> findAll(int offset, int limit);

    List<Article> getTop(int sizeTop);
}
