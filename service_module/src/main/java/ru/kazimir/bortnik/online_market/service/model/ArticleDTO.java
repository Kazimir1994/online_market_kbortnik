package ru.kazimir.bortnik.online_market.service.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class ArticleDTO {
    private Long id;

    @NotNull
    @Size(min = 10, max = 200, message = "{article.error.title}")
    private String title;

    @NotNull
    @Size(min = 10, max = 1000, message = "{article.error.content}")
    private String content;

    private String summary;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dataCreate;

    @NotNull
    @Valid
    private ThemeDTO themeDTO;

    private boolean deleted;
    private UserDTO author;
    private List<CommentDTO> commentDTOList;

    public ArticleDTO() {
    }

    public ArticleDTO(Long id) {
        this.id = id;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public List<CommentDTO> getCommentDTOList() {
        return commentDTOList;
    }

    public void setCommentDTOList(List<CommentDTO> commentDTOList) {
        this.commentDTOList = commentDTOList;
    }

    public ThemeDTO getThemeDTO() {
        return themeDTO;
    }

    public void setThemeDTO(ThemeDTO themeDTO) {
        this.themeDTO = themeDTO;
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
                "id=" + id +
                ", dataCreate=" + dataCreate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", deleted=" + deleted +
                ", author=" + author +
                ", commentDTOList=" + commentDTOList +
                ", themeDTO=" + themeDTO +
                "}\n";
    }
}
