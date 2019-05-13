package ru.kazimir.bortnik.online_market.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.model.Pageable;
import ru.kazimir.bortnik.online_market.model.ShellAboveReviewSheet;
import ru.kazimir.bortnik.online_market.service.ReviewService;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import javax.validation.Valid;
import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_DELETE_REVIEWS;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_UPDATE_STATUS_SHOWING_REVIEWS;
import static ru.kazimir.bortnik.online_market.constant.URLConstants.*;

@Controller
@RequestMapping(PRIVATE_REVIEWS_URL)
public class ReviewsWebController {
    private final static Logger logger = LoggerFactory.getLogger(ReviewsWebController.class);
    private final Pageable pageable = new Pageable(10L);
    private final ReviewService reviewService;

    @Autowired
    public ReviewsWebController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(PRIVATE_REVIEWS_SHOWING_URL)
    public String showReviews(@RequestParam(defaultValue = "1", value = "currentPage") Long currentPage, Model model) {
        Long amountOfPages = reviewService.getNumberOfPages(pageable.getLimitPositions());
        if (currentPage > amountOfPages) {
            currentPage = amountOfPages;
        } else if (currentPage < amountOfPages) {
            currentPage = 1L;
        }
        List<ReviewDTO> reviewDTOS = reviewService.getReviews(pageable.getLimitPositions(), currentPage);
        ShellAboveReviewSheet shellAboveReviewSheet = new ShellAboveReviewSheet();
        shellAboveReviewSheet.setReviewList(reviewDTOS);
        model.addAttribute("reviews", shellAboveReviewSheet);
        model.addAttribute("SizePage", amountOfPages);
        model.addAttribute("currentPage", currentPage);
        logger.info("List of reviews for submission {}.", reviewDTOS);
        return PRIVATE_REVIEWS_PAGE;
    }

    @PostMapping(PRIVATE_REVIEWS_UPDATE_SHOWING_URL)
    public String updateStatusShowing(@Valid ShellAboveReviewSheet shellAboveReviewSheet,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        logger.info("Order status change request", shellAboveReviewSheet.getReviewList());
        if (bindingResult.hasErrors()) {
            logger.info("Request denied. Error code := {}", ERROR_UPDATE_STATUS_SHOWING_REVIEWS);
            return REDIRECT_PRIVATE_REVIEWS_SHOWING;
        }

        reviewService.updateShowing(shellAboveReviewSheet.getReviewList());
        redirectAttributes.addFlashAttribute("message", "Review's showing status was changed successfully.");
        return REDIRECT_PRIVATE_REVIEWS_SHOWING;
    }

    @PostMapping(PRIVATE_REVIEWS_DELETE_URL)
    public String deleteReviews(@Valid ReviewDTO reviewDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        logger.info("Request to delete thr review with ID = {}.", reviewDTO.getId());
        if (bindingResult.hasErrors()) {
            logger.info("Request denied. Error code := {}", ERROR_DELETE_REVIEWS);
            redirectAttributes.addFlashAttribute("message", "Incorrect data to delete.");
            return REDIRECT_PRIVATE_REVIEWS_SHOWING;
        }
        reviewService.deleteReviewsById(reviewDTO.getId());
        redirectAttributes.addFlashAttribute("message", "Review was deleted successfully.");
        return REDIRECT_PRIVATE_REVIEWS_SHOWING;
    }
}
