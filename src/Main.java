import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

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
	}