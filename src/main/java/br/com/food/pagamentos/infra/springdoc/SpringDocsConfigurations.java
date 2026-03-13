package br.com.food.pagamentos.infra.springdoc;

import br.com.food.pagamentos.infra.exception.ErrorResponse;
import br.com.food.pagamentos.infra.exception.ErrorValidationDetails;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocsConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        // Converte a classe Record ErrorResponse em um Schema do OpenAPI
        ResolvedSchema errorResponseSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class));
        // Converte a classe Record ErrorValidationDetails em um Schema do OpenApi
        ResolvedSchema errorDetailsSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ErrorValidationDetails.class));

        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080/pagamento-api").description("Gateway Server")))
                .info(new Info()
                        .title("pagamento-api")
                        .description("Serviço de pagamentos. " +
                                "Esta aplicação expõe endpoints REST para criação, consulta, " +
                                "atualização, confirmação e exclusão de pagamentos, " +
                                "além de realizar integração com o serviço de Pedidos")
                        .contact(new Contact()
                                .name("Time Backend")
                                .email("")))
                .components(new Components()
                        // Adiciona o schema convertido ao dicionário do Swagger
                        .addSchemas("ErrorResponse", errorResponseSchema.schema)
                        .addSchemas("ErrorValidationDetails", errorDetailsSchema.schema)
                        // Define as respostas
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Dados de entrada inválidos")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))

                        .addResponses("NotFound", new ApiResponse()
                                .description("Recurso não encontrado")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))));
    }
}
