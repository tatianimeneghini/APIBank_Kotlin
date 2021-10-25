package com.example.bankapi.service

import com.example.bankapi.model.Account
import com.example.bankapi.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class AccountServiceImpl(private val repository: AccountRepository) : AccountService {

    override fun create(account: Account): Account {
        Assert.hasLength(account.name, "[name] não pode estar em branco!")
        Assert.isTrue(account.name.length >= 5, "[name] deve ter no mínimo 5 caracteres.")

        Assert.hasLength(account.document, "[document] não pode estar em branco!")
        Assert.isTrue(account.document.length >= 11, "[document] deve ter no mínimo 11 caracteres.")

        Assert.hasLength(account.phone, "[phone] não pode estar em branco!")
        Assert.isTrue(account.phone.length >= 9, "[phone] deve ter no mínimo 9 caracteres.")

        return repository.save(account)
    }

    override fun getAll(): List<Account> {
        return repository.findAll()
    }

    override fun getById(id: Long): Optional<Account> {
        return repository.findById(id)
    }

    override fun update(id: Long, account: Account): Optional<Account> {
        val optional = getById(id)
        if (optional.isEmpty) Optional.empty<Account>()

        return optional.map {
            val accountUpdated = it.copy(
                name = account.name,
                document = account.document,
                phone = account.phone
            )
            repository.save(accountUpdated)
        }
    }

    override fun delete(id: Long) {
        repository.findById(id).map {
            repository.delete(it)
        }.orElseThrow { throw RuntimeException("Id not found $id") }
    }
}