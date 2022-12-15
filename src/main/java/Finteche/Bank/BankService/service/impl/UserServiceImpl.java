package Finteche.Bank.BankService.service.impl;

import Finteche.Bank.BankService.models.Status;
import Finteche.Bank.BankService.models.User;
import Finteche.Bank.BankService.repository.UserRepository;
import Finteche.Bank.BankService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(User user) throws IllegalAccessException {
        if(
                user.getPatronymic() == null || user.getPassword() == null || user.getUsername() == null || user.getLastName() == null || user.getFirstName() == null ||
                        user.getEmail() == null
        ){
            throw new IllegalAccessException("Not enough data");
        }
        char[] notAllowSymb = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w'
                ,'x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W'
                ,'X','Y','Z','0','1','2','3','4','5','6','7','8','9', '!', '.', ',', '"', ' '};
        for(char nas : notAllowSymb){
            if(user.getFirstName().indexOf(nas) != -1 || user.getLastName().indexOf(nas) != -1 || user.getPatronymic().indexOf(nas) != -1){
                throw new IllegalAccessException("Not allow symbol in fio");
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setBalance("0");
        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);
        Random r = new Random();
        user.setAccountNumber(Integer.toString(r.nextInt(10000)+1000));
        while(userRepository.findByAccountNumber(user.getAccountNumber()) != null){
            user.setAccountNumber(Integer.toString(r.nextInt()));
        }
        if(userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        }
        else throw new IllegalAccessException("Such user has been registered earlier");
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public void deleteUser(String accountNumber) throws IllegalAccessException {

    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    public User findByAccountNumber(String accountNumber) {
        User result = userRepository.findByAccountNumber(accountNumber);
        log.info("IN findByUsername - user: {} found by username: {}", result, accountNumber);
        return result;
    }
}