package com.vendor;


import com.vendor.models.*;
import com.vendor.repositories.ProductJpaRepository;
import com.vendor.repositories.UsersJpaRepository;
import com.vendor.repositories.WalletJpaRepository;
import com.vendor.services.UserService;
import com.vendor.services.impl.ProductServiceImpl;
import com.vendor.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class VendorApplicationTests {

    @InjectMocks
    UserServiceImpl userService;

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    WalletJpaRepository walletJpaRepository;

    @Mock
    ProductJpaRepository productJpaRepository;

    @Mock
    UsersJpaRepository usersJpaRepository;

    @Mock
    UserService userServiceMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDeposit() {
        User u = this.getMockUser();

        when(usersJpaRepository.findByUsername(u.getUsername())).thenReturn(Optional.of(u));

        when(walletJpaRepository.save(u.getWallet())).thenReturn(new Wallet(5));

        userService.deposit(5, u);

        assertEquals(5, u.getWallet().getAmount());
    }

    @Test
    void buy() {
        Product p = this.getMockProduct();
        User u = this.getMockUser();
        u.getWallet().setAmount(20);
        when(userServiceMock.getUserByName(u.getUsername())).thenReturn(u);
        when(productJpaRepository.findByName(p.getName())).thenReturn(Optional.of(p));
        when(walletJpaRepository.save(u.getWallet())).thenReturn(u.getWallet());

        BuyReturnDTO dto = productService.buy(p.getName(), 2, u);

        assertEquals(dto.getChange(), List.of(10));
        assertEquals(dto.getProductName(), p.getName());
    }

    private Wallet walletWithNewAmount(Wallet w, Integer amount) {
        w.setAmount(amount);
        return w;
    }

    private User getMockUser() {
        User u = new User();
        u.setUsername("someone");
        u.setCreatedDate(LocalDateTime.now());
        u.setPassword("some hash");
        u.setRoles(List.of(new Role("ROLE_USER")));

        Wallet w = new Wallet(0);
        u.setWallet(w);

        return u;
    }


    private Product getMockProduct() {
        return new Product(5, "pepsi");
    }

}
