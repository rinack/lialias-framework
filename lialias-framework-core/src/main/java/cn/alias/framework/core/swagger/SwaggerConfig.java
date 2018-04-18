package cn.alias.framework.core.swagger;

import org.springframework.context.annotation.Bean;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import io.swagger.models.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig {

	@Bean    
    public Docket adminApi(){    
        return new Docket(DocumentationType.SWAGGER_2)    
                .groupName("Document Api")    
                .forCodeGeneration(true)    
                .pathMapping("/")    
                .select()    
                .paths(paths())    
                .build()    
                .apiInfo(apiInfo())    
                .useDefaultResponseMessages(false);    
    }
    
    private Predicate<String> paths(){    
        return Predicates.and(PathSelectors.regex("/.*"), Predicates.not(PathSelectors.regex("/error")));    
    }
    
    private ApiInfo apiInfo(){    
        Contact contact = new Contact().name("").url("").email("");    
        return new ApiInfoBuilder()    
                .title("Document Api")    
                .description("Spring-boot-Springfox Example")    
                .license("Apache License Version 2.0")    
                .contact(contact.toString())    
                .version("2.0")    
                .build();    
    }
}