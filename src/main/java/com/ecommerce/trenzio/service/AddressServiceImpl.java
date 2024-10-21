package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.AddressDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.AddressMapper;
import com.ecommerce.trenzio.model.Address;
import com.ecommerce.trenzio.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream().map(addressMapper::addressToDTO).collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Address not found with ID: " + id));
        return addressMapper.addressToDTO(address);
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = addressMapper.dtoToAddress(addressDTO);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.addressToDTO(savedAddress);
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        // Retrieve the existing address entity
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));

        // Use MapStruct to update basic fields in the existing address entity
        addressMapper.updateAddressFromDTO(addressDTO, existingAddress);

        // Save and return the updated address
        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.addressToDTO(updatedAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Address not found with ID: " + id));
        addressRepository.deleteById(id);
    }

    public Address getAddressByIdEntity(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));
    }

}
