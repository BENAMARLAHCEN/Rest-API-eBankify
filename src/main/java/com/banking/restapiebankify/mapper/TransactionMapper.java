package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "fromAccount.id", target = "fromAccountId")
    @Mapping(source = "toAccount.id", target = "toAccountId")
    TransactionDTO toTransactionDTO(Transaction transaction);

    @Mapping(target = "fromAccount.id", source = "fromAccountId")
    @Mapping(target = "toAccount.id", source = "toAccountId")
    Transaction toTransaction(TransactionDTO transactionDTO);
}
