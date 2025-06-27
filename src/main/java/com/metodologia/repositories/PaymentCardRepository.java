package com.metodologia.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.metodologia.models.PaymentCard;

@Repository
public interface PaymentCardRepository extends CrudRepository<PaymentCard, Long>{

}
