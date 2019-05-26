package ru.kazimir.bortnik.online_market.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Article")
@SQLDelete(sql = "UPDATE Article SET deleted = 1  WHERE id = ? ")
@Where(clause = "deleted = '0'")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_create")
    private Date dataCreate;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "summary")
    private String summary;

    @Column(name = "deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    @OrderBy(value = "dataCreate desc")
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme")
    private Theme theme;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(Date dataCreate) {
        this.dataCreate = dataCreate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", dataCreate=" + dataCreate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", deleted=" + deleted +
                ", author=" + author +
                ", comments=" + comments +
                ", theme=" + theme +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return deleted == article.deleted &&
                Objects.equals(id, article.id) &&
                Objects.equals(dataCreate, article.dataCreate) &&
                Objects.equals(title, article.title) &&
                Objects.equals(content, article.content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, dataCreate, title, content);
    }
}
