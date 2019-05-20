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
import ru.kazimir.bortnik.online_market.controllers.web.AdminReviewsWebController;
import ru.kazimir.bortnik.online_market.model.ShellAboveReviewSheet;
import ru.kazimir.bortnik.online_market.service.ReviewService;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private ShellAboveReviewSheet shellAboveReviewSheet;
    private ArrayList<ReviewDTO> reviewDTOList;

    @Before
    public void init() {
        adminReviewsWebController = new AdminReviewsWebController(reviewService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminReviewsWebController).build();

        reviewDTOList = new ArrayList<>();
        ReviewDTO reviewDTO1 = new ReviewDTO();
        reviewDTO1.setFeedback("TEST1");
        ReviewDTO reviewDTO2 = new ReviewDTO();
        reviewDTO2.setFeedback("TEST2");

        reviewDTOList.add(reviewDTO1);
        reviewDTOList.add(reviewDTO2);
        shellAboveReviewSheet = new ShellAboveReviewSheet();

    }

    @Test
    public void shouldReturnThePageFullOfTestimonials() throws Exception {
        when(reviewService.getReviews(10L, 0L)).thenReturn(reviewDTOList);
        List<ReviewDTO> reviewDTOS = reviewService.getReviews(10L, 0L);
        shellAboveReviewSheet.setReviewList(reviewDTOS);
        mockMvc.perform(get("/private/reviews/showing.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("reviews", equalTo(shellAboveReviewSheet)))
                .andExpect(forwardedUrl("private_reviews"));
    }

    @Test
    public void shouldGetAPageWithFilledPaginationFields() throws Exception {
        when(reviewService.getNumberOfPages(10L)).thenReturn(5L);
        mockMvc.perform(get("/private/reviews/showing.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("SizePage", equalTo(5L)))
                .andExpect(model().attribute("currentPage", equalTo(1L)))
                .andExpect(forwardedUrl("private_reviews"));
    }

    @Test
    public void ifAnEmptyListOfTheUpdateDisplayStatusHasComeThenTheAddMethodShouldNotBeDone() {
        when(bindingResult.hasErrors()).thenReturn(true);
        ShellAboveReviewSheet shellAboveReviewSheet = new ShellAboveReviewSheet();
        adminReviewsWebController.updateStatusShowing(shellAboveReviewSheet, bindingResult, redirectAttributes);
        verify(reviewService, never()).updateShowing(shellAboveReviewSheet.getReviewList());
    }

    @Test
    public void ifTheEmptyDisplayListUpdateListIsNotEmptyThenTheAddMethodMustBeDone() {
        when(bindingResult.hasErrors()).thenReturn(false);
        ShellAboveReviewSheet shellAboveReviewSheet = new ShellAboveReviewSheet();
        shellAboveReviewSheet.setReviewList(reviewDTOList);
        adminReviewsWebController.updateStatusShowing(shellAboveReviewSheet, bindingResult, redirectAttributes);
        verify(reviewService, Mockito.times(1)).updateShowing(shellAboveReviewSheet.getReviewList());
    }

    @Test
    public void ifNullObjectReviewsCameThenTheRemovalMethodShouldNotBeCalled() {
        when(bindingResult.hasErrors()).thenReturn(true);
        ReviewDTO reviewDTO = new ReviewDTO();
        adminReviewsWebController.deleteReviews(reviewDTO, bindingResult, redirectAttributes);
        verify(reviewService, never()).deleteReviewsById(reviewDTO.getId());
    }

    @Test
    public void ifNonNullObjectReviewsCameInThenTheRemovalMethodShouldBeCalled() {
        when(bindingResult.hasErrors()).thenReturn(false);
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(4L);
        adminReviewsWebController.deleteReviews(reviewDTO, bindingResult, redirectAttributes);
        verify(reviewService, Mockito.times(1)).deleteReviewsById(reviewDTO.getId());
    }
}
