package com.vendor.services.impl;

import com.vendor.exceptions.*;
import com.vendor.models.BuyReturnDTO;
import com.vendor.models.Product;
import com.vendor.models.User;
import com.vendor.models.Wallet;
import com.vendor.repositories.ProductJpaRepository;
import com.vendor.repositories.WalletJpaRepository;
import com.vendor.services.ProductService;
import com.vendor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository repository;

    private final UserService userService;

    private final WalletJpaRepository walletJpaRepository;

    @Override
    public Product getByName(String name) {
        Optional<Product> modalOptional = this.repository.findByName(name);
        if (!modalOptional.isPresent()) {
            throw new NotFoundGeneralException(name);
        }

        return modalOptional.get();
    }

    @Override
    public List<Product> getAll() {
        return this.repository.findAll();
    }

    @Override
    public void delete(String name, User creator) {

        Product p = this.getByName(name);

        if(!p.getCreatorId().equals(creator.getId())) {
            throw new CreatorNotSameException();
        }

        this.repository.delete(this.getByName(name));
    }

    @Override
    public void save(Product product, User creator) {

        Optional<Product> modalOptional = this.repository.findByName(product.getName());
        if (modalOptional.isPresent()) {
            throw new ResourceAlreadyRegisteredException(product.getName());
        }

        product.setCreatorId(creator.getId());

        this.repository.save(product);
    }

    @Override
    public void update(String name, Product product, User creator) {

        Product p = this.getByName(name);

        if(!p.getCreatorId().equals(creator.getId())) {
            throw new CreatorNotSameException();
        }

        if(product.getName() != null) p.setName(product.getName());
        if(product.getCost() != null) p.setCost(product.getCost());

        this.repository.save(p);
    }

    @Override
    public BuyReturnDTO buy(String name, Integer amount, User u) {

        Product p = this.getByName(name);

        Integer totalCost = p.getCost()*amount;

        User user = this.userService.getUserByName(u.getUsername());

        if(user.getWallet().getAmount() < totalCost) {
            throw new NotEnoughFundsException();
        }

        Integer change = user.getWallet().getAmount() - totalCost;

        Wallet w = user.getWallet();
        w.setAmount(change);
        walletJpaRepository.save(w);

        List<Integer> returnList = new ArrayList<>();

        if(change > 0) {
            while(change > 0) {
                if(change - 100 >= 0) {
                    change -= 100;
                    returnList.add(100);
                } else if(change - 50 >= 0) {
                    change -= 50;
                    returnList.add(50);
                }else if(change - 20 >= 0) {
                    change -= 20;
                    returnList.add(20);
                }
                else if(change - 10 >= 0) {
                    change -= 10;
                    returnList.add(10);
                }else if(change -5 >= 0) {
                    change -= 5;
                    returnList.add(5);
                }
            }
        }

        return new BuyReturnDTO(p.getName(), returnList.stream().sorted().toList());
    }
}
