package ru.kazimir.bortnik.online_market.service.model;

import java.sql.Timestamp;

public class CommentDTO {
    private Long id;
    private Timestamp dataCreate;
    private String content;
    private UserDTO userDTO;
    private ArticleDTO articleDTO;

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

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public ArticleDTO getArticleDTO() {
        return articleDTO;
    }

    public void setArticleDTO(ArticleDTO articleDTO) {
        this.articleDTO = articleDTO;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", dataCreate=" + dataCreate +
                ", content='" + content + '\'' +
                ", userDTO=" + userDTO +
                '}';
    }
}
