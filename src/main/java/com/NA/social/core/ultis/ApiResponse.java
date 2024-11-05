package com.NA.social.core.ultis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse {

    private int code;
    private List<String> messages = new ArrayList<>();

    private Object data;
}
