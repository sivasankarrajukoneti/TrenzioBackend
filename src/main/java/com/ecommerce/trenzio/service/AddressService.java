package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.AddressDTO;
import com.ecommerce.trenzio.model.Address;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAllAddresses();
    AddressDTO getAddressById(Long id);
    AddressDTO createAddress(AddressDTO addressDTO);
    AddressDTO updateAddress(Long id, AddressDTO addressDTO);
    void deleteAddress(Long id);
    Address getAddressByIdEntity(Long addressId);
}
