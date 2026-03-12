package br.com.food.pagamentos.controller.openapi;

import br.com.food.pagamentos.dto.PagamentoAtualizacaoDTO;
import br.com.food.pagamentos.dto.PagamentoComItensDTO;
import br.com.food.pagamentos.dto.PagamentoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Pagamento", description = "Endpoints para gerenciamento de pagamentos e integração com pedidos")
public interface PagamentoControllerOpenApi {

    @Operation(summary = "Lista todos os pagamentos", description = "Retorna uma lista paginada de todos os registros de pagamentos.")
    ResponseEntity<Page<PagamentoDTO>> listar(
            @Parameter(
                    description = "ID do pedido para filtrar os pagamentos. Caso não seja informado, serão listados todos os pagamentos paginados",
                    example = "10")
            Long pedidoId,
            @Parameter(description = "Configuração de paginação") Pageable paginacao);

    @Operation(summary = "Busca pagamento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<PagamentoDTO> detalhar(@Parameter(description = "ID do pagamento", example = "1") Long id);

    @Operation(summary = "Busca pagamento detalhado com itens",
            description = "Agrega dados do pagamento com os itens do pedido via comunicação entre microsserviços")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados agregados com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<PagamentoComItensDTO> detalharComItensDoPedido(@Parameter(description = "ID do pagamento", example = "1") Long id);

    @Operation(summary = "Registra um novo pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso"),
            @ApiResponse(responseCode = "400", ref = "BadRequest")
    })
    ResponseEntity<PagamentoDTO> cadastrar(PagamentoDTO dto, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Atualiza dados de um pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento atualizado"),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<PagamentoDTO> atualizar(@Parameter(description = "ID do pagamento") Long id, PagamentoAtualizacaoDTO dto);

    @Operation(summary = "Exclui um registro de pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pagamento removido com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<Void> remover(@Parameter(description = "ID do pagamento") Long id);

    @Operation(summary = "Confirma o pagamento no sistema",
            description = "Altera o status para CONFIRMADO e dispara a notificação para o serviço de pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento confirmado"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<PagamentoDTO> confirmarPagamento(@Parameter(description = "ID do pagamento") Long id);

    @Operation(
            summary = "Cancela pagamentos por ID do Pedido",
            description = "Localiza todos os pagamentos associados a um pedido e altera seus status para CANCELADO, ignorando os que já estão cancelados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamentos processados com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<Void> cancelarPagamentoPorPedido(
            @Parameter(description = "ID do pedido cujos pagamentos serão cancelados", example = "123")
            Long pedidoId
    );
}
