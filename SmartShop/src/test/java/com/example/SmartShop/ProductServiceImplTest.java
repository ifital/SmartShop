package com.example.SmartShop;

import com.example.SmartShop.dto.product.ProductCreateDTO;
import com.example.SmartShop.dto.product.ProductDTO;
import com.example.SmartShop.dto.product.ProductUpdateDTO;
import com.example.SmartShop.entity.Product;
import com.example.SmartShop.exception.ProductAlreadyExistsException;
import com.example.SmartShop.exception.ProductNotFoundException;
import com.example.SmartShop.mapper.ProductMapper;
import com.example.SmartShop.repository.OrderItemRepository;
import com.example.SmartShop.repository.ProductRepository;
import com.example.SmartShop.service.impl.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------------------------------------
    // CREATE TESTS
    // ----------------------------------------------
    @Test
    void testCreate_ShouldThrow_WhenProductAlreadyExists() {
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("PC Portable");

        when(productRepository.existsByNameAndDeletedFalse("PC Portable"))
                .thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class,
                () -> productService.create(dto));
    }

    @Test
    void testCreate_ShouldCreateProductSuccessfully() {
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("PC Portable");
        dto.setStock(10);
        dto.setUnitPrice(BigDecimal.valueOf(5000));

        Product product = Product.builder()
                .name("PC Portable")
                .unitPrice(BigDecimal.valueOf(5000))
                .stock(10)
                .deleted(false)
                .build();

        Product saved = Product.builder()
                .id("1")
                .name("PC Portable")
                .unitPrice(BigDecimal.valueOf(5000))
                .stock(10)
                .deleted(false)
                .build();

        ProductDTO returned = new ProductDTO();
        returned.setId("1");
        returned.setName("PC Portable");

        when(productRepository.existsByNameAndDeletedFalse("PC Portable")).thenReturn(false);
        when(productMapper.toEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(saved);
        when(productMapper.toDTO(saved)).thenReturn(returned);

        ProductDTO result = productService.create(dto);

        assertEquals("1", result.getId());
        assertEquals("PC Portable", result.getName());
    }

    // ----------------------------------------------
    // UPDATE TESTS
    // ----------------------------------------------
    @Test
    void testUpdate_ShouldThrow_WhenProductNotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        ProductUpdateDTO dto = new ProductUpdateDTO();
        dto.setName("Nouveau Nom");

        assertThrows(ProductNotFoundException.class,
                () -> productService.update("1", dto));
    }

    @Test
    void testUpdate_ShouldUpdateSuccessfully() {
        Product product = Product.builder()
                .id("1")
                .name("Ancien Nom")
                .stock(5)
                .deleted(false)
                .build();

        ProductUpdateDTO dto = new ProductUpdateDTO();
        dto.setName("Nouveau Nom");

        Product updated = Product.builder()
                .id("1")
                .name("Nouveau Nom")
                .stock(5)
                .deleted(false)
                .build();

        ProductDTO returned = new ProductDTO();
        returned.setId("1");
        returned.setName("Nouveau Nom");

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        doAnswer(invocation -> {
            product.setName("Nouveau Nom");
            return null;
        }).when(productMapper).updateEntityFromDTO(dto, product);

        when(productRepository.save(product)).thenReturn(updated);
        when(productMapper.toDTO(updated)).thenReturn(returned);

        ProductDTO result = productService.update("1", dto);

        assertEquals("1", result.getId());
        assertEquals("Nouveau Nom", result.getName());
    }

    // ----------------------------------------------
    // GET BY ID TESTS
    // ----------------------------------------------
    @Test
    void testGetById_ShouldThrow_WhenProductNotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.getById("1"));
    }

    @Test
    void testGetById_ShouldReturnProductSuccessfully() {
        Product product = Product.builder()
                .id("1")
                .name("PC Portable")
                .deleted(false)
                .build();

        ProductDTO dto = new ProductDTO();
        dto.setId("1");
        dto.setName("PC Portable");

        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(dto);

        ProductDTO result = productService.getById("1");

        assertEquals("1", result.getId());
        assertEquals("PC Portable", result.getName());
    }

    // ----------------------------------------------
    // GET ALL TESTS
    // ----------------------------------------------
    @Test
    void testGetAll_ShouldReturnPagedProducts() {
        PageRequest pageable = PageRequest.of(0, 2);

        Product p1 = Product.builder().id("1").name("PC").deleted(false).build();
        Product p2 = Product.builder().id("2").name("Imprimante").deleted(false).build();

        ProductDTO dto1 = new ProductDTO();
        dto1.setId("1");
        dto1.setName("PC");

        ProductDTO dto2 = new ProductDTO();
        dto2.setId("2");
        dto2.setName("Imprimante");

        Page<Product> productPage = new PageImpl<>(Arrays.asList(p1, p2));

        when(productRepository.findAllByDeletedFalse(pageable)).thenReturn(productPage);
        when(productMapper.toDTO(p1)).thenReturn(dto1);
        when(productMapper.toDTO(p2)).thenReturn(dto2);

        Page<ProductDTO> result = productService.getAll(pageable);

        assertEquals(2, result.getTotalElements());
    }

    // ----------------------------------------------
    // DELETE TESTS
    // ----------------------------------------------
    @Test
    void testDelete_ShouldThrow_WhenProductNotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.delete("1"));
    }

    @Test
    void testDelete_ShouldSoftDelete_WhenProductHasOrders() {
        Product product = Product.builder()
                .id("1")
                .deleted(false)
                .build();

        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(orderItemRepository.existsByProductId("1")).thenReturn(true);

        productService.delete("1");

        assertTrue(product.isDeleted());
        verify(productRepository).save(product);
        verify(productRepository, never()).delete(product);
    }

    @Test
    void testDelete_ShouldHardDelete_WhenProductHasNoOrders() {
        Product product = Product.builder()
                .id("1")
                .deleted(false)
                .build();

        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(orderItemRepository.existsByProductId("1")).thenReturn(false);

        productService.delete("1");

        verify(productRepository).delete(product);
        verify(productRepository, never()).save(product);
    }
}
