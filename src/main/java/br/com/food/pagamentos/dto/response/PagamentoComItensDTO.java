package br.com.food.pagamentos.dto.response;

import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoComItensDTO(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String codigo,
        Status status,
        Long formaDePagamentoId,
        Long pedidoId,
        List<ItemDoPedidoDTO> itens
) {

    public PagamentoComItensDTO(Pagamento pagamento, PedidoDTO itensDoPedido) {
        this(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getNome(),
                pagamento.getNumero(),
                pagamento.getExpiracao(),
                pagamento.getCodigo(),
                pagamento.getStatus(),
                pagamento.getFormaDePagamentoId(),
                pagamento.getPedidoId(),
                itensDoPedido.itens()
        );
    }
}