package com.bcy.gateway.utils;

import com.bcy.utils.BloomFilterUtils;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
@Component
@Slf4j
public class RedisUtils {


    @Autowired
    private StringRedisTemplate redisTemplate;

    //次数加1
    public void addKeyByTime(String key,int hours){
        //防止雪崩，加随机时间
        String value = redisTemplate.opsForValue().get(key);
        Random random = new Random();
        long second = hours * 3600L + random.nextInt(100);
        int cnt = 0;
        if(value != null){
            cnt = Integer.parseInt(value);
        }
        cnt ++;
        //存入redis
        redisTemplate.opsForValue().set(key,String.valueOf(cnt),second, TimeUnit.SECONDS);
    }

    //次数减1
    public void subKeyByTime(String key,int hours){
        //这里不会雪崩就不加随机时间了
        String value = redisTemplate.opsForValue().get(key);
        int cnt = 0;
        if(value != null){
            cnt = Integer.parseInt(value);
        }
        cnt --;
        //存入redis
        redisTemplate.opsForValue().set(key,String.valueOf(cnt),hours * 3600L,TimeUnit.SECONDS);
    }

    //存带有过期时间的key-value
    public void saveByHoursTime(String key,String value,int hours){
        //为防止缓存雪崩  加一个随机时间
        Random random = new Random();
        long second = hours * 3600L + random.nextInt(100);
        redisTemplate.opsForValue().set(key,value,second,TimeUnit.SECONDS);
    }

    public void saveByMinutesTime(String key,String value,int minutes){
        //为防止缓存雪崩  加一个随机时间
        Random random = new Random();
        long second = minutes * 60L + random.nextInt(25);
        redisTemplate.opsForValue().set(key,value,second,TimeUnit.SECONDS);
    }


    //重置时间
    public void resetExpire(String key,String value,int minutes){
        //为防止缓存雪崩，加一个随机时间
        Random random = new Random();
        long seconds = minutes * 60L + random.nextInt(25);
        redisTemplate.opsForValue().set(key,value,seconds,TimeUnit.SECONDS);
    }


    //判断key是否存在
    public boolean hasKey(String key){
        return redisTemplate.opsForValue().get(key) != null;
    }

    //删除key
    public void delete(String key){
        redisTemplate.delete(key);
    }

    //获取value
    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    //判断key是否在这个时间后
    public boolean isAfterDate(String key,int minutes){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS) > (long) minutes * 60;
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void addByBloomFilter(BloomFilterUtils<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            System.out.println("key : " + key + " " + "value : " + i);
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean includeByBloomFilter(BloomFilterUtils<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            System.out.println("key : " + key + " " + "value : " + i);
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }

    @Bean
    public BloomFilterUtils<String> initBloomFilterHelper() {
        return new BloomFilterUtils<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1000000, 0.01);
    }

}
