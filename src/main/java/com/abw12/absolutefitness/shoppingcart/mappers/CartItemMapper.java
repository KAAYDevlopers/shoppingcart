package com.abw12.absolutefitness.shoppingcart.mappers;

import com.abw12.absolutefitness.shoppingcart.dto.CartItemDTO;
import com.abw12.absolutefitness.shoppingcart.entity.CartItemDAO;
import com.abw12.absolutefitness.shoppingcart.helper.OffsetDateTimeParser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartItemMapper extends OffsetDateTimeParser {
    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);
    @Mapping(source = "cartItemCreatedAt", target = "cartItemCreatedAt", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(source = "cartItemModifiedAt", target = "cartItemModifiedAt", qualifiedByName = "stringToOffsetDateTime")
    CartItemDAO dtoToEntity(CartItemDTO requestDTO);
    @Mapping(source = "cartItemCreatedAt", target = "cartItemCreatedAt", qualifiedByName = "offsetDateTimeToString")
    @Mapping(source = "cartItemModifiedAt", target = "cartItemModifiedAt", qualifiedByName = "offsetDateTimeToString")
    CartItemDTO entityToDto(CartItemDAO dbData);
}
