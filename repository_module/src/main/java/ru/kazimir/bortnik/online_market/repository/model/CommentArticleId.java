package ru.kazimir.bortnik.online_market.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Comment")
public class CommentArticleId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_article_id")
    private Long commentArticleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentArticleId that = (CommentArticleId) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(commentArticleId, that.commentArticleId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, commentArticleId);
    }
}
