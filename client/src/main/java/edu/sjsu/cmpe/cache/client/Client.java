package edu.sjsu.cmpe.cache.client;

import java.util.*;

public class Client {
    private static List<CacheServiceInterface> InitializeCaches() {
        CacheServiceInterface cache1 = new DistributedCacheService("http://localhost:3000");
        CacheServiceInterface cache2 = new DistributedCacheService("http://localhost:3001");
        CacheServiceInterface cache3 = new DistributedCacheService("http://localhost:3002");
        return Arrays.asList(cache1, cache2, cache3);
    }

    public static void main(String[] args) throws Exception {
        CRDTClient CrdtClient = new CRDTClient(InitializeCaches());

        // Step 1
        CrdtClient.put(key_, "a");
        System.out.println("Sleeping after step 1");
        Thread.sleep(thirySecondSleepTime_);
        // Step 2
        CrdtClient.put(key_, "b");
        System.out.println("Sleeping after step 2");
        Thread.sleep(thirySecondSleepTime_);
        // Step 3
        System.out.println("Cache return value:" + CrdtClient.get(key_));
    }

    private static final int thirySecondSleepTime_ = 30000;
    private static final Long key_ = 1L;
}
