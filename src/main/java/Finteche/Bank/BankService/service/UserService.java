package Finteche.Bank.BankService.service;

import Finteche.Bank.BankService.dto.RegisterDto;
import Finteche.Bank.BankService.models.User;

import java.util.List;

public interface UserService {
    void register(RegisterDto registerDto) throws IllegalAccessException;
    List<User> getAll();
    void deleteUser(String accountNumber) throws IllegalAccessException;

    User findByUsername(String username);

    User findByAccountNumber(String accountNumber);
}
