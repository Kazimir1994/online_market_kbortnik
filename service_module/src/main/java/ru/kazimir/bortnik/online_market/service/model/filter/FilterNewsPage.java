package ru.kazimir.bortnik.online_market.service.model.filter;

public class FilterNewsPage {
    private Long limitPositions;
    private Long theme;
    private final Long[] limits = {5L, 10L, 20L, 30L, 40L, 50L};

    public FilterNewsPage(Long limitPositions, Long theme) {
        this.limitPositions = limitPositions;
        this.theme = theme;
    }

    public Long getLimitPositions() {
        return limitPositions;
    }

    public void setLimitPositions(Long limitPositions) {
        this.limitPositions = limitPositions;
    }

    public Long getTheme() {
        return theme;
    }

    public void setTheme(Long theme) {
        this.theme = theme;
    }

    public Long[] getLimits() {
        return limits;
    }
}
