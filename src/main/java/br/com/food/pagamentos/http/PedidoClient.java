package br.com.food.pagamentos.http;

import br.com.food.pagamentos.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("pedidos-api")
public interface PedidoClient {
    // Realiza uma chamada HTTP PUT ao serviço de Pedidos
    // para atualizar o status do pedido para "PAGO".
    @RequestMapping(method = RequestMethod.POST, value = "/pedidos/{id}/pago")
    void atualizaPagamento(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/pedidos/{id}")
    PedidoDTO obterPedidoComItens(@PathVariable Long id);
}
