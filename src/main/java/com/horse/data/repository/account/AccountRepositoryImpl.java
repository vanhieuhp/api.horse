package com.horse.data.repository.account;

import com.horse.data.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Repository
public class AccountRepositoryImpl implements AccountCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void changePassword(String username, String password) {

        if (username != null && password != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE Account set password = :password WHERE username = :username");

            TypedQuery<Account> query = entityManager.createQuery(sql.toString(), Account.class);

            query.setParameter("password", password);
            query.setParameter("username", username);

            query.getResultList();
        }
    }


}
