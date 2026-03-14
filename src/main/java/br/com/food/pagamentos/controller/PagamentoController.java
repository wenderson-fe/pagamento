package br.com.food.pagamentos.controller;

import br.com.food.pagamentos.controller.openapi.PagamentoControllerOpenApi;
import br.com.food.pagamentos.dto.request.PagamentoAtualizacaoDTO;
import br.com.food.pagamentos.dto.response.PagamentoComItensDTO;
import br.com.food.pagamentos.dto.request.PagamentoRequestDTO;
import br.com.food.pagamentos.dto.response.PagamentoResponseDTO;
import br.com.food.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController implements PagamentoControllerOpenApi {
    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<Page<PagamentoResponseDTO>> listar(
            @RequestParam(required = false) Long pedidoId,
            @PageableDefault(size = 10) Pageable paginacao) {

        return ResponseEntity.ok().body(pagamentoService.obterTodos(paginacao, pedidoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> detalhar(@PathVariable Long id) {
        PagamentoResponseDTO dto = pagamentoService.obterPorId(id);
        return ResponseEntity.ok(dto);
    }

    // Retorna os dados do pagamento agregados com os itens do pedido associado,
    // realizando uma chamada ao serviço de Pedidos para compor a resposta.
    @GetMapping("/{id}/itens")
    public ResponseEntity<PagamentoComItensDTO> detalharComItensDoPedido(@PathVariable Long id) {
        PagamentoComItensDTO pagamentoComItens = pagamentoService.obterPorIdComItensDoPedido(id);
        return ResponseEntity.ok(pagamentoComItens);
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> cadastrar(@RequestBody @Valid PagamentoRequestDTO dtoRequest, UriComponentsBuilder uriBuilder) {
        PagamentoResponseDTO pagamento = pagamentoService.criarPagamento(dtoRequest);
        URI endereco = uriBuilder.path("/pagamento/{id}").buildAndExpand(pagamento.id()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PagamentoAtualizacaoDTO dto) {
        PagamentoResponseDTO atualizado = pagamentoService.atualizarPagamento(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        pagamentoService.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PagamentoResponseDTO> confirmarPagamento(@PathVariable Long id) {
        PagamentoResponseDTO pagamentoConfirmado = pagamentoService.confirmarPagamento(id);
        return  ResponseEntity.ok(pagamentoConfirmado);
    }

    @PostMapping("/cancelar-por-pedido/{pedidoId}")
    public ResponseEntity<Void> cancelarPagamentoPorPedido(@PathVariable Long pedidoId) {
        pagamentoService.cancelarPagamentoPorPedido(pedidoId);
        return ResponseEntity.ok().build();
    }

}