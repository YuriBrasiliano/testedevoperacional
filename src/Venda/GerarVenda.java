package Venda;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class GerarVenda {

    public Venda criarVenda(Empresa empresa, Cliente cliente, List<Venda> vendas, List<Produto> carrinho) {
        // Calcula o valor total da venda e as taxas
        Double total = carrinho.stream().mapToDouble(Produto::getPreco).sum();
        Double taxas = total * empresa.getTaxa();

        // Cria a venda
        int vendaId = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCódigo() + 1;
        Venda venda = new Venda(vendaId, new ArrayList<>(carrinho), total, taxas, empresa, cliente);

        // Atualiza o saldo da empresa
        empresa.setSaldo(empresa.getSaldo() + total - taxas);

        return venda;
    }
}
