package ru.kazimir.bortnik.online_market.model;

import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class WrappingSheetReviews {
    @NotNull
    private List<ReviewDTO> reviewList;

    public List<ReviewDTO> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<ReviewDTO> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public String toString() {
        return "WrappingSheetReviews{" +
                "reviewList=" + reviewList +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrappingSheetReviews that = (WrappingSheetReviews) o;
        return Objects.equals(reviewList, that.reviewList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(reviewList);
    }
}
