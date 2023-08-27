package com.mohibur.redisdemo.service;

import com.mohibur.redisdemo.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final String HASH_KEY = "Product";
    @Autowired
    private RedisTemplate<String, Object> template;

    public Product save(Product product) {
        template.opsForHash().put(HASH_KEY, product.getId(), product);
        return product;
    }

    public List<Product> findAll() {
//        return template.opsForHash().values(HASH_KEY);
        List<Object> objects = template.opsForHash().values(HASH_KEY);
        return objects.stream().map(this::convertToProduct).collect(Collectors.toList());
    }

    @Cacheable(key = "#id", value = "Product", unless = "#result == null or #result.price > 1000")
    public Product findById(int id) {
        logger.info("called findById() from DB");
//        return (Product) template.opsForHash().get(HASH_KEY, id);
        Object obj = template.opsForHash().get(HASH_KEY, id);
        if (obj != null) {
            return convertToProduct(obj);
        }
        return null;
    }

    @CachePut(key = "#id", value = "Product")
    public Product update(int id, Product product) {
        if (existById(id)) {
            template.opsForHash().put(HASH_KEY, id, product);
            return product;
        }
        return null;
    }

    @CacheEvict(key = "#id", value = "Product")
    public boolean deleteById(int id) {
        if (existById(id)) {
            template.opsForHash().delete(HASH_KEY, id);
            return true;
        }
        return false;
    }

    private boolean existById(int id) {
        return template.opsForHash().hasKey(HASH_KEY, id);
    }

    private Product convertToProduct(Object object) {
        if (object == null) {
            return null;
        }
        Product product = new Product();
        BeanUtils.copyProperties(object, product);
        return product;
    }
}
