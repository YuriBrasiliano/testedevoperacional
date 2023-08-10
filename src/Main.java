import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import model.*;
import banco.*;
import repositorios.*;

public class Main {

	public static RepositorioUsuario repositorioUsuario;

	public static void main(String[] args) {
		Banco banco = new Banco();
		repositorioUsuario = new RepositorioUsuario(banco);

		executar(banco.getUsuarios(), banco.getEmpresas(), banco.getProdutos(), banco.getCarrinho(), banco.getVendas());
	}

	public static void executar(List<Usuario> usuarios, List<Empresa> empresas,
			List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
		Scanner sc = new Scanner(System.in);
	
		if (!repositorioUsuario.isLogged()) {
			System.out.println("Entre com seu usuário e senha:");
			System.out.print("Usuário: ");
			String username = sc.next();
			System.out.print("Senha: ");
			String senha = sc.next();
			ErroData<Usuario> loginResult = repositorioUsuario.login(username, senha);
				if (!loginResult.isSuccess()) {
					System.out.println(loginResult.getError());
					executar(usuarios, empresas, produtos, carrinho, vendas);
					return;
			}
		}
		Usuario usuario = repositorioUsuario.getUsuarioLogado();

		if (usuario.IsAdmin()) {
			executarAdmin(sc, usuario, usuarios, empresas, produtos, carrinho, vendas);
			return;
		}
		System.out.println("Escolha uma opção para iniciar");
		if (usuario.IsEmpresa()) executarEmpresa(sc, usuario, usuarios, empresas, produtos, carrinho, vendas);
	}
		
		public static void executarAdmin(Scanner scanner, Usuario usuario,
		List<Usuario> usuarios, List<Empresa> empresas,
		List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
			System.out.println("1 - Visualizar uma empresa");
			System.out.println("0 - Deslogar");

			switch (scanner.nextInt()) {
			case 1 -> {
			System.out.println("Escolha a empresa para visualizar as informações: ");

			// listando empresas
			if (empresas.isEmpty()) {
			System.out.println("Não existem empresas no nosso banco de dados.");
			executar(usuarios, empresas, produtos, carrinho, vendas);
			return;
			}else {
				for (Empresa empresa : empresas) {
					System.out.println(empresa.getId() + " - " + empresa.getNome());
				}
			

			int empresaId;
			Optional<Empresa> EmpresasEncontradas;

			do {
				empresaId = scanner.nextInt();
				int finalempresaId = empresaId;
				EmpresasEncontradas = empresas.stream()
					.filter(empresaSelecionada -> empresaSelecionada.getId().equals(finalempresaId))
					.findFirst();
			
				if (EmpresasEncontradas.isEmpty()) {
					System.out.println("Empresa não encontrada, tente novamente");
				}
			} while (EmpresasEncontradas.isEmpty());
			

			System.out.println("Logado como empresa. Escolha uma opção para continuar: ");

				usuario.setEmpresa(EmpresasEncontradas.get());
				executarEmpresa(scanner, usuario, usuarios, empresas, produtos, carrinho, vendas);
				return;
			}}
			case 0 -> {
				repositorioUsuario.logout();
				executar(usuarios, empresas, produtos, carrinho, vendas);
				return;
			}
		}

		executarAdmin(scanner, usuario, usuarios, empresas, produtos, carrinho, vendas);	

		}
		public static void executarEmpresa(Scanner scanner, Usuario usuario,
								  List<Usuario> usuarios, List<Empresa> empresas,
								  List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
		System.out.println("1 - Listar vendas");
		System.out.println("2 - Ver produtos");
		System.out.println("0 - Deslogar");

		switch (scanner.nextInt()) {
			case 1 -> {
				System.out.println();
				System.out.println("************************************************************");
				System.out.println("VENDAS EFETUADAS");

				List<Venda> VendasEmpresa = vendas.stream().filter(venda -> venda.getEmpresa().getId().equals(usuario.getEmpresa().getId())).toList();
				if (VendasEmpresa.isEmpty()) System.out.println("Essa empresa ainda não vendeu nenhum produto.");
				else {
					for (Venda venda : VendasEmpresa) {
						// listando vendas
						System.out.println("************************************************************");
						System.out.println("Venda de código: " + venda.getCódigo() + " no CPF " + venda.getCliente().getCpf() + ": ");
						venda.getItens().forEach(produto -> System.out.println(produto.getId() + " - " + produto.getNome() + "    R$" + produto.getPreco()));

						// total
						System.out.println("Total: R$" + venda.getValor());
						System.out.println("Taxa a ser paga: R$" + venda.getComissaoSistema());
						System.out.println("Total Líquido  para empresa: R$" + (venda.getValor() - venda.getComissaoSistema()));
						System.out.println("************************************************************");
					}
				}

				// listando saldo
				System.out.println("Saldo da empresa: " + usuario.getEmpresa().getSaldo());
				System.out.println("************************************************************");
			}
			case 2 -> {
				System.out.println();
				System.out.println("************************************************************");
				System.out.println("MEUS PRODUTOS");

				// listando produtos
				List<Produto> ProdutosEmpresa = produtos.stream().filter(produto -> produto.getEmpresa().getId().equals(usuario.getEmpresa().getId())).toList();
				if (ProdutosEmpresa.isEmpty()) System.out.println("Essa empresa ainda não possui nenhum produto.");
				else {
					for (Produto produto : ProdutosEmpresa) {
						// informações do produto
						System.out.println("************************************************************");
						System.out.println("Código: " + produto.getId());
						System.out.println("Produto: " + produto.getNome());
						System.out.println("Quantidade em estoque: " + produto.getQuantidade());
						System.out.println("Valor: R$" + produto.getPreco());
						System.out.println("************************************************************");
					}
				}

				System.out.println("Saldo da empresa: " + usuario.getEmpresa().getSaldo());
				System.out.println("************************************************************");
			}
			case 0 -> {
				if (repositorioUsuario.getUsuarioLogado().IsAdmin()) repositorioUsuario.getUsuarioLogado().setEmpresa(null);

				repositorioUsuario.logout();
				executar(usuarios, empresas, produtos, carrinho, vendas);
				return;
			}
		}

		executarEmpresa(scanner, usuario, usuarios, empresas, produtos, carrinho, vendas);
	}

	}