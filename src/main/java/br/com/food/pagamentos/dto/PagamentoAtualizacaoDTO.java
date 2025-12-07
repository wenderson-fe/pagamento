package br.com.food.pagamentos.dto;

import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PagamentoAtualizacaoDTO(
        @NotNull
        @Positive
        BigDecimal valor,

        @NotBlank
        @Size(max = 100)
        String nome,

        @NotBlank
        @Size(max = 19)
        String numero,

        @NotBlank
        @Size(max = 7)
        String expiracao,

        @NotBlank
        @Size(min = 3, max = 3)
        String codigo,

        Status status,

        @NotNull
        Long pedidoId,

        @NotNull
        Long formaDePagamentoId
) {
    public PagamentoAtualizacaoDTO(Pagamento pagamento) {
        this(
                pagamento.getValor(),
                pagamento.getNome(),
                pagamento.getNumero(),
                pagamento.getExpiracao(),
                pagamento.getCodigo(),
                pagamento.getStatus(),
                pagamento.getPedidoId(),
                pagamento.getFormaDePagamentoId()
        );
    }
}
