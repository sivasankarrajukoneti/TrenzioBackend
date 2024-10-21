package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.ProductDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.ProductMapper;
import com.ecommerce.trenzio.model.Category;
import com.ecommerce.trenzio.model.Product;
import com.ecommerce.trenzio.model.User;
import com.ecommerce.trenzio.repository.CategoryRepository;
import com.ecommerce.trenzio.repository.ProductRepository;
import com.ecommerce.trenzio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService{


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        // If no products are found, throw an exception
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found.");
        }

        return products.stream()
                .map(productMapper::productToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product is not found"));
        return productMapper.productToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creating a new user with username: {}", productDTO.getProductName());
        Product product = productMapper.dtoToProduct(productDTO);
        Product savedProduct =  productRepository.save(product);
        log.debug("User saved with ID: {}", savedProduct.getProductId());
        return productMapper.productToDTO(savedProduct);
    }

    @Transactional
    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // 1. Retrieve existing product by ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // 2. Use MapStruct to update fields that are not part of relationships
        productMapper.updateProductFromDTO(productDTO, existingProduct);

        // 3. Manually handle Category association if categoryId is present
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
            existingProduct.setCategory(category);
        }

        // 4. Manually handle User (seller) association if sellerId is present
        if (productDTO.getSellerId() != null) {
            User seller = userRepository.findById(productDTO.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User (Seller) not found with ID: " + productDTO.getSellerId()));
            existingProduct.setUser(seller);
        }

        // Explicitly set the updatedAt field manually
        existingProduct.setUpdatedAt(LocalDateTime.now());

        // 5. Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // 6. Convert the updated product back to ProductDTO
        return productMapper.productToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
