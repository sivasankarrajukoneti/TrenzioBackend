package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.CategoryDTO;
import com.ecommerce.trenzio.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CategoryMapper {

    // Map CategoryDTO to Category entity
    @Mapping(target = "parentCategory", ignore = true)  // Manually handle parent category in service
    @Mapping(target = "products", ignore = true)  // Manually handle products in service
    @Mapping(target = "subcategories", ignore = true)  // Manually handle subcategories in service
    Category dtoToCategory(CategoryDTO categoryDTO);

    // Map Category entity to CategoryDTO
    @Mapping(target = "parentCategoryId", source = "parentCategory.categoryId")  // Map parent category's ID to DTO
    @Mapping(target = "products", source = "products")  // Map list of products to ProductDTOs
    @Mapping(target = "subcategories", source = "subcategories")  // Map subcategories from entity to DTO
    CategoryDTO categoryToDTO(Category category);

    // Update Category entity from CategoryDTO without affecting relationships
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "parentCategory", ignore = true)  // Ignore parent category, handle manually in service
    @Mapping(target = "products", ignore = true)  // Ignore products, handle manually in service
    @Mapping(target = "subcategories", ignore = true)  // Ignore subcategories, handle manually in service
    void updateCategoryFromDTO(CategoryDTO categoryDTO, @MappingTarget Category category);
}
