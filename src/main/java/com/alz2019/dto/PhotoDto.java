package com.alz2019.dto;

import lombok.Builder;

import java.net.URI;

@Builder
public record PhotoDto(URI uri, long size) {
}
