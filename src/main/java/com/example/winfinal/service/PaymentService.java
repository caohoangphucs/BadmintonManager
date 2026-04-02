package com.example.winfinal.service;

import com.example.winfinal.dao.PaymentDAO;
import com.example.winfinal.dao.BookingDAO;
import com.example.winfinal.dto.PaymentDTO;
import com.example.winfinal.entity.Payment;
import com.example.winfinal.mapper.PaymentMapper;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentService {
    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.bookingDAO = new BookingDAO();
    }

    public List<PaymentDTO> getAllPayments() {
        return paymentDAO.findAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(Integer id) {
        return paymentDAO.findById(id)
                .map(paymentMapper::toDTO)
                .orElse(null);
    }

    public void processPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setStatus(paymentDTO.getStatus());

        bookingDAO.findById(paymentDTO.getBookingId()).ifPresent(payment::setBooking);

        paymentDAO.save(payment);
    }

    public void updatePayment(PaymentDTO paymentDTO) {
        paymentDAO.findById(paymentDTO.getPaymentId()).ifPresent(payment -> {
            payment.setPaymentDate(paymentDTO.getPaymentDate());
            payment.setAmount(paymentDTO.getAmount());
            payment.setPaymentMethod(paymentDTO.getPaymentMethod());
            payment.setStatus(paymentDTO.getStatus());

            bookingDAO.findById(paymentDTO.getBookingId()).ifPresent(payment::setBooking);

            paymentDAO.update(payment);
        });
    }

    public void deletePayment(Integer id) {
        paymentDAO.deleteById(id);
    }
}
