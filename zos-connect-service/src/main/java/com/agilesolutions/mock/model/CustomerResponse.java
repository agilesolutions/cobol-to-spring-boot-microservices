package com.agilesolutions.mock.model;

import lombok.Builder;

@Builder
public record CustomerResponse(String customerNumber, String customerType, String customerName) {
}
