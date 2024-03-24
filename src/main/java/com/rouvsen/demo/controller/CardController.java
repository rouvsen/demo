package com.rouvsen.demo.controller;

import com.rouvsen.demo.model.dto.CardRequestDto;
import com.rouvsen.demo.model.dto.CardResponseDto;
import com.rouvsen.demo.model.dto.TransactionRequestDto;
import com.rouvsen.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getAll(){
        return ResponseEntity.ok(cardService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> get(@PathVariable("id") Integer id){
        return ResponseEntity.ok(cardService.get(id));
    }

    @PostMapping
    public ResponseEntity<CardResponseDto> create(@RequestBody CardRequestDto dto) {
        return cardService.create(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        return cardService.delete(id);
    }

    @PostMapping("/{id}/transactions")
    public ResponseEntity<?> createTransaction(
            @RequestBody TransactionRequestDto dto,
            @PathVariable("id") Integer id
    ) {
        return cardService.createTransaction(dto, id);
    }

    @GetMapping("/setCashback")
    public void setCashBacks() {
        cardService.processCashbackForTransactions();
    }

}
