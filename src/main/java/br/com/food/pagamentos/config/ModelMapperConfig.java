package br.com.food.pagamentos.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;
import static org.modelmapper.config.Configuration.AccessLevel.PUBLIC;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper obterModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                // Habilita o mapeamento direto dos campos da classe (além de via métodos).
                .setFieldMatchingEnabled(true)

                // Permite que o ModelMapper acesse campos privados diretamente.
                .setFieldAccessLevel(PRIVATE)

                // Define que métodos públicos (getters/setters) também podem ser usados.
                .setMethodAccessLevel(PUBLIC)

                // Faz o ModelMapper exigir nomes/estruturas muito próximos entre src e dest.
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}
