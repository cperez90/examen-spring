package org.example.apiexam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bocata {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Bread bread;

    public Bocata(String name, double price, Bread bread) {
        this.name = name;
        this.price = price;
        this.bread = bread;
    }
}
