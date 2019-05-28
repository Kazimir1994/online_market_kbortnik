package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Comment;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.CommentDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class CommentConverterImpl implements Converter<CommentDTO, Comment> {
    private final Converter<UserDTO, User> userNewsPageConverter;

    public CommentConverterImpl(@Qualifier("authorConverterImpl") Converter<UserDTO, User> userNewsPageConverter) {
        this.userNewsPageConverter = userNewsPageConverter;
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        UserDTO userDTO = userNewsPageConverter.toDTO(comment.getUser());
        commentDTO.setUserDTO(userDTO);
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setDataCreate(comment.getDataCreate());
        return commentDTO;
    }

    @Override
    public Comment fromDTO(CommentDTO commentDTO) {
        Comment comment = new Comment();
        User user = userNewsPageConverter.fromDTO(commentDTO.getUserDTO());
        comment.setUser(user);
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setDataCreate(commentDTO.getDataCreate());
        return comment;
    }
}
