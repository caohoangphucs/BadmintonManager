package com.example.winfinal.controller;

import com.example.winfinal.dto.BookingDTO;
import com.example.winfinal.service.BookingService;

import java.util.List;

public class BookingController {
    private final BookingService bookingService;

    public BookingController() {
        this.bookingService = new BookingService();
    }

    public List<BookingDTO> getAll() {
        return bookingService.getAllBookings();
    }

    public BookingDTO getById(Integer id) {
        return bookingService.getBookingById(id);
    }

    public List<BookingDTO> getByStatus(String status) {
        return bookingService.getByStatus(status);
    }

    public void createBooking(BookingDTO dto) {
        bookingService.addBooking(dto);
    }

    public void updateBooking(BookingDTO dto) {
        bookingService.updateBooking(dto);
    }

    public void cancelBooking(Integer id) {
        BookingDTO booking = bookingService.getBookingById(id);
        if (booking != null) {
            if ("Cancelled".equalsIgnoreCase(booking.getStatus())) {
                throw new RuntimeException("Lượt đặt sân này đã bị hủy từ trước.");
            }
            booking.setStatus("Cancelled");
            bookingService.updateBooking(booking);
        } else {
            throw new RuntimeException("Không tìm thấy lượt đặt sân này trong hệ thống.");
        }
    }

    public void deleteBooking(Integer id) {
        bookingService.deleteBooking(id);
    }
}
