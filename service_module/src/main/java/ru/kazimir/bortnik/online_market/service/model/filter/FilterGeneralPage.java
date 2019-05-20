package ru.kazimir.bortnik.online_market.service.model.filter;

public class FilterGeneralPage {
    private int amountOfNewsDisplayed = 3;

    public FilterGeneralPage() {
    }

    public FilterGeneralPage(int amountOfNewsDisplayed) {
        this.amountOfNewsDisplayed = amountOfNewsDisplayed;
    }

    public int getAmountOfNewsDisplayed() {
        return amountOfNewsDisplayed;
    }

    public void setAmountOfNewsDisplayed(int amountOfNewsDisplayed) {
        this.amountOfNewsDisplayed = amountOfNewsDisplayed;
    }
}
