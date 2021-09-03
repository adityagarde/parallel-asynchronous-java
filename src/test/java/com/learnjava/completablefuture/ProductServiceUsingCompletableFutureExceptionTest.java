package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUsingCompletableFutureExceptionTest {

    @Mock
    private ProductInfoService productInfoService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    ProductServiceUsingCompletableFuture productServiceCf;

    @Test
    void retrieveProductDetailsWithInventoryAsync() {

        String productId = "ABCD1234";

        when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
        when(reviewService.retrieveReviews(any())).thenThrow(new RuntimeException("Exception Occurred!"));
        when(inventoryService.addInventory(any())).thenCallRealMethod();

        Product product = productServiceCf.retrieveProductDetailsWithInventoryAsync(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);

        product.getProductInfo().getProductOptions()
                .forEach(productOption -> {
                    assertNotNull(productOption.getInventory());
                });

        assertNotNull(product.getReview());
        // Checking Number of reviews as 0, because when the retrieveService is set to throw exception
        // then the exceptionally block in our pipeline is tripped and sets it to 0.
        assertEquals(0, product.getReview().getNoOfReviews());
    }

    @Test
    void retrieveProductDetailsWithInventoryAsync2() {

        String productId = "ABCD1234";

        when(productInfoService.retrieveProductInfo(any())).thenThrow(new RuntimeException("Exception Occurred!"));
        when(reviewService.retrieveReviews(any())).thenCallRealMethod();

        Assertions.assertThrows(RuntimeException.class, () -> productServiceCf.retrieveProductDetailsWithInventoryAsync(productId));
    }
}