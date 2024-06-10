package ua.epam.mishchenko.ticketbooking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.repository.UserRepository;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserRepository userRepository;

    public UserAccountServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User refillAccount(UUID userId, BigDecimal money) {
        log.info("Refilling user account for user with id: {}", userId);
        try {
            thrownRuntimeExceptionIfMoneyLessZero(money);
            throwRuntimeExceptionIfUserNotExist(userId);
            User user = userRepository.findById(userId).get();
            BigDecimal oldMoney = user.getMoney();
            if (oldMoney == null) {
                user.setMoney(money);
            } else {
                user.setMoney(oldMoney.add(money));
            }
            log.info("The user account with user id {} successfully refilled", userId);
            return user;
        } catch (RuntimeException e) {
            log.warn("Can not to refill account with user id: {}", userId);
            return null;
        }
    }

    private void thrownRuntimeExceptionIfMoneyLessZero(BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) < 1) {
            throw new RuntimeException("The money can not to be less zero");
        }
    }

    private void throwRuntimeExceptionIfUserNotExist(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("The user with id " + userId + " does not exist");
        }
    }
}
