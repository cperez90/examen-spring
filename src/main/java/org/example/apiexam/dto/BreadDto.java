package org.example.apiexam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BreadDto {

    private Long id;
    private String name;
    private boolean glutenFree;
}
