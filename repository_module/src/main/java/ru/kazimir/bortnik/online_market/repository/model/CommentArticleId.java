package ru.kazimir.bortnik.online_market.repository.model;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Comment")
@Where(clause = "deleted = '0'")
public class CommentArticleId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_article_id")
    private Long commentArticleId;

    @Column(name = "deleted")
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

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
