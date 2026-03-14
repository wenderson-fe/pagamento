package br.com.food.pagamentos.dto.response;

import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;

import java.math.BigDecimal;

public record PagamentoResponseDTO(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String codigo,
        Status status,
        Long formaDePagamentoId,
        Long pedidoId
) {
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getNome(),
                pagamento.getNumero(),
                pagamento.getExpiracao(),
                pagamento.getCodigo(),
                pagamento.getStatus(),
                pagamento.getFormaDePagamentoId(),
                pagamento.getPedidoId()
        );
    }

}
