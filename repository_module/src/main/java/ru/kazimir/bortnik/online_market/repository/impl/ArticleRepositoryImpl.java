package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.ArticleRepository;
import ru.kazimir.bortnik.online_market.repository.model.Article;
import ru.kazimir.bortnik.online_market.repository.model.CommentArticleId;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {

    @Override
    public Long getCountOfArticle(Long idTheme) {
        String query = "SELECT COUNT(*) FROM " + Article.class.getName() + " where theme.id =:idTheme";
        Query q = entityManager.createQuery(query).setParameter("idTheme", idTheme);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Article> getThemeArticles(Long limit, Long offset, Long idTheme) {
        String query = "from " + Article.class.getName() + " where theme.id =:idTheme order by dataCreate desc ";
        Query q = entityManager.createQuery(query).setParameter("idTheme", idTheme);
        q.setMaxResults(Math.toIntExact(limit));
        q.setFirstResult(Math.toIntExact(offset));
        return q.getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Article> findAll(int offset, int limit) {
        String query = "from " + Article.class.getName() + " order by dataCreate desc";
        Query q = entityManager.createQuery(query)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Article> getTop(int sizeTop) {
        String query = "SELECT commentArticleId  FROM " + CommentArticleId.class.getName()
                + " GROUP BY commentArticleId  ORDER BY commentArticleId DESC ";
        Query queryManager = entityManager.createQuery(query);
        queryManager.setMaxResults(sizeTop);

        List<Long> idTopArticle = queryManager.getResultList();
        if (!idTopArticle.isEmpty()) {
            query = "from " + Article.class.getName() + " where id IN(:idTopArticles)";
            Query q = entityManager.createQuery(query).setParameter("idTopArticles", idTopArticle);
            q.setMaxResults(sizeTop);
            return q.getResultList();
        } else {
            return Collections.emptyList();
        }
    }
}

