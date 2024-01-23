package com.wevel.wevel_server.deepapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslationRequest {
    private List<String> text;
    private String target_lang;
}
