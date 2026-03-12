package br.com.food.pagamentos.service;

import br.com.food.pagamentos.dto.PedidoDTO;
import br.com.food.pagamentos.dto.PagamentoAtualizacaoDTO;
import br.com.food.pagamentos.dto.PagamentoComItensDTO;
import br.com.food.pagamentos.dto.PagamentoDTO;
import br.com.food.pagamentos.infra.http.PedidoClient;
import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;
import br.com.food.pagamentos.repository.PagamentoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {
    private PagamentoRepository pagamentoRepository;
    private ModelMapper modelMapper;
    private PedidoClient pedidoClient;

    public PagamentoService(PagamentoRepository pagamentoRepository, ModelMapper modelMapper, PedidoClient pedidoClient) {
        this.pagamentoRepository = pagamentoRepository;
        this.modelMapper = modelMapper;
        this.pedidoClient = pedidoClient;
    }

    public Page<PagamentoDTO> obterTodos(Pageable paginacao, Long pedidoId) {
        if (pedidoId != null) {
            return pagamentoRepository.findAllByPedidoId(pedidoId, paginacao)
                    .map(PagamentoDTO::new);
        }

        return pagamentoRepository
                .findAll(paginacao)
                .map(PagamentoDTO::new);
    }

    public PagamentoDTO obterPorId(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return new PagamentoDTO(pagamento);
    }

    @CircuitBreaker(name = "pedidoItens", fallbackMethod = "pagamentoComListaDeItensVazia")
    public PagamentoComItensDTO obterPorIdComItensDoPedido(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com o id: " + id));

        PedidoDTO itensDoPedido = pedidoClient.obterPedidoComItens(pagamento.getPedidoId());

        return new PagamentoComItensDTO(pagamento, itensDoPedido);
    }

    // fallback de "obterPorIdComItensDoPedido"
    public PagamentoComItensDTO pagamentoComListaDeItensVazia(Long id, Exception e) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com o id: " + id));

        // Retorna o pedido com lista de itens vazia.
        return new PagamentoComItensDTO(pagamento, new PedidoDTO(List.of()));
    }

    @Transactional
    public PagamentoDTO criarPagamento(PagamentoDTO dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamentoRepository.save(pagamento);

        return new PagamentoDTO(pagamento);
    }

    @Transactional
    public PagamentoDTO atualizarPagamento(Long id, PagamentoAtualizacaoDTO dto) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com o id: " + id));
        modelMapper.map(dto, pagamento);

        return new PagamentoDTO(pagamento);
    }

    @Transactional
    public void excluirPagamento(Long id) {
        if(!pagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pagamento não encontrado com o id: " + id);
        }
        pagamentoRepository.deleteById(id);
    }

    // Confirma o pagamento e notifica o serviço de Pedidos.
    @Transactional
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoComConfirmacaoPendente")
    public PagamentoDTO confirmarPagamento(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com o id: " + id));

        pagamento.setStatus(Status.CONFIRMADO);

        pedidoClient.atualizaPagamento(pagamento.getPedidoId());

        return new PagamentoDTO(pagamento);
    }

    // fallback de "confirmarPagamento"
    @Transactional()
    public PagamentoDTO pagamentoComConfirmacaoPendente(Long id, Exception e) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        pagamento.setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);

        return new PagamentoDTO(pagamento);
    }

    @Transactional
    public void cancelarPagamentoPorPedido(Long pedidoId) {
        List<Pagamento> pagamentos = pagamentoRepository.findAllByPedidoId(pedidoId);

        if(pagamentos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pagamento encontrado para o pedido: " + pedidoId);
        }

        pagamentos.stream()
                .filter(p -> !Status.CANCELADO.equals(p.getStatus()))
                .forEach(p -> p.setStatus(Status.CANCELADO));
    }
}
