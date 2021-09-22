package com.redhat.training.service;

import com.redhat.training.model.Incident;
import io.quarkus.runtime.util.StringUtil;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IncidentSearchService {

    @Inject
    private RestClient restClient;

    public List<Incident> searchById(Long id) throws IOException {
        return search("id", id.toString());
    }

    public List<Incident> searchByRequesterName(String requesterName) throws IOException {
        return search("requesterName", requesterName);
    }

    public List<Incident> searchAll() throws IOException {
        return search("", null);
    }

    private List<Incident> search(String term, String match) throws IOException {
        Request request = new Request("GET", "/incidents/_search");

        if (match != null) {
            JsonObject termJson = new JsonObject().put(term, match);
            JsonObject matchJson = new JsonObject().put("match", termJson);
            JsonObject queryJson = new JsonObject().put("query", matchJson);
            request.setJsonEntity(queryJson.encode());
        }

        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");

        List<Incident> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            Incident incident = hit.getJsonObject("_source").mapTo(Incident.class);
            results.add(incident);
        }
        return results;
    }
}
