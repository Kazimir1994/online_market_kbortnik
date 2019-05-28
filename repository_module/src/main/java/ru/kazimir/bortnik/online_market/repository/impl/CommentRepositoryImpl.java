package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.CommentRepository;
import ru.kazimir.bortnik.online_market.repository.model.Comment;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {
}
