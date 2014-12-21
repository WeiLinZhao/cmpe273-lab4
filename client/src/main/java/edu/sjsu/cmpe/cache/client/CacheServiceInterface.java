package edu.sjsu.cmpe.cache.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import java.util.concurrent.Future;
import java.util.List;

/**
 * Cache Service Interface
 * 
 */
public interface CacheServiceInterface {
    public Future<HttpResponse<JsonNode>> get(long key);

    public Future<HttpResponse<JsonNode>> put(long key, String value);

    public void delete(long key);
}
