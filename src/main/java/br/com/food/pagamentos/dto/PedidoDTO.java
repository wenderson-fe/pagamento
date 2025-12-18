package br.com.food.pagamentos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PedidoDTO(
        List<ItemDoPedidoDTO> itens
) {
}
