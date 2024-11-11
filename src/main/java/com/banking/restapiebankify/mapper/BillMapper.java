package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {

    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    @Mapping(source = "user.id", target = "userId")
    BillDTO toBillDTO(Bill bill);

    @Mapping(target = "user.id", source = "userId")
    Bill toBill(BillDTO billDTO);
}
