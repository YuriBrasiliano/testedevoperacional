import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import model.*;
import banco.*;
import Repositorio.*;
import Venda.GerarVenda;

public class Main {

    // Declaração do repositório de usuários como estático para ser acessado em toda a classe
    public static RepositorioUsuario repositorioUsuario;

    public static void main(String[] args) {
        Banco banco = new Banco();
        repositorioUsuario = new RepositorioUsuario(banco);

        executar(banco.getUsuarios(), banco.getEmpresas(), banco.getProdutos(), banco.getCarrinho(), banco.getVendas());
    }

    // Função para executar o programa
    public static void executar(List<Usuario> usuarios, List<Empresa> empresas,
                                List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
        Scanner sc = new Scanner(System.in);

        // Verificar se o usuário está logado
        if (!repositorioUsuario.isLogged()) {
            System.out.println("Entre com seu usuário e senha:");
            System.out.print("Usuário: ");
            String username = sc.next();
            System.out.print("Senha: ");
            String senha = sc.next();
            
            // Tentativa de login
            ErroData<Usuario> loginResult = repositorioUsuario.login(username, senha);
            if (!loginResult.isSuccess()) {
                System.out.println(loginResult.getError());
                executar(usuarios, empresas, produtos, carrinho, vendas);
                return;
            }
        }

        Usuario usuario = repositorioUsuario.getUsuarioLogado();

        // Verificar se o usuário é administrador
        if (usuario.IsAdmin()) {
            executarAdmin(sc, usuarios, usuario, empresas, produtos, carrinho, vendas);
            return;
        }

        System.out.println("Escolha uma opção para iniciar");

        // Verificar se o usuário é uma empresa
        if (usuario.IsEmpresa()) {
            executarEmpresa(sc, usuarios, usuario, empresas, produtos, carrinho, vendas);
        } else {
            executarUsuario(sc, usuario, usuarios, empresas, produtos, carrinho, vendas);
        }
    }
		public static void executarAdmin(Scanner scanner, List<Usuario> usuarios, Usuario usuario,
                                 List<Empresa> empresas, List<Produto> produtos,
                                 List<Produto> carrinho, List<Venda> vendas) {
			System.out.println("1 - Visualizar Empresas");
			System.out.println("2 - Visualizar Pedidos");
			System.out.println("3 - Visualizar Usuários");
			System.out.println("0 - Deslogar");

    int opcao = scanner.nextInt(); // Captura a opção escolhida pelo administrador
    switch (opcao) {
        case 1 -> {
            System.out.println("Escolha a empresa para visualizar as informações: ");

            // Listando empresas
            if (empresas.isEmpty()) {
                System.out.println("Não existem empresas no nosso banco de dados.");
                executar(usuarios, empresas, produtos, carrinho, vendas);
                return;
            } else {
                for (Empresa empresa : empresas) {
                    System.out.println(empresa.getId() + " - " + empresa.getNome());
                }
            }

            int empresaId;
            Optional<Empresa> empresaEncontrada;

            do {
                empresaId = scanner.nextInt(); // Captura o ID da empresa escolhida
				int finalempresaId = empresaId;
                empresaEncontrada = empresas.stream()
                    .filter(empresaSelecionada -> empresaSelecionada.getId().equals(finalempresaId))
                    .findFirst();

                if (empresaEncontrada.isEmpty()) {
                    System.out.println("Empresa não encontrada, tente novamente");
                }
            } while (empresaEncontrada.isEmpty());

            System.out.println("Logado como empresa. Escolha uma opção para continuar: ");
            usuario.setEmpresa(empresaEncontrada.get()); // Define a empresa escolhida para o usuário
            executarEmpresa(scanner, usuarios, usuario, empresas, produtos, carrinho, vendas);
            return;
        }
        case 2 -> {
			List<Venda> vendasList = vendas;
			if(vendasList.isEmpty()){
				System.out.println("Nenhum pedido registrado");
				System.out.println("************************************************************");

			}

			for (Venda venda : vendasList) {
				System.out.println("************************************************************");
				System.out.println("Compra de código: " + venda.getCódigo() + " pelo cliente " +venda.getClienteUsername()+ " na empresa " + venda.getEmpresa().getNome() + ": ");
			
				// Listando produtos
				venda.getItens().forEach(product -> System.out.println(product.getId() + " - " + product.getNome() + "    R$" + product.getPreco()));
			
				System.out.println("Total: R$" + venda.getValor());
				System.out.println("************************************************************");
			}
			
					
				
        }
        case 3 -> {
            if (usuarios.isEmpty()) {
                System.out.println("Não existem usuários no nosso banco de dados.");
                executarAdmin(scanner, usuarios, usuario, empresas, produtos, carrinho, vendas);
                return;
            } else {
                for (Usuario user : usuarios) {
                    System.out.println("Username - " + user.getUsername());
                    if (user.IsCliente()) {
                        System.out.println("Usuário é um Cliente");
                        System.out.println("Nome - " + user.getCliente().getNome());
                        System.out.println("CPF - " + user.getCliente().getCpf());
                        System.out.println("Idade - " + user.getCliente().getIdade());
                    } else if (user.IsEmpresa()) {
                        System.out.println("Usuário é uma Empresa");
                        System.out.println("ID - " + user.getEmpresa().getId());
                        System.out.println("Nome - " + user.getEmpresa().getNome());
                        System.out.println("CNPJ - " + user.getEmpresa().getCnpj());
                        System.out.println("Saldo - " + user.getEmpresa().getSaldo());
                        System.out.println("Taxa Cobrada - " + user.getEmpresa().getTaxa());
                    } else {
                        System.out.println("Usuário é um Administrador");
                    }
                    System.out.println("************************************************************");
                }
            }
        }
		    case 4 -> {
			System.out.println("************************************************************");
			executarAdmin(scanner, usuarios, usuario, empresas, produtos, carrinho, vendas);
        }
        case 0 -> {
            repositorioUsuario.logout();
            executar(usuarios, empresas, produtos, carrinho, vendas); // Volta ao menu principal
            return;
        }
    }

    // Se nenhuma opção válida foi escolhida, chama a função executarAdmin novamente
    executarAdmin(scanner, usuarios, usuario, empresas, produtos, carrinho, vendas);
}

