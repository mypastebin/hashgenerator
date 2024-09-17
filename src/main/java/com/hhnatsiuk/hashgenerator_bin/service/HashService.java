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

    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${hash.prepared.count}")
    private int preparedHashCount;

    @Value("${redis.queue.name}")
    private String redisQueueName;

    public String generateUniqueHash() {
        String hash = redisTemplate.opsForList().leftPop(redisQueueName);
        if (hash == null) {
            logger.info("No pre-generated hashes found, generating new ones.");
            for (int i = 0; i < preparedHashCount; i++) {
                Sequence sequence = new Sequence();
                sequenceRepository.save(sequence);
                hash = generateHash(sequence.getId());
                redisTemplate.opsForList().rightPush(redisQueueName, hash);
            }
            hash = redisTemplate.opsForList().leftPop(redisQueueName);
        }
        logger.info("Returning hash: {}", hash);
        return hash;
    }

    private String generateHash(long id) {
        logger.debug("Generating hash for ID: {}", id);
        byte[] idBytes = ByteBuffer.allocate(Long.BYTES).putLong(id).array();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(idBytes);
    }
}
