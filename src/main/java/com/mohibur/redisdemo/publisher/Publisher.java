package com.mohibur.redisdemo.publisher;

import com.mohibur.redisdemo.dto.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Publisher {

    private final Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic channelTopic;

    @PostMapping("/publish")
    public String publish(@RequestBody Product product) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), product.toString());
        return String.format("Published product : %s!!", product);
    }

}
