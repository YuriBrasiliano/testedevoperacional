package Venda;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerar uma nova venda.
 */
public class GerarVenda {

    /**
     * Cria uma nova venda com base nos parâmetros fornecidos.
     *
     * @param empresa   A empresa relacionada à venda.
     * @param cliente   O cliente relacionado à venda.
     * @param vendas    A lista de vendas existentes.
     * @param carrinho  A lista de produtos no carrinho.
     * @return A venda criada.
     */
    public Venda createSale(Empresa empresa, Cliente cliente, List<Venda> vendas, List<Produto> carrinho) {
        // Calcula o total do carrinho e as taxas
        Double total = carrinho.stream().mapToDouble(Produto::getPreco).sum();
        Double taxas = total * empresa.getTaxa();

        // Gera um novo ID para a venda
        int vendaId = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCódigo() + 1;

        // Cria a nova venda
        Venda venda = new Venda(vendaId, new ArrayList<>(carrinho), total, taxas, empresa, cliente);

        // Atualiza o saldo da empresa
        empresa.setSaldo(empresa.getSaldo() + total);

        return venda;
    }
}
