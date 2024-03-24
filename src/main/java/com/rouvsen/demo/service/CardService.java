package com.rouvsen.demo.service;

import com.rouvsen.demo.client.CashbackClient;
import com.rouvsen.demo.dao.CardDAO;
import com.rouvsen.demo.dao.TransactionDAO;
import com.rouvsen.demo.model.Card;
import com.rouvsen.demo.model.Transaction;
import com.rouvsen.demo.model.constant.Type;
import com.rouvsen.demo.model.dto.CardRequestDto;
import com.rouvsen.demo.model.dto.CardResponseDto;
import com.rouvsen.demo.model.dto.CashbackResponse;
import com.rouvsen.demo.model.dto.TransactionRequestDto;
import com.rouvsen.demo.model.exception.CardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardDAO cardRepository;
    private final TransactionDAO transactionRepository;
    private final ModelMapper modelMapper;
    private final CashbackClient cashbackClient;

    public List<CardResponseDto> getAll() {
        List<CardResponseDto> dtoCards = new ArrayList<>();
        for(Card card : cardRepository.findAll()) {
            dtoCards.add(modelMapper.map(card, CardResponseDto.class));
        }
        return dtoCards;
    }

    public CardResponseDto get(Integer id) {
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException("Card Not Found with given id: %d".formatted(id))
        );
        card.setTransactions(new ArrayList<>());
        return modelMapper.map(card, CardResponseDto.class);
    }

    public ResponseEntity<CardResponseDto> create(CardRequestDto dto) {
        Card card = modelMapper.map(dto, Card.class);
        card.setBalance(0d);
        card.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(
                modelMapper.map(cardRepository.save(card), CardResponseDto.class)
        );
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            cardRepository.findById(id).orElseThrow(
                    () -> new CardNotFoundException("Card Not Found with given id: %d".formatted(id))
            );
        } catch (CardNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
        cardRepository.deleteById(id);
        return ResponseEntity.ok("Deleted by id: %d".formatted(id));
    }

    public ResponseEntity<?> createTransaction(TransactionRequestDto transactionRequestDto, Integer cardId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        try {
            if (cardOptional.isEmpty()) {
                throw new CardNotFoundException("Card Not Found with given id: %d".formatted(cardId));
            }
        } catch (CardNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        Transaction transaction = Transaction.builder()
                .amount(transactionRequestDto.amount())
                .type(
                        transactionRequestDto.type().equals("CREDIT") ? Type.CREDIT : Type.DEBIT
                )
                .hasCashback(transactionRequestDto.hasCashback())
                .card(cardOptional.get())
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
        return ResponseEntity.ok(transaction);
    }

    public void processCashbackForTransactions() {
        List<Transaction> transactionsWithCashback = transactionRepository.findByHasCashback(true);

        for (Transaction transaction : transactionsWithCashback) {
            CashbackResponse cashbackResponse = cashbackClient.getCashbackAmount(transaction.getAmount());
            System.out.println("Cashback amount: " + cashbackResponse);

            Card card = transaction.getCard();
            card.setBalance(card.getBalance() + (cashbackResponse == null ? 0d : cashbackResponse.getCashbackAmount()));
            cardRepository.save(card);

            Transaction cashbackTransaction = new Transaction();
            cashbackTransaction.setAmount(cashbackResponse == null ? 0d : cashbackResponse.getCashbackAmount());
            cashbackTransaction.setCard(card);
            cashbackTransaction.setType(Type.CASHBACK);
            cashbackTransaction.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(cashbackTransaction);
        }
    }

}
