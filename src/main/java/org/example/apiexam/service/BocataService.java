package org.example.apiexam.service;

import lombok.RequiredArgsConstructor;
import org.example.apiexam.dto.BocataCreateDto;
import org.example.apiexam.dto.BocataDto;
import org.example.apiexam.exception.ResourceException;
import org.example.apiexam.model.Bocata;
import org.example.apiexam.model.Bread;
import org.example.apiexam.repository.BocataRepository;
import org.example.apiexam.repository.BreadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BocataService {

    private final BocataRepository bocataRepository;
    private final BreadRepository breadRepository;

    public List<BocataDto> getAllBocatas(){
        return  bocataRepository.findAll().stream().map(b -> new BocataDto(b.getName() , b.getBread().getName())).toList();
    }

    public BocataDto getBocataById(Long id){
        Bocata bocata = bocataRepository.findById(id).orElseThrow(() -> new ResourceException("Bocata Not Fount"));
        return new BocataDto(bocata.getName(), bocata.getBread().getName());
    }

    public BocataDto create(BocataCreateDto dto){
        Bread bread = breadRepository.findById(dto.getBreadId()).orElseThrow(() -> new ResourceException("Bread Not Fount"));
        Bocata bocataNew = new Bocata(dto.getName(),dto.getPrice(),bread);
        bocataRepository.save(bocataNew);
        return new BocataDto(bocataNew.getName(),bocataNew.getBread().getName());
    }
}
