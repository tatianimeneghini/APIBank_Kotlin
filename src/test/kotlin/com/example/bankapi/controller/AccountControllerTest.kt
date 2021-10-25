package com.example.bankapi.controller

import com.example.bankapi.model.Account
import com.example.bankapi.repository.AccountRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Test
    fun `test find all`() {
        accountRepository.save(Account(name = "Teste", document = "123", phone = "986372927"))

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].document").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].phone").isString)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test find by id`() {
        val account = accountRepository.save(
            Account(
                name = "Teste",
                document = "123",
                phone = "986372927"
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/${account.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(account.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account`() {
        val account = Account(
            name = "Teste",
            document = "12345678910",
            phone = "986372927"
        )
        val json = ObjectMapper().writeValueAsString(account)
        accountRepository.deleteAll()

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())

        Assertions.assertFalse(accountRepository.findAll().isEmpty())
    }

    @Test
    fun `test create account validation error empty name`() {
        val account = Account(
            name = "",
            document = "123",
            phone = "986372927"
        )
        val json = ObjectMapper().writeValueAsString(account)
        accountRepository.deleteAll()

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[name] não pode estar em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account validation error should be 5 character`() {
        val account = Account(
            name = "Test",
            document = "123",
            phone = "986372927"
        )
        val json = ObjectMapper().writeValueAsString(account)
        accountRepository.deleteAll()

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[name] deve ter no mínimo 5 caracteres."))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account validation error empty document`() {
        val account = Account(
            name = "Teste",
            document = "",
            phone = "986372927"
        )
        val json = ObjectMapper().writeValueAsString(account)
        accountRepository.deleteAll()

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[document] não pode estar em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account validation error should be 11 character`() {
        val account = Account(
            name = "Teste",
            document = "123",
            phone = "986372927"
        )
        val json = ObjectMapper().writeValueAsString(account)
        accountRepository.deleteAll()

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[document] deve ter no mínimo 11 caracteres."))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account validation error empty phone`() {
        val account = Account(
            name = "Teste",
            document = "12345678910",
            phone = "999999"
        )
        val json = ObjectMapper().writeValueAsString(account)
        accountRepository.deleteAll()

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[phone] deve ter no mínimo 9 caracteres."))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account validation error should be 9 character`() {
        val account = Account(
            name = "Teste",
            document = "123",
            phone = "986372927"
        )
        val json = ObjectMapper().writeValueAsString(account)
        accountRepository.deleteAll()

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[document] deve ter no mínimo 11 caracteres."))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test update account`() {
        val account = accountRepository.save(
            Account(
                name = "Teste Create",
                document = "",
                phone = "986372927"
            )
        ).copy(name = "Updated")
        val json = ObjectMapper().writeValueAsString(account)


        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/${account.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(account.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())

        val findById = accountRepository.findById(account.id!!)
        Assertions.assertTrue(findById.isPresent)
        Assertions.assertEquals(account.name, findById.get().name)
    }

    @Test
    fun `test delete account`() {
        val account = accountRepository.save(
            Account(
                name = "Teste",
                document = "123",
                phone = "986372927"
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/${account.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        val findById = accountRepository.findById(account.id!!)
        Assertions.assertFalse(findById.isPresent)
    }
}