package ru.kazimir.bortnik.online_market.controllers.web.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.service.ReviewService;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDetail;

import javax.validation.Valid;
import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_ADD_REVIEW;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ADD_REVIEWS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ADD_REVIEWS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_REDIRECT_ADD_REVIEWS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_REVIEWS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_SHOW_FORM_SEND_REVIEWS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_LOGIN_URL;

@Controller
@RequestMapping(PUBLIC_CUSTOMER_REVIEWS_URL)
public class CustomerReviewsWebController {
    private final static Logger logger = LoggerFactory.getLogger(CustomerReviewsWebController.class);
    private final ReviewService reviewService;

    public CustomerReviewsWebController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(PUBLIC_CUSTOMER_SHOW_FORM_SEND_REVIEWS_URL)
    public final String showFormSendReviews(ReviewDTO reviewDTO, BindingResult results, Model model) {
        if (model.containsAttribute("error")) {
            for (ObjectError error : ((List<ObjectError>) model.asMap().get("error"))) {
                results.addError(error);
            }
        }
        return PUBLIC_CUSTOMER_ADD_REVIEWS_PAGE;
    }

    @PostMapping(PUBLIC_CUSTOMER_ADD_REVIEWS_URL)
    public final String addArticle(@Valid ReviewDTO reviewDTO,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal UserDetail UserDetail) {
        if (UserDetail.getUserDTO() != null) {
            logger.info("Request to add review= {}.", reviewDTO);
            if (bindingResult.hasFieldErrors("review")) {
                redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
                logger.info("Request denied. Error code := {},{}", ERROR_ADD_REVIEW, bindingResult.getAllErrors());
                return PUBLIC_CUSTOMER_REDIRECT_ADD_REVIEWS_URL;
            }
            reviewDTO.setUserDTO(new UserDTO(UserDetail.getUserDTO().getId()));
            reviewService.add(reviewDTO);
            redirectAttributes.addFlashAttribute("message", "Your review has been successfully added.");
            return PUBLIC_CUSTOMER_REDIRECT_ADD_REVIEWS_URL;
        } else {
            return REDIRECT_LOGIN_URL;
        }
    }
}

