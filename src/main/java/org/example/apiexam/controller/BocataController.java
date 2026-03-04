package org.example.apiexam.controller;

import lombok.RequiredArgsConstructor;
import org.example.apiexam.dto.BocataCreateDto;
import org.example.apiexam.dto.BocataDto;
import org.example.apiexam.service.BocataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bocata")
public class BocataController {

    public final BocataService bocataService;

    @GetMapping()
    public ResponseEntity<List<BocataDto>> showAllBocatas(){
        return  ResponseEntity.ok(bocataService.getAllBocatas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BocataDto> showBocataById(@PathVariable Long id){

        return ResponseEntity.ok(bocataService.getBocataById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('PROPIETARI')")
    public ResponseEntity<BocataDto> createBocata(@RequestBody BocataCreateDto dto){

        return ResponseEntity.ok(bocataService.create(dto));

    }
}
