package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.model.Bill;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.BillStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BillMapperTest {

    private final BillMapper mapper = BillMapper.INSTANCE;

    @Test
    void toBillDTO_ShouldMapBillToDTO() {
        User user = new User();
        user.setId(1L);

        Bill bill = Bill.builder()
                .id(1L)
                .user(user)
                .biller("Electric Company")
                .amount(BigDecimal.valueOf(200))
                .dueDate(LocalDate.of(2023, 12, 31))
                .status(BillStatus.UNPAID)
                .build();

        BillDTO dto = mapper.toBillDTO(bill);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getUserId());
        assertEquals("Electric Company", dto.getBiller());
        assertEquals(BigDecimal.valueOf(200), dto.getAmount());
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), dto.getDueDate());
        assertEquals("UNPAID", dto.getStatus());
    }

    @Test
    void toBill_ShouldMapDTOToBill() {
        BillDTO dto = BillDTO.builder()
                .id(1L)
                .userId(1L)
                .biller("Electric Company")
                .amount(BigDecimal.valueOf(200))
                .dueDate(LocalDate.of(2023, 12, 31))
                .status("UNPAID")
                .build();

        Bill bill = mapper.toBill(dto);

        assertNotNull(bill);
        assertEquals(1L, bill.getId());
        assertNotNull(bill.getUser());
        assertEquals(1L, bill.getUser().getId());
        assertEquals("Electric Company", bill.getBiller());
        assertEquals(BigDecimal.valueOf(200), bill.getAmount());
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), bill.getDueDate());
        assertEquals(BillStatus.UNPAID, bill.getStatus());
    }

    @Test
    void toBillDTO_ShouldHandleNullBill() {
        BillDTO dto = mapper.toBillDTO(null);
        assertNull(dto);
    }

    @Test
    void toBill_ShouldHandleNullDTO() {
        Bill bill = mapper.toBill(null);
        assertNull(bill);
    }
}