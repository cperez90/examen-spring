package org.example.apiexam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BocataCreateDto {
    private String name;
    private double price;
    private Long breadId;
}
