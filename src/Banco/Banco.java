package Banco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.*;

public class Banco {

    // Lista de produtos no carrinho
    private final List<Produto> carrinho = new ArrayList<>();
    // Lista de vendas realizadas
    private final List<Venda> vendas = new ArrayList<>();
    // Mapeamento de empresas por ID
    private final Map<Integer, Empresa> empresas = new HashMap<>();
    // Lista de usuários
    private final List<Usuario> usuarios = new ArrayList<>();
    // Lista de produtos disponíveis
    private final List<Produto> produtos = new ArrayList<>();

    // Construtor da classe Banco
    public Banco() {
        addEmpresas();
        addProdutos();
        addUsuarios();
    }

    // Adiciona empresas ao map de empresas
    private void addEmpresas() {
        empresas.put(1, new Empresa(1, "Level Varejo", "53239160000154", 0.05, 0.0));
        empresas.put(2, new Empresa(2, "SafeWay Padaria", "30021423000159", 0.15, 0.0));
        empresas.put(3, new Empresa(3, "SafeWay Restaurante", "41361511000116", 0.20, 0.0));
    }

    // Adiciona produtos à lista de produtos
    private void addProdutos() {
        // Associa empresas aos produtos e adiciona à lista de produtos
        produtos.add(new Produto(1, "Pão Frances", 5, 3.50, empresas.get(2)));
        produtos.add(new Produto(2, "Coturno", 10, 400.0, empresas.get(1)));
        produtos.add(new Produto(3, "Jaqueta Jeans", 15, 150.0, empresas.get(1)));
        produtos.add(new Produto(4, "Calça Sarja", 15, 150.0, empresas.get(1)));
        produtos.add(new Produto(5, "Prato feito - Frango", 10, 25.0, empresas.get(3)));
        produtos.add(new Produto(6, "Prato feito - Carne", 10, 25.0, empresas.get(3)));
        produtos.add(new Produto(7, "Suco Natural", 30, 10.0, empresas.get(3)));
        produtos.add(new Produto(8, "Sonho", 5, 8.50, empresas.get(2)));
        produtos.add(new Produto(9, "Croissant", 7, 6.50, empresas.get(2)));
        produtos.add(new Produto(10, "Chá Gelado", 4, 5.50, empresas.get(2)));    
    
    }

     // Adiciona usuários à lista de usuários
    private void addUsuarios() {
        // Criação de objetos Cliente para usuários específicos
        Cliente cliente = new Cliente("07221134049", "Allan da Silva", "cliente", 20);
        Cliente cliente2 = new Cliente("72840700050", "Samuel da Silva", "cliente2", 24);

        usuarios.add(new Usuario("admin", "admin123", null, null));
        usuarios.add(new Usuario("empresa", "level123", null, empresas.get(1)));
        usuarios.add(new Usuario("empresa2", "safewayp123", null, empresas.get(2)));
        usuarios.add(new Usuario("empresa3", "safewayr123", null, empresas.get(3)));
        usuarios.add(new Usuario("Allan", "Allan123", cliente, null));
        usuarios.add(new Usuario("Samuel", "Samuel123", cliente2,null));
    }

    public List<Produto> getCarrinho() {
        return carrinho;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Empresa> getEmpresas() {
        return new ArrayList<>(empresas.values());
    }

    public List<Produto> getProdutos() {
        return produtos;
    }
}
