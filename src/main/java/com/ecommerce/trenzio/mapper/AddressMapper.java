package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.AddressDTO;
import com.ecommerce.trenzio.model.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    // Map Address entity to AddressDTO
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    AddressDTO addressToDTO(Address address);

    // Map AddressDTO to Address entity
    @Mapping(target = "users", ignore = true)  // We are not handling users directly in Address service
    Address dtoToAddress(AddressDTO addressDTO);

    // Update Address entity from AddressDTO without modifying users
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "users", ignore = true)  // Ignore users during update
    void updateAddressFromDTO(AddressDTO addressDTO, @MappingTarget Address address);

}
