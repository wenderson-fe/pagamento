package br.com.food.pagamentos.repository;

import br.com.food.pagamentos.model.Pagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Page<Pagamento> findAllByPedidoId(Long pedidoId, Pageable paginacao);
    List<Pagamento> findAllByPedidoId(Long pedidoId);
}
