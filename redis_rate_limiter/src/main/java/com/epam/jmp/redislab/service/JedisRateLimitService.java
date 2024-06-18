package com.epam.jmp.redislab.service;

import com.epam.jmp.redislab.api.RequestDescriptor;
import com.epam.jmp.redislab.configuration.ratelimit.RateLimitRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.time.LocalTime;
import java.util.*;

@Component
public class JedisRateLimitService implements RateLimitService{

    private final Set<RateLimitRule> rateLimitRules;

    private final JedisCluster jedis;

    @Autowired
    public JedisRateLimitService(Set<RateLimitRule> rateLimitRules, JedisCluster jedis) {
        this.rateLimitRules = rateLimitRules;
        this.jedis = jedis;
    }

    @Override
    public boolean shouldLimit(Set<RequestDescriptor> requestDescriptors) {
        for (RequestDescriptor descriptor : requestDescriptors) {

            String accountId = descriptor.getAccountId().orElse(null);
            String clientIp = descriptor.getClientIp().orElse(null);
            String requestType = descriptor.getRequestType().orElse(null);

            RateLimitRule rule = getMatchedRule(accountId, clientIp, requestType);
            if(rule == null) continue;

            String key = generateKey(rule, descriptor);
            long ttl = rule.getExpiration();
            long value = jedis.incr(key);
            if (value == 1)
                jedis.expire(key, ttl);
            if (value > rule.getAllowedNumberOfRequests())
                return true;
        }
        return false;
    }


    private RateLimitRule getMatchedRule(String accountId, String clientIp, String requestType) {

        RateLimitRule result = null;
        for (RateLimitRule rule : rateLimitRules) {
            String ruleAccountId = rule.getAccountId().orElse(null);
            String ruleClientIp = rule.getClientIp().orElse(null);
            String ruleRequestType = rule.getRequestType().orElse(null);

            if (matchBy(ruleAccountId, accountId) &&
                    matchBy(ruleClientIp, clientIp) &&
                    matchBy(ruleRequestType, requestType)
            ) {
                if (result != null &&
                        ((ruleAccountId != null && ruleAccountId.isEmpty()) ||
                        (ruleClientIp != null && ruleClientIp.isEmpty())))
                    continue;

                result = rule;
            }
        }

        return result;
    }

    private boolean matchBy(String ruleEntity, String descriptorEntity) {
        return (ruleEntity == null && descriptorEntity == null) ||
                (ruleEntity != null && ruleEntity.isEmpty() && descriptorEntity != null) ||
                (ruleEntity != null && ruleEntity.equals(descriptorEntity));
    }

    private String generateKey(RateLimitRule rule, RequestDescriptor descriptor) {
        return descriptor.toString() + LocalTime.now().truncatedTo(rule.getTimeInterval().toChronoUnit());
    }
}
