package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Article;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class NewsPageConverterImpl implements Converter<ArticleDTO, Article> {
    private final Converter<UserDTO, User> userNewsPageConverter;

    public NewsPageConverterImpl(@Qualifier("userNewsPageConverterImpl") Converter<UserDTO, User> userNewsPageConverter) {
        this.userNewsPageConverter = userNewsPageConverter;
    }

    @Override
    public ArticleDTO toDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDataCreate(article.getDataCreate());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setSummary(article.getSummary());
        UserDTO userDTO = userNewsPageConverter.toDTO(article.getAuthor());
        articleDTO.setAuthor(userDTO);
        return articleDTO;
    }

    @Override
    public Article fromDTO(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setId(articleDTO.getId());
        article.setDataCreate(articleDTO.getDataCreate());
        article.setTitle(articleDTO.getTitle());
        article.setSummary(articleDTO.getSummary());
        User user = userNewsPageConverter.fromDTO(articleDTO.getAuthor());
        article.setAuthor(user);
        return article;
    }
}
