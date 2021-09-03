package com.learnjava.completablefuture;

import com.learnjava.domain.*;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {

    private ProductInfoService productInfoService;
    private ReviewService reviewService;
    private InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetailsJoin(String productId) {
        startTimer();

        CompletableFuture<ProductInfo> productInfoCf = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCf = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCf
                .thenCombine(reviewCf, (productInfo, review) -> new Product(productId, productInfo, review))
                .join();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());

        return product;
    }

    public CompletableFuture<Product> retrieveProductDetailsAsync(String productId) {
        startTimer();

        CompletableFuture<ProductInfo> productInfoCf = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCf = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());

        return productInfoCf
                .thenCombine(reviewCf, (productInfo, review) -> new Product(productId, productInfo, review));
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        startTimer();

        CompletableFuture<ProductInfo> productInfoCf =
                CompletableFuture
                        .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                        .thenApply(productInfo -> {
                            productInfo.setProductOptions(updateInventory(productInfo));
                            return productInfo;
                        });

        CompletableFuture<Review> reviewCf = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCf
                .thenCombine(reviewCf, (productInfo, review) -> new Product(productId, productInfo, review))
                .join();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());

        return product;
    }

    public Product retrieveProductDetailsWithInventoryAsync(String productId) {
        startTimer();

        CompletableFuture<ProductInfo> productInfoCf =
                CompletableFuture
                        .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                        .thenApply(productInfo -> {
                            productInfo.setProductOptions(updateInventoryAsync(productInfo));
                            return productInfo;
                        });

        CompletableFuture<Review> reviewCf = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally(ex -> {
                    log("Exception in ReviewService == " + ex.getMessage());
                    return Review.builder()
                            .noOfReviews(0)
                            .overallRating(0.0)
                            .build();
                });

        Product product = productInfoCf
                .thenCombine(reviewCf, (productInfo, review) -> new Product(productId, productInfo, review))
                .whenComplete((prod, ex) -> {
                    log("Inside whenComplete == " + prod + " and the exception == " + ex);
                })
                .join();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());

        return product;
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        List<ProductOption> optionList = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    Inventory inventory = inventoryService.addInventory(productOption);
                    productOption.setInventory(inventory);
                    return productOption;
                }).collect(Collectors.toList());

        return optionList;
    }

    private List<ProductOption> updateInventoryAsync(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> optionList = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    return CompletableFuture.supplyAsync(() -> inventoryService.addInventory(productOption))
                            .exceptionally(ex -> {
                                log("Exception in updateInventoryAsync() - Returning Default Inventory");
                                return Inventory.builder()
                                        .count(1)
                                        .build();
                            })
                            .thenApply(inventory -> {
                                productOption.setInventory(inventory);
                                return productOption;
                            });
                }).collect(Collectors.toList());

        return optionList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetailsJoin(productId);
        log("Product is " + product);

    }
}
