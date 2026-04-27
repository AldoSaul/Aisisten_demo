package com.leads.integration.dto;

public record ConnectedChannelDto(
    Long connectionId,
    String provider,
    String channel,
    String externalId,
    String displayName,
    boolean active
) {
}

