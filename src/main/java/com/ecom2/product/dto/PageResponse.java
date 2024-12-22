package com.ecom2.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> implements Serializable {
    private int size;
    private int page;
    private int totalPages;
    private T items;
}