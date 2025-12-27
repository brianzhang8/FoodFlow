package com.brian.foodapp.review.service;

import com.brian.foodapp.response.Response;
import com.brian.foodapp.review.dtos.ReviewDTO;
import java.util.List;

public interface ReviewService {

    Response<ReviewDTO> createReview(ReviewDTO reviewDTO);

    Response<List<ReviewDTO>> getReviewsForMenu(Long menuId);

    Response<Double> getAverageRating(Long menuId);
}
