package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.product.ProductCreateDTO;
import com.example.SmartShop.dto.product.ProductDTO;
import com.example.SmartShop.dto.product.ProductUpdateDTO;
import com.example.SmartShop.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T16:04:27+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setId( product.getId() );
        productDTO.setName( product.getName() );
        productDTO.setUnitPrice( product.getUnitPrice() );
        productDTO.setStock( product.getStock() );
        productDTO.setDeleted( product.isDeleted() );

        return productDTO;
    }

    @Override
    public Product toEntity(ProductCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( dto.getName() );
        product.unitPrice( dto.getUnitPrice() );
        if ( dto.getStock() != null ) {
            product.stock( dto.getStock() );
        }

        return product.build();
    }

    @Override
    public void updateEntityFromDTO(ProductUpdateDTO dto, Product product) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getName() != null ) {
            product.setName( dto.getName() );
        }
        if ( dto.getUnitPrice() != null ) {
            product.setUnitPrice( dto.getUnitPrice() );
        }
        if ( dto.getStock() != null ) {
            product.setStock( dto.getStock() );
        }
    }
}
