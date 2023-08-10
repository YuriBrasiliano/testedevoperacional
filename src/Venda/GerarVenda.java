package Venda;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GerarVenda {

    public Venda criarVenda(Empresa empresa, Cliente cliente, List<Venda> vendas, List<Produto> carrinho) {
        // Calcula o valor total da venda e as taxas
        Double total = carrinho.stream().mapToDouble(Produto::getPreco).sum();
        Double taxas = total * empresa.getTaxa();

        // Verifica o estoque antes de criar a venda
        for (Produto produto : carrinho) {
            if (produto.getQuantidade() < 1) {
                System.out.println("Não há estoque suficiente para o produto: " + produto.getNome());
                return null;
            }
        }

        // Cria a venda
        int vendaId = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCódigo() + 1;
        Venda venda = new Venda(vendaId, new ArrayList<>(carrinho), total, taxas, empresa, cliente);

        // Atualiza o estoque após a venda
        for (Produto produto : carrinho) {
            produto.setQuantidade(produto.getQuantidade() - 1);
        }

        // Atualiza o saldo da empresa
        empresa.setSaldo(empresa.getSaldo() + total - taxas);

        return venda;
    }
}
