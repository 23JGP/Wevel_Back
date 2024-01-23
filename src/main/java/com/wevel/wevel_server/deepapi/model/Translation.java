package com.wevel.wevel_server.deepapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Translation {
    private String detected_source_language;
    private String text;
}
