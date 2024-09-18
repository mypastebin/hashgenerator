package com.hhnatsiuk.hashgenerator_bin.service;

import com.hhnatsiuk.hashgenerator_bin.entity.Sequence;
import com.hhnatsiuk.hashgenerator_bin.repository.SequenceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Base64;

@Service
public class HashService {

    private static final Logger logger = LogManager.getLogger(HashService.class);

    private final SequenceRepository sequenceRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${hash.prepared.count}")
    private int preparedHashCount;

    @Value("${redis.queue.name}")
    private String redisQueueName;

    @Value("${redis.queue.min.size}")
    private int minQueueSize;

    @Autowired
    public HashService(SequenceRepository sequenceRepository, RedisTemplate<String, String> redisTemplate) {
        this.sequenceRepository = sequenceRepository;
        this.redisTemplate = redisTemplate;
    }

    public String generateUniqueHash() {
        String hash = redisTemplate.opsForList().leftPop(redisQueueName);

        if (hash == null) {
            logger.info("No pre-generated hashes found, generating new ones.");
            refillHashQueue();
            hash = redisTemplate.opsForList().leftPop(redisQueueName);
        }

        logger.info("Returning hash: {}", hash);

        Long queueSize = redisTemplate.opsForList().size(redisQueueName);
        if (queueSize != null && queueSize < minQueueSize) {
            logger.info("Queue size is low ({}), refilling the queue.", queueSize);
            refillHashQueue();
        }

        logRedisContents();
        return hash;
    }

    private String generateHash(long id) {
        logger.debug("Generating hash for ID: {}", id);
        byte[] idBytes = ByteBuffer.allocate(Long.BYTES).putLong(id).array();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(idBytes);
    }

    private void refillHashQueue() {
        for (int i = 0; i < preparedHashCount; i++) {
            Sequence sequence = new Sequence();
            sequenceRepository.save(sequence);
            String hash = generateHash(sequence.getId());
            redisTemplate.opsForList().rightPush(redisQueueName, hash);
        }
    }

    private void logRedisContents() {
        Long queueSize = redisTemplate.opsForList().size(redisQueueName);
        logger.info("Queue size: {}", queueSize);

        if (queueSize != null && queueSize > 0) {
            for (int i = 0; i < queueSize; i++) {
                String queuedHash = redisTemplate.opsForList().index(redisQueueName, i);
                logger.info("Position {}: {}", i, queuedHash);
            }
        } else {
            logger.info("Redis queue is empty.");
        }
    }
}
