package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanMapper {

    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    @Mapping(source = "user.id", target = "userId")
    LoanDTO toLoanDTO(Loan loan);

    @Mapping(target = "user.id", source = "userId")
    Loan toLoan(LoanDTO loanDTO);
}
