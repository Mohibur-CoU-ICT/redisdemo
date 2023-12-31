package com.mohibur.redisdemo.controller;

import com.mohibur.redisdemo.entity.Product;
import com.mohibur.redisdemo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("unused")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        Product createdProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("unused")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("unused")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Product product = productService.findById(id);
        logger.info(String.valueOf(product));
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested product with ID " + id + " was not found.");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("unused")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody Product product) {
        Product product1 = productService.update(id, product);
        if (product1 != null) {
            return ResponseEntity.ok("Product updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested product with ID " + id + " was not found.");
    }

    @DeleteMapping(value = "/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<String> deleteById(@PathVariable("id") int id) {
        boolean deleted = productService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested product with ID " + id + " was not found.");
    }

}
