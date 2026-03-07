package br.com.food.pagamentos.controller;

import br.com.food.pagamentos.controller.openapi.PagamentoControllerOpenApi;
import br.com.food.pagamentos.dto.PagamentoAtualizacaoDTO;
import br.com.food.pagamentos.dto.PagamentoComItensDTO;
import br.com.food.pagamentos.dto.PagamentoDTO;
import br.com.food.pagamentos.service.PagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<Page<PagamentoDTO>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        Page<PagamentoDTO> page = pagamentoService.obterTodos(paginacao);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> detalhar(@PathVariable Long id) {
        PagamentoDTO dto = pagamentoService.obterPorId(id);
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
    public ResponseEntity<PagamentoDTO> cadastrar(@RequestBody @Valid PagamentoDTO dto, UriComponentsBuilder uriBuilder) {
        PagamentoDTO pagamento = pagamentoService.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamento/{id}").buildAndExpand(pagamento.id()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PagamentoAtualizacaoDTO dto) {
        PagamentoDTO atualizado = pagamentoService.atualizarPagamento(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        pagamentoService.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<PagamentoDTO> confirmarPagamento(@PathVariable Long id) {
        PagamentoDTO pagamentoConfirmado = pagamentoService.confirmarPagamento(id);
        return  ResponseEntity.ok(pagamentoConfirmado);
    }
}