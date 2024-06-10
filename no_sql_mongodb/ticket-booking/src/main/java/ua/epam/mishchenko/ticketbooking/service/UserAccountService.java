package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.User;

import java.math.BigDecimal;
import java.util.UUID;

public interface UserAccountService {

    User refillAccount(UUID userId, BigDecimal money);
}
