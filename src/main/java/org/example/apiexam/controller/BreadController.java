package org.example.apiexam.controller;

import lombok.RequiredArgsConstructor;
import org.example.apiexam.dto.BreadDto;
import org.example.apiexam.service.BreadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bread")
public class BreadController {

    public  final BreadService breadService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('CUINER', 'PROPIETARI')")
    public ResponseEntity<List<BreadDto>> showAllBread(){

        return ResponseEntity.ok(breadService.getAllBreads());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUINER','PROPIETARI')")
    public ResponseEntity<BreadDto> showBreadById(@PathVariable Long id){

        return  ResponseEntity.ok(breadService.getBreadById(id));
    }
}
