package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Article;
import ru.kazimir.bortnik.online_market.repository.model.Theme;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class NewsPageConverterImpl implements Converter<ArticleDTO, Article> {
    private final Converter<UserDTO, User> userNewsPageConverter;
    private final Converter<ThemeDTO, Theme> themeConverter;

    public NewsPageConverterImpl(@Qualifier("authorConverterImpl") Converter<UserDTO, User> userNewsPageConverter,
                                 Converter<ThemeDTO, Theme> themeConverter) {
        this.userNewsPageConverter = userNewsPageConverter;
        this.themeConverter = themeConverter;
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
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        User user = userNewsPageConverter.fromDTO(articleDTO.getAuthor());
        article.setAuthor(user);
        article.setTheme(themeConverter.fromDTO(articleDTO.getThemeDTO()));
        return article;
    }
}
