package ru.kazimir.bortnik.online_market.controllers.web.admin;

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
import ru.kazimir.bortnik.online_market.model.WrappingSheetReviews;
import ru.kazimir.bortnik.online_market.service.ReviewService;
import ru.kazimir.bortnik.online_market.service.exception.ReviewServiceException;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.Pageable;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import javax.validation.Valid;

import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_DELETE_REVIEWS;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_UPDATE_STATUS_SHOWING_REVIEWS;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_REVIEWS_DELETE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_REVIEWS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_REVIEWS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_REVIEWS_UPDATE_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_REVIEWS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_404;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_PRIVATE_REVIEWS_SHOWING;

@Controller
@RequestMapping(PRIVATE_REVIEWS_URL)
public class AdminReviewsWebController {
    private final static Logger logger = LoggerFactory.getLogger(AdminReviewsWebController.class);
    private final Pageable pageable = new Pageable(10L);
    private final ReviewService reviewService;

    @Autowired
    public AdminReviewsWebController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(PRIVATE_REVIEWS_SHOWING_URL)
    public String showReviews(@RequestParam(defaultValue = "1", value = "currentPage") Long currentPage, Model model) {
        PageDTO<ReviewDTO> pageDTO = reviewService.getReviews(pageable.getLimitPositions(), currentPage);
        logger.info("List of reviews for submission {}.", pageDTO.getList());
        WrappingSheetReviews WrappingSheetReviews = new WrappingSheetReviews();
        WrappingSheetReviews.setReviewList(pageDTO.getList());
        model.addAttribute("reviews", WrappingSheetReviews);
        model.addAttribute("page", pageDTO);
        return PRIVATE_REVIEWS_PAGE;
    }

    @PostMapping(PRIVATE_REVIEWS_UPDATE_SHOWING_URL)
    public String updateStatusShowing(@Valid WrappingSheetReviews WrappingSheetReviews,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        logger.info("OrderDTO status change request {} ", WrappingSheetReviews.getReviewList());
        if (bindingResult.hasErrors()) {
            logger.info("Request denied. Error code := {}", ERROR_UPDATE_STATUS_SHOWING_REVIEWS);
            return REDIRECT_PRIVATE_REVIEWS_SHOWING;
        }
        try {
            reviewService.updateHidden(WrappingSheetReviews.getReviewList());
        } catch (ReviewServiceException e) {
            return REDIRECT_ERROR_404;
        }
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
        try {
            reviewService.deleteById(reviewDTO.getId());
        } catch (ReviewServiceException e) {
            return REDIRECT_ERROR_404;
        }
        redirectAttributes.addFlashAttribute("message", "Review was deleted successfully.");
        return REDIRECT_PRIVATE_REVIEWS_SHOWING;
    }
}
