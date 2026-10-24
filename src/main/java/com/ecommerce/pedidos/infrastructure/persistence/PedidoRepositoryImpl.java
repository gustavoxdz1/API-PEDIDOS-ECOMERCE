package com.ecommerce.pedidos.infrastructure.persistence;

import com.ecommerce.pedidos.core.domain.entities.ItemPedido;
import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component // Anotação do Spring para injetar essa classe automaticamente no Caso de Uso
public class PedidoRepositoryImpl implements PedidoRepository {
    private final SpringDataPedidoRepository springDataPedidoRepository;
    public PedidoRepositoryImpl(SpringDataPedidoRepository springDataPedidoRepository) {
        this.springDataPedidoRepository = springDataPedidoRepository;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        // 1. Converte do Domínio para o Modelo de Banco de Dados (Entity)
        PedidoEntity entity = toEntity(pedido);

        // 2. Salva no banco de dados usando o Spring Data JPA
        PedidoEntity entitySalva = springDataPedidoRepository.save(entity);

        // 3. Converte de volta para o Modelo de Domínio e retorna
        return toDomain(entitySalva);
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        // Busca no banco e, se encontrar, converte para Domínio
        return springDataPedidoRepository.findById(id)
                .map(this::toDomain);
    }
    // --- Métodos Privados de Mapeamento (Mapper) ---

    private PedidoEntity toEntity(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(pedido.getId());
        entity.setUsuarioId(pedido.getUsuarioId());
        entity.setValorTotal(pedido.getValorTotal());
        entity.setStatus(pedido.getStatus());
        entity.setDataCriacao(pedido.getDataCriacao());

        // Converte a lista de Itens
        List<ItemPedidoEntity> itemsEntity = pedido.getItens().stream()
                .map(item -> {
                    ItemPedidoEntity itemEntity = new ItemPedidoEntity();
                    itemEntity.setId(item.getId());
                    itemEntity.setProdutoId(item.getProdutoId());
                    itemEntity.setQuantidade(item.getQuantidade());
                    itemEntity.setPrecoUnitario(item.getPrecoUnitario());
                    itemEntity.setPedido(entity); // Amarração da chave estrangeira
                    itemEntity.setDateTime(item.getDateTime());
                    return itemEntity;
                })
                .toList();

        entity.setItens(itemsEntity);
        return entity;
    }

    private Pedido toDomain(PedidoEntity entity) {
        // Converte a lista de ItemsEntity para Itens de Domínio
        List<ItemPedido> itens = entity.getItens().stream()
                .map(itemEntity -> new ItemPedido(
                        itemEntity.getId(),
                        itemEntity.getProdutoId(),
                        itemEntity.getQuantidade(),
                        itemEntity.getPrecoUnitario(),
                        itemEntity.getDateTime()
                ))
                .toList();

        // Aqui, precisamos de um construtor na classe Pedido que aceite todos os atributos (inclusive os preenchidos no banco, como o status atual e o ID).
        // Vamos precisar criar esse construtor na classe Pedido.java no próximo passo!
        return new Pedido(
                entity.getId(),
                entity.getUsuarioId(),
                itens,
                entity.getValorTotal(),
                entity.getStatus(),
                entity.getDataCriacao()  // Passando a data recuperada do banco

        );
    }
}
