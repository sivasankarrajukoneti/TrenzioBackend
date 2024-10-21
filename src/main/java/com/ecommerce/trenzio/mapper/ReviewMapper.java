package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.ReviewDTO;
import com.ecommerce.trenzio.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    // Map Review entity to ReviewDTO
    @Mapping(target = "userId", source = "user.userId")       // Extract user ID
    @Mapping(target = "productId", source = "product.productId") // Extract product ID
    ReviewDTO reviewToDTO(Review review);

    // Map ReviewDTO to Review entity
    @Mapping(target = "user.userId", source = "userId")       // Set user reference by ID
    @Mapping(target = "product.productId", source = "productId") // Set product reference by ID
    Review dtoToReview(ReviewDTO reviewDTO);
}
