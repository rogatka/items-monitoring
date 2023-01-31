package com.items.parsing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    private Long id;

    private String name;

    private String released;

    private Integer metacritic;
}
