package edu.sjsu.cmpe.cache.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.async.*;
import java.util.concurrent.*;
import java.util.List;

/**
 * Distributed cache service
 * 
 */
public class DistributedCacheService implements CacheServiceInterface {
    private final String cacheServerUrl;

    public DistributedCacheService(String serverUrl) {
        this.cacheServerUrl = serverUrl;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */
    @Override
    public Future<HttpResponse<JsonNode>> get(long key) {
        return Unirest
            .get(this.cacheServerUrl + "/cache/{key}")
            .header("accept", "application/json")
            .routeParam("key", Long.toString(key))
            .asJsonAsync();
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#put(long,
     *      java.lang.String, java.util.List)
     */
    @Override
    public Future<HttpResponse<JsonNode>> put(long key, String value) {
        return Unirest
            .put(this.cacheServerUrl + "/cache/{key}/{value}")
            .header("accept", "application/json")
            .routeParam("key", Long.toString(key))
            .routeParam("value", value)
            .asJsonAsync();
    }
    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#delete(long)
     */
    @Override
    public void delete(long key) {
        Unirest
            .delete(this.cacheServerUrl + "/cache/{key}")
            .header("accept", "application/json")
            .routeParam("key", Long.toString(key))
            .asJsonAsync();
    }
}
