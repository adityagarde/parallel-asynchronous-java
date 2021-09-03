package com.learnjava.service;

import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.ProductOption;

import java.util.List;

import static com.learnjava.util.CommonUtil.delay;

public class ProductInfoService {

    public ProductInfo retrieveProductInfo(String productId) {
        delay(1000);
        List<ProductOption> productOptions = List.of(
                new ProductOption(1, "64GB", "Black", 699.99),
                new ProductOption(2, "128GB", "Black", 749.99),
                new ProductOption(3, "256GB", "Black", 1049.99),
                new ProductOption(4, "128GB", "Green", 749.99));
        return ProductInfo.builder().productId(productId)
                .productOptions(productOptions)
                .build();
    }
}