		public static void executarEmpresa(Scanner scanner, List<Usuario> usuarios, Usuario usuario, List<Empresa> empresas,
								  List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
		System.out.println("1 - Listar vendas");
		System.out.println("2 - Ver produtos");
		System.out.println("3 - Menu Principal");
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
				    
			case 3 -> {
			System.out.println("************************************************************");
			if(usuario.getUsername() != "admin"){
				executarEmpresa(scanner, usuarios, usuario, empresas, produtos, carrinho, vendas);
				return;
        }else{
		executarAdmin(scanner, usuarios, usuario, empresas, produtos, carrinho, vendas);}
        }
			case 0 -> {
				if (repositorioUsuario.getUsuarioLogado().IsAdmin()) repositorioUsuario.getUsuarioLogado().setEmpresa(null);

				repositorioUsuario.logout();
				executar(usuarios, empresas, produtos, carrinho, vendas);
				return;
			}
		}

		executarEmpresa(scanner, usuarios, usuario, empresas, produtos, carrinho, vendas);
	}

		public static void executarUsuario(Scanner scanner, Usuario usuario,
									  List<Usuario> usuarios, List<Empresa> empresas,
									  List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
		System.out.println("1 - Relizar Compras");
		System.out.println("2 - Ver Compras");
		System.out.println("0 - Deslogar");

		switch (scanner.nextInt()) {
			case 1 -> {
				System.out.println("Para realizar uma compra, escolha a empresa onde deseja comprar: ");

				// listando empresas
				if (empresas.isEmpty()) {
					System.out.println("Não existem empresas no nosso banco de dados.");

					executar(usuarios, empresas, produtos, carrinho, vendas);
					return;
				} else empresas.forEach(empresa -> System.out.println(empresa.getId() + " - " + empresa.getNome()));

				Integer empresaId = scanner.nextInt();
				int produtoId;

				// escolhendo produtos
				do {
					System.out.println("Escolha os seus produtos: ");
					System.out.println("Para finalizar a compra digite 0");

					// listando produtos
					List<Produto> ProdutosEmpresa = produtos.stream().filter(produto -> produto.getEmpresa().getId().equals(empresaId)).toList();
					if (ProdutosEmpresa.isEmpty()) System.out.println("Essa empresa não possui nenhum produto.");
					else {
						for (Produto produto : ProdutosEmpresa) {
							System.out.println(produto.getId() + " - " + produto.getNome());
						}
					}

					// adicionando produto escolhido para o carrinho
					produtoId = scanner.nextInt();
					int finalprodutoId = produtoId;

					produtos.stream().filter(ProdutoSelecionado -> ProdutoSelecionado.getId().equals(finalprodutoId))
							.findFirst()
							.ifPresent(produtoSelecionado -> {
								if (produtoSelecionado.getQuantidade() > 0) {
									carrinho.add(produtoSelecionado);
									produtoSelecionado.setQuantidade(produtoSelecionado.getQuantidade() - 1);
								} else {
									System.out.println("Produto fora de estoque: " + produtoSelecionado.getNome());
								}
							});
				} while (produtoId != 0);

				System.out.println("************************************************************");
				System.out.println("Resumo da compra: ");

				// listando produtos
				List<Produto> ProdutosComprados = carrinho.stream().filter(produto -> produto.getEmpresa().getId().equals(empresaId)).toList();
				if (ProdutosComprados.isEmpty()) System.out.println("Você não comprou nada");
				else {
					for (Produto produto : ProdutosComprados) {
						System.out.println(produto.getId() + " - " + produto.getNome() + "    R$" + produto.getPreco());
					}
				}

				Optional<Empresa> EmpresasEncontradas = empresas.stream().filter(empresaSelecionada -> empresaSelecionada.getId().equals(empresaId)).findFirst();
				if (EmpresasEncontradas.isPresent()) {
					Cliente cliente = usuario.getCliente();
					cliente.setUsername(usuario.getUsername());
					Venda venda = new GerarVenda().criarVenda(EmpresasEncontradas.get(), cliente, vendas, carrinho);
					System.out.println("Total: R$" + venda.getValor());
					System.out.println("************************************************************");
					carrinho.clear();
					vendas.add(venda);
				}
				else {
					System.out.println("Empresa não encontrada, tente novamente");
				}
			}
			case 2 -> {
				System.out.println();
				System.out.println("************************************************************");
				System.out.println("COMPRAS EFETUADAS");

				// listando vendas
				List<Venda> VendasUsuario = vendas.stream().filter(venda -> venda.getClienteUsername().equals(usuario.getUsername())).toList();
				if (VendasUsuario.isEmpty()) {System.out.println("Nenhuma compra foi efetuada até o momento.");
				System.out.println("************************************************************");}
				else {
					for (Venda venda : VendasUsuario) {
						System.out.println("************************************************************");
						System.out.println("Compra de código: " + venda.getCódigo() + " na empresa " + venda.getEmpresa().getNome() + ": ");

						// listando produtos
						venda.getItens().forEach(product -> System.out.println(product.getId() + " - " + product.getNome() + "    R$" + product.getPreco()));

						System.out.println("Total: R$" + venda.getValor());
						System.out.println("************************************************************");
					}
				}
			}
			case 0 -> repositorioUsuario.logout();
		}

		executar(usuarios, empresas, produtos, carrinho, vendas);
	}

}