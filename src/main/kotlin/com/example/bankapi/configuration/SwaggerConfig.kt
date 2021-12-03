package com.example.bankapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.spring.web.plugins.Docket as Docket1

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket1 {
        return Docket1(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET, responseMessageForGET())
    }

    private fun responseMessageForGET(): List<ResponseMessage?>? {
        return object : ArrayList<ResponseMessage?>() {
            init {
                add(
                    ResponseMessageBuilder()
                        .code(403)
                        .message("Forbidden!")
                        .build()
                )
            }
        }
    }
}


