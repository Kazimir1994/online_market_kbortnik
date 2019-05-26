package ru.kazimir.bortnik.online_market.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.controllers.web.admin.AdminReviewsWebController;
import ru.kazimir.bortnik.online_market.model.WrappingSheetReviews;
import ru.kazimir.bortnik.online_market.service.ReviewService;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import java.util.ArrayList;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminReviewsWebControllerTest {

    @Mock
    private ReviewService reviewService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private
    RedirectAttributes redirectAttributes;

    private MockMvc mockMvc;
    private AdminReviewsWebController adminReviewsWebController;
    private WrappingSheetReviews WrappingSheetReviews;
    private ArrayList<ReviewDTO> reviewDTOList;

    @Before
    public void init() {
        adminReviewsWebController = new AdminReviewsWebController(reviewService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminReviewsWebController).build();

        reviewDTOList = new ArrayList<>();
        ReviewDTO reviewDTO1 = new ReviewDTO();
        reviewDTO1.setReview("TEST1");
        ReviewDTO reviewDTO2 = new ReviewDTO();
        reviewDTO2.setReview("TEST2");

        reviewDTOList.add(reviewDTO1);
        reviewDTOList.add(reviewDTO2);
        WrappingSheetReviews = new WrappingSheetReviews();

    }

    @Test
    public void ifNullObjectReviewsCameThenTheRemovalMethodShouldNotBeCalled() {
        when(bindingResult.hasErrors()).thenReturn(true);
        ReviewDTO reviewDTO = new ReviewDTO();
        adminReviewsWebController.deleteReviews(reviewDTO, bindingResult, redirectAttributes);
        verify(reviewService, never()).deleteById(reviewDTO.getId());
    }

    @Test
    public void ifNonNullObjectReviewsCameInThenTheRemovalMethodShouldBeCalled() {
        when(bindingResult.hasErrors()).thenReturn(false);
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(4L);
        adminReviewsWebController.deleteReviews(reviewDTO, bindingResult, redirectAttributes);
        verify(reviewService, Mockito.times(1)).deleteById(reviewDTO.getId());
    }
}
