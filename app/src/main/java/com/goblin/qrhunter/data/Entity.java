package com.goblin.qrhunter.data;

import java.util.Map;

public interface Entity {
    public String getId();

    public void setId(String id);

    public Map<String, Object> toMap();

}
