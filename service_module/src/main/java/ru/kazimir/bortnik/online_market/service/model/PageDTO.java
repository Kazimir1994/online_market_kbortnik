package ru.kazimir.bortnik.online_market.service.model;

import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> {
    private List<T> list = new ArrayList<>();
    private Long countOfPages;
    private Long currentPage;

    public Long getCountOfPages() {
        return countOfPages;
    }

    public void setCountOfPages(Long countOfPages) {
        this.countOfPages = countOfPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "list=" + list +
                ", countOfPages=" + countOfPages +
                ", currentPage=" + currentPage +
                '}';
    }
}


