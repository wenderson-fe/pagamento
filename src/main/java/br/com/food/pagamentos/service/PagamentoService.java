package br.com.food.pagamentos.service;

import br.com.food.pagamentos.dto.PagamentoAtualizacaoDTO;
import br.com.food.pagamentos.dto.PagamentoDTO;
import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;
import br.com.food.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {
    private PagamentoRepository pagamentoRepository;
    private ModelMapper modelMapper;

    public PagamentoService(PagamentoRepository pagamentoRepository, ModelMapper modelMapper) {
        this.pagamentoRepository = pagamentoRepository;
        this.modelMapper = modelMapper;
    }

    public Page<PagamentoDTO> obterTodos(Pageable paginacao) {
        return pagamentoRepository
                .findAll(paginacao)
                .map(PagamentoDTO::new);
    }

    public PagamentoDTO obterPorId(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return new PagamentoDTO(pagamento);
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
        Pagamento pagamento = pagamentoRepository.getReferenceById(id);
        modelMapper.map(dto, pagamento);

        return new PagamentoDTO(pagamento);
    }

    @Transactional
    public void excluirPagamento(Long id) {
        pagamentoRepository.deleteById(id);
    }
}
