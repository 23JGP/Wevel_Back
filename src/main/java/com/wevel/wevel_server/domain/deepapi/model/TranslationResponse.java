package com.wevel.wevel_server.domain.deepapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslationResponse {
    private List<Translation> translations;
}
