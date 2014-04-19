package com.cyber.kinoost.api.models;

import java.util.List;

public class JsonUpdateList {

    private List<JsonUpdate> updates;

    public List<JsonUpdate> getUpdates() {
        return updates;
    }

    public void setUpdates(List<JsonUpdate> updates) {
        this.updates = updates;
    }
}