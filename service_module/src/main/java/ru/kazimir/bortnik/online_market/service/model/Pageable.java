package ru.kazimir.bortnik.online_market.service.model;

public class Pageable {
    private Long limitPositions;

    public Pageable(Long limitPositions) {
        this.limitPositions = limitPositions;
    }

    public Long getLimitPositions() {
        return limitPositions;
    }

    public void setLimitPositions(Long limitPositions) {
        this.limitPositions = limitPositions;
    }
}
