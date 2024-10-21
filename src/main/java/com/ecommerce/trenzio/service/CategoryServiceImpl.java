package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.CategoryDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.CategoryMapper;
import com.ecommerce.trenzio.mapper.ProductMapper;
import com.ecommerce.trenzio.model.Category;
import com.ecommerce.trenzio.model.Product;
import com.ecommerce.trenzio.repository.CategoryRepository;
import com.ecommerce.trenzio.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }



    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();

        if(categoryList.isEmpty()){
            throw new ResourceNotFoundException("No Categories Found");
        }
        return categoryList.stream().map(categoryMapper::categoryToDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category Not Found : "+ id));
        return categoryMapper.categoryToDTO(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Map CategoryDTO to Category entity using the mapper (relationships are handled manually)
        Category category = categoryMapper.dtoToCategory(categoryDTO);

        // Manually set the parent category if the parentCategoryId is provided
        if (categoryDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Category Not Found: " + categoryDTO.getParentCategoryId()));
            category.setParentCategory(parentCategory);
            log.debug("Parent Category set with ID: {}", parentCategory.getCategoryId());
        }

        // Handle subcategories manually if they are provided in the DTO
        if (categoryDTO.getSubcategories() != null && !categoryDTO.getSubcategories().isEmpty()) {
            category.getSubcategories().clear();
            categoryDTO.getSubcategories().forEach(subcategoryDTO -> {
                Category subcategory = categoryMapper.dtoToCategory(subcategoryDTO);
                subcategory.setParentCategory(category);  // Set parent reference
                category.getSubcategories().add(subcategory);
            });
        }

        // Handle products manually if they are provided in the DTO
        if (categoryDTO.getProducts() != null && !categoryDTO.getProducts().isEmpty()) {
            category.getProducts().clear();
            categoryDTO.getProducts().forEach(productDTO -> {
                Product product = productMapper.dtoToProduct(productDTO);
                product.setCategory(category);  // Set category reference
                category.getProducts().add(product);
            });
        }

        // Save the category entity to the database
        Category savedCategory = categoryRepository.save(category);

        // Convert the saved category entity back to a DTO and return
        return categoryMapper.categoryToDTO(savedCategory);
    }



    @Transactional
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        // 1. Retrieve the existing category by ID
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // 2. Update fields using MapStruct without affecting relationships
        categoryMapper.updateCategoryFromDTO(categoryDTO, existingCategory);

        // 3. Handle parentCategory manually if provided in the DTO
        if (categoryDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Category not found with ID: " + categoryDTO.getParentCategoryId()));
            existingCategory.setParentCategory(parentCategory);
        }

        // 4. Handle subcategories manually (clear and set new subcategories)
        if (categoryDTO.getSubcategories() != null && !categoryDTO.getSubcategories().isEmpty()) {
            // Clear existing subcategories and re-add based on DTO
            existingCategory.getSubcategories().clear();
            categoryDTO.getSubcategories().forEach(subcategoryDTO -> {
                Category subcategory = categoryMapper.dtoToCategory(subcategoryDTO);
                subcategory.setParentCategory(existingCategory);  // Set parent reference
                existingCategory.getSubcategories().add(subcategory);
            });
        }

        // 5. *Optional* Reassign products to the updated category
        if (categoryDTO.getProducts() != null) {
            // Clear existing products and reassign them to the new/updated category
            existingCategory.getProducts().clear();
            categoryDTO.getProducts().forEach(productDTO -> {
                Product product = productMapper.dtoToProduct(productDTO);
                product.setCategory(existingCategory);  // Reassign category
                existingCategory.getProducts().add(product);
            });
        }
        // 6. Update the `updatedAt` timestamp manually
        existingCategory.setUpdatedAt(LocalDateTime.now());

        // 7. Save the updated category to the database
        Category updatedCategory = categoryRepository.save(existingCategory);

        // 8. Convert and return the updated entity as a DTO
        return categoryMapper.categoryToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
