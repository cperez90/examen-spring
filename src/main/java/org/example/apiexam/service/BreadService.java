package org.example.apiexam.service;

import lombok.RequiredArgsConstructor;
import org.example.apiexam.dto.BocataDto;
import org.example.apiexam.dto.BreadDto;
import org.example.apiexam.exception.ResourceException;
import org.example.apiexam.model.Bread;
import org.example.apiexam.repository.BreadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BreadService {

    private final BreadRepository breadRepository;

    public List<BreadDto> getAllBreads(){
        return breadRepository.findAll().stream().map(b -> new BreadDto(b.getId(), b.getName() , b.isGlutenFree())).toList();
    }

    public BreadDto getBreadById(Long id){
        Bread bread = breadRepository.findById(id).orElseThrow(() -> new ResourceException("Bread Not Fount"));
        return  new BreadDto(bread.getId(),bread.getName(), bread.isGlutenFree());
    }
}
