package com.tae.Etickette.payment.infra;

import com.tae.Etickette.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
