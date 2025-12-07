package br.com.food.pagamentos.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pagamentos")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal valor;
    private String nome;
    private String numero;
    private String expiracao;
    private String codigo;

    @Enumerated(EnumType.STRING)
    private Status status;
    private Long pedidoId;
    private Long formaDePagamentoId;

    public Pagamento() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setExpiracao(String expiracao) {
        this.expiracao = expiracao;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public void setFormaDePagamentoId(Long formaDePagamentoId) {
        this.formaDePagamentoId = formaDePagamentoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public String getExpiracao() {
        return expiracao;
    }

    public String getCodigo() {
        return codigo;
    }

    public Long getFormaDePagamentoId() {
        return formaDePagamentoId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public Status getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }
}
