package io.github.luisbokor.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Null fields are omitted so that an empty payload can be sent. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthRequest(String username, String password) {}
