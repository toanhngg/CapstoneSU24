package fpt.CapstoneSU24.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RedisSortedSetService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Object> hashOperations;

   @PostConstruct
   private void init() {
       hashOperations = redisTemplate.opsForHash();
   }

    public void saveHash(String key,String field, String value) {
        hashOperations.put(key, field, value);
    }

    public Object getHash(String key,String field) {
        return hashOperations.get(key, field);
    }

    public void deleteHash(String key, String field) {
        hashOperations.delete(key, field);
    }
    // Thêm một phần tử vào sorted set
    public void addToSortedSet(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    // Lấy các phần tử từ sorted set trong khoảng điểm nhất định
    public Set<Object> getSortedSetRangeByScore(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    // Lấy các phần tử từ sorted set theo thứ tự tăng dần
    public Set<Object> getSortedSetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    // Xóa phần tử khỏi sorted set
    public void removeFromSortedSet(String key, Object value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    // Lấy thứ hạng của một phần tử trong sorted set
    public Long getRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    // Lấy số lượng phần tử trong sorted set
    public Long getSortedSetSize(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }
}