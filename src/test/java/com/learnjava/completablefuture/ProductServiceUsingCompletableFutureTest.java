package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    private ProductInfoService productInfoService = new ProductInfoService();
    private ReviewService reviewService = new ReviewService();
    private InventoryService inventoryService = new InventoryService();

    ProductServiceUsingCompletableFuture productServiceCf = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);

    @Test
    void retrieveProductDetailsJoin() {
        Product product = productServiceCf.retrieveProductDetailsJoin("ABCD1234");

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsAsync() {
        CompletableFuture<Product> productCf = productServiceCf.retrieveProductDetailsAsync("ABCD1234");

        productCf.thenAccept(product -> {
            assertNotNull(product);
            assertTrue(product.getProductInfo().getProductOptions().size() > 0);
            assertNotNull(product.getReview());
        }).join();

    }

    @Test
    void retrieveProductDetailsWithInventory() {

        Product product = productServiceCf.retrieveProductDetailsWithInventory("ABCD1234");

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());

        product.getProductInfo().getProductOptions()
                .forEach(productOption -> {
                    assertNotNull(productOption.getInventory());
                });

    }

    /**
     * retrieveProductDetailsWithInventory Test takes ~3024ms to process 4 items in inventory.
     * This is because for each item there is a delay of 500ms. If the list grows then the time grows as well.
     * <p>
     * Thus, in the retrieveProductDetailsWithInventoryAsync case (see updateInventoryAsync() method)
     * we are returning the List<CompletableFuture<ProductOption>> and processing it once only.
     * Thus the below test runs in ~2025ms only.
     */

    @Test
    void retrieveProductDetailsWithInventoryAsync() {

        Product product = productServiceCf.retrieveProductDetailsWithInventoryAsync("ABCD1234");

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());

        product.getProductInfo().getProductOptions()
                .forEach(productOption -> {
                    assertNotNull(productOption.getInventory());
                });

    }
}