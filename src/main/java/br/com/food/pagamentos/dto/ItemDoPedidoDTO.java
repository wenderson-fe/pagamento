package br.com.food.pagamentos.dto;

public record ItemDoPedidoDTO(
        Long id,
        Integer quantidade,
        String descricao
) {
}
