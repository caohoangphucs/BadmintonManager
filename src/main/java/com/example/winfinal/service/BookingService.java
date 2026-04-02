package com.example.winfinal.service;

import com.example.winfinal.dao.BookingDAO;
import com.example.winfinal.dao.CustomerDAO;
import com.example.winfinal.dao.CourtDAO;
import com.example.winfinal.dto.BookingDTO;
import com.example.winfinal.entity.Booking;
import com.example.winfinal.mapper.BookingMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookingService {
    private final BookingDAO bookingDAO;
    private final CustomerDAO customerDAO;
    private final CourtDAO courtDAO;
    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.customerDAO = new CustomerDAO();
        this.courtDAO = new CourtDAO();
    }

    public List<BookingDTO> getAllBookings() {
        return bookingDAO.findAll().stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(Integer id) {
        return bookingDAO.findById(id)
                .map(bookingMapper::toDTO)
                .orElse(null);
    }

    public List<BookingDTO> getByStatus(String status) {
        return bookingDAO.findByStatus(status).stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void addBooking(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setTotalPrice(bookingDTO.getTotalPrice());
        booking.setStatus(bookingDTO.getStatus());

        customerDAO.findById(bookingDTO.getCustomerId()).ifPresent(booking::setCustomer);
        courtDAO.findById(bookingDTO.getCourtId()).ifPresent(booking::setCourt);

        bookingDAO.save(booking);
    }

    public void updateBooking(BookingDTO bookingDTO) {
        bookingDAO.findById(bookingDTO.getBookingId()).ifPresent(booking -> {
            booking.setBookingDate(bookingDTO.getBookingDate());
            booking.setStartTime(bookingDTO.getStartTime());
            booking.setEndTime(bookingDTO.getEndTime());
            booking.setTotalPrice(bookingDTO.getTotalPrice());
            booking.setStatus(bookingDTO.getStatus());

            customerDAO.findById(bookingDTO.getCustomerId()).ifPresent(booking::setCustomer);
            courtDAO.findById(bookingDTO.getCourtId()).ifPresent(booking::setCourt);

            bookingDAO.update(booking);
        });
    }

    public void deleteBooking(Integer id) {
        bookingDAO.deleteById(id);
    }
}
