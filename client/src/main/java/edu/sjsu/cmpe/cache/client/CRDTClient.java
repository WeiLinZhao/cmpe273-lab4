package edu.sjsu.cmpe.cache.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import java.util.*;
import java.util.concurrent.*;
import java.util.Map.Entry;

public class CRDTClient {
	public CRDTClient(List<CacheServiceInterface> caches) {
		caches_ = caches;
	}

    public void put(Long key, String value) {
    	System.out.println("put(" + Long.toString(key) +  " => " + value + ")");
    	List<Future<HttpResponse<JsonNode>>> futures = new ArrayList();
    	for (CacheServiceInterface cache : caches_) {
			futures.add(cache.put(key, value));
    	}

    	final int maxNumFailedRequests = 1;
    	int numFailedRequests = 0;
		for (Future<HttpResponse<JsonNode>> f : futures) {
			while (!f.isDone()); // block until http call is complete.
			try {
				final HttpResponse<JsonNode> response = f.get();
				if (response.getCode() != 200 && ++numFailedRequests > maxNumFailedRequests) {
					System.out.println("Too many failures, deleting key from all servers.");
					for (CacheServiceInterface cache : caches_) {
						cache.delete(key);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
    }

    public String get(Long key) {
    	System.out.println("get(" + Long.toString(key) + ")");
    	List<Future<HttpResponse<JsonNode>>> futures = new ArrayList();
    	for (CacheServiceInterface cache : caches_) {
			futures.add(cache.get(key));
    	}

    	Map<String, Integer> valueToServerCountMap = new HashMap<String, Integer>();
		for (Future<HttpResponse<JsonNode>> f : futures) {
			while (!f.isDone()); // block until http call is complete.
			try {
				final HttpResponse<JsonNode> response = f.get();
				String value = response.getBody().getObject().getString("value");
				Integer count = (Integer)(valueToServerCountMap.containsKey(value) ? valueToServerCountMap.get(value) : 0);
				valueToServerCountMap.put(value, count + 1);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		final int minServerAgrees = 2;
		for (Entry<String, Integer> entry : valueToServerCountMap.entrySet()) {
		    String value = entry.getKey();
		    Integer count = entry.getValue();
		    if (count >= minServerAgrees) {
		    	// Update all servers with the correct value. If an update fails,
		    	// don't delete all other server values.
	    		for (CacheServiceInterface cache : caches_) {
					cache.put(key, value);
    			}
				// Return the first value that meets our requirement for number of server agrees.
			    return value;
		    }
		}
		System.out.println("No acceptable values found, returning default.");
		// Default empty value;
    	return "";
    }
    private List<CacheServiceInterface> caches_;
}