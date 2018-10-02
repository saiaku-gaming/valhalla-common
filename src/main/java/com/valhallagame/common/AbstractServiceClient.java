package com.valhallagame.common;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class AbstractServiceClient {

    protected RestCaller restCaller = new RestCaller();

    protected String serviceServerUrl;

    public RestResponse<JsonNode> ping() throws IOException {
        return this.restCaller.getCall(serviceServerUrl + "/", JsonNode.class);
    }
}
