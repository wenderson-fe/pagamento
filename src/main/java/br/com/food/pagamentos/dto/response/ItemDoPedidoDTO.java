package br.com.food.pagamentos.dto.response;

public record ItemDoPedidoDTO(
        Long id,
        Integer quantidade,
        String descricao
) {
}
