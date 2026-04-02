package com.example.winfinal.mapper;

import com.example.winfinal.dto.ServiceInvoiceDTO;
import com.example.winfinal.entity.ServiceInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceInvoiceMapper {
    ServiceInvoiceMapper INSTANCE = Mappers.getMapper(ServiceInvoiceMapper.class);

    @Mapping(source = "booking.bookingId", target = "bookingId")
    ServiceInvoiceDTO toDTO(ServiceInvoice serviceInvoice);
}
