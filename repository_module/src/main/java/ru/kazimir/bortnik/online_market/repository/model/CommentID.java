package ru.kazimir.bortnik.online_market.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Comment")
@SQLDelete(sql = "UPDATE Article SET deleted = 1  WHERE id = ? ")
@Where(clause = "deleted = '0'")
public class CommentID {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_create")
    private Timestamp dataCreate;

    @Column(name = "content")
    private String content;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "commentArticleId")
    private Long commentArticleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(Timestamp dataCreate) {
        this.dataCreate = dataCreate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCommentArticleId() {
        return commentArticleId;
    }

    public void setCommentArticleId(Long commentArticleId) {
        this.commentArticleId = commentArticleId;
    }

    @Override
    public String toString() {
        return "CommentID{" +
                "id=" + id +
                ", dataCreate=" + dataCreate +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", commentArticleId=" + commentArticleId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentID commentID = (CommentID) o;
        return deleted == commentID.deleted &&
                Objects.equals(id, commentID.id) &&
                Objects.equals(dataCreate, commentID.dataCreate) &&
                Objects.equals(content, commentID.content) &&
                Objects.equals(commentArticleId, commentID.commentArticleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataCreate, content, deleted, commentArticleId);
    }
}
