package com.abw12.absolutefitness.shoppingcart.mappers;

import com.abw12.absolutefitness.shoppingcart.dto.CartDTO;
import com.abw12.absolutefitness.shoppingcart.entity.CartDAO;
import com.abw12.absolutefitness.shoppingcart.helper.OffsetDateTimeParser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper extends OffsetDateTimeParser {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(source = "cartCreatedAt", target = "cartCreatedAt", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "cartModifiedAt", target = "cartModifiedAt", qualifiedByName = "stringToOffsetDateTime")
    CartDAO dtoToEntity(CartDTO requestDTO);

    @Mapping(source = "cartModifiedAt", target = "cartCreatedAt", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "cartModifiedAt", target = "cartModifiedAt", qualifiedByName = "offsetDateTimeToString")
    CartDTO entityToDto(CartDAO dbData);
}
