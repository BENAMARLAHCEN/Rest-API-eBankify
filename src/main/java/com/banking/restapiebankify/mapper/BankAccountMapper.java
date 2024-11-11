package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.BankAccountDTO;
import com.banking.restapiebankify.model.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountMapper {

    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    @Mapping(source = "user.id", target = "userId")
    BankAccountDTO toBankAccountDTO(BankAccount bankAccount);

    BankAccount toBankAccount(BankAccountDTO bankAccountDTO);
}
