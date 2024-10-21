package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.ProductDTO;
import com.ecommerce.trenzio.model.Category;
import com.ecommerce.trenzio.model.Product;
import com.ecommerce.trenzio.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class})
public abstract class ProductMapper {

    // No need to @Autowired CategoryMapper or UserMapper directly

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    public abstract Product dtoToProduct(ProductDTO productDTO);

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "sellerId", source = "user.userId")
    public abstract ProductDTO productToDTO(Product product);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    public abstract void updateProductFromDTO(ProductDTO productDTO, @MappingTarget Product product);

    @AfterMapping
    protected void setCategoryAndUser(ProductDTO productDTO, @MappingTarget Product product) {
        if (productDTO.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(productDTO.getCategoryId());
            product.setCategory(category);
        }

        if (productDTO.getSellerId() != null) {
            User user = new User();
            user.setUserId(productDTO.getSellerId());
            product.setUser(user);
        }
    }
}
