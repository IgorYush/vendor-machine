package com.vendor.services.impl;

import com.vendor.constants.Constants;
import com.vendor.exceptions.NotFoundUserException;
import com.vendor.exceptions.TypeOfCoinNotAcceptedException;
import com.vendor.exceptions.UserAlreadyHaveRoleException;
import com.vendor.models.Role;
import com.vendor.models.User;
import com.vendor.models.Wallet;
import com.vendor.repositories.UsersJpaRepository;
import com.vendor.exceptions.ResourceAlreadyRegisteredException;
import com.vendor.repositories.WalletJpaRepository;
import com.vendor.services.RoleService;
import com.vendor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersJpaRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final WalletJpaRepository walletJpaRepository;

    @Override
    @Transactional
    public void save(User user) {
        Optional<User> modalOptional = this.repository.findByUsername(user.getUsername());
        if (modalOptional.isPresent()) {
            throw new ResourceAlreadyRegisteredException(user.getUsername());
        }else {
            User userEntity = new User();
            userEntity.setUsername(user.getUsername());
            userEntity.setPassword(this.passwordEncoder.encode(user.getPassword()));
            userEntity.setCreatedDate(LocalDateTime.now());
            // userEntity.setActive(false);

            userEntity.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));

            Wallet w = this.walletJpaRepository.save(new Wallet(0));

            userEntity.setWallet(w);

            this.repository.save(userEntity);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return this.repository.findAll();
    }

    @Override
    public User getUserByName(String name) {
        Optional<User> modalOptional = this.repository.findByUsername(name);
        if (!modalOptional.isPresent()) {
            throw new NotFoundUserException(name);
        }

        return modalOptional.get();
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = this.getUserByName(username);
        Role role = this.roleService.getRoleByName(roleName);

        if(user.getRoles().stream().anyMatch(role.getId()::equals)) {
            throw new UserAlreadyHaveRoleException(username, roleName);
        }else {
            user.getRoles().add(role);
            this.repository.save(user);
        }

    }

    @Override
    public void deposit(Integer value, User u) {

        if(!Arrays.stream(Constants.POSSIBLE_COINS).anyMatch(value::equals)) {
            throw new TypeOfCoinNotAcceptedException();
        }

        User user = this.getUserByName(u.getUsername());

        Wallet w = user.getWallet();
        Integer total = w.getAmount() + value;
        w.setAmount(total);

        this.walletJpaRepository.save(w);
    }

    @Override
    public void reset(User u) {
        User user = this.getUserByName(u.getUsername());

        Wallet w = user.getWallet();
        w.setAmount(0);

        this.walletJpaRepository.save(w);
    }

    @Override
    public Integer getUserAmount(User u) {
        User user = this.getUserByName(u.getUsername());
        return user.getWallet().getAmount();
    }

}