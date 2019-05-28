package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Article;
import ru.kazimir.bortnik.online_market.repository.model.Comment;
import ru.kazimir.bortnik.online_market.repository.model.Theme;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.CommentDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MoreArticleConverterImpl implements Converter<ArticleDTO, Article> {
    private final Converter<UserDTO, User> userNewsPageConverter;
    private final Converter<ThemeDTO, Theme> themeConverter;
    private final Converter<CommentDTO, Comment> commentConverter;

    public MoreArticleConverterImpl(@Qualifier("authorConverterImpl") Converter<UserDTO, User> userNewsPageConverter,
                                    Converter<ThemeDTO, Theme> themeConverter,
                                    Converter<CommentDTO, Comment> commentConverter) {
        this.userNewsPageConverter = userNewsPageConverter;
        this.themeConverter = themeConverter;
        this.commentConverter = commentConverter;
    }

    @Override
    public ArticleDTO toDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDataCreate(article.getDataCreate());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        UserDTO userDTO = userNewsPageConverter.toDTO(article.getAuthor());
        articleDTO.setAuthor(userDTO);
        List<CommentDTO> commentDTOS = article.getComments()
                .stream().map(commentConverter::toDTO).collect(Collectors.toList());
        articleDTO.setCommentDTOList(commentDTOS);
        articleDTO.setThemeDTO(themeConverter.toDTO(article.getTheme()));
        return articleDTO;
    }

    @Override
    public Article fromDTO(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setContent(articleDTO.getContent());
        article.setTitle(articleDTO.getTitle());
        article.setAuthor(userNewsPageConverter.fromDTO(articleDTO.getAuthor()));
        return article;
    }
}
