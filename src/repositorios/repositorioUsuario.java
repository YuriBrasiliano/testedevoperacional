package repositorios;

import java.util.Optional;

import banco.Banco;
import model.Usuario;

/**
 * Repositório de gerenciamento de usuários.
 */
public class repositorioUsuario {

    private final Banco banco;
    private Usuario usuarioLogado;

    /**
     * Construtor para o RepositorioUsuario.
     *
     * @param banco O banco de dados.
     */
    public repositorioUsuario(Banco banco) {
        this.banco = banco;
    }

    /**
     * Verifica se um usuário está logado.
     *
     * @return Verdadeiro se um usuário estiver logado, falso caso contrário.
     */
    public boolean isLogged() {
        return usuarioLogado != null;
    }

    /**
     * Efetua o login de um usuário.
     *
     * @param username O nome de usuário.
     * @param password A senha do usuário.
     * @return ErroData contendo informações sobre o sucesso do login ou erro.
     */
    public ErroData<Usuario> login(String username, String password) {
        ErroData<Usuario> result = new ErroData<>();

        Optional<Usuario> foundUsuario = banco.getUsuarios().stream()
                .filter(usuario -> usuario.getUsername().equals(username))
                .findFirst();

        if (foundUsuario.isPresent()) {
            Usuario usuario = foundUsuario.get();
            if (!usuario.getSenha().equals(password)) {
                result.setSuccess(false);
                result.setError("Senha incorreta, tente novamente");
            } else {
                result.setSuccess(true);
                result.setValue(usuario);
                usuarioLogado = usuario;
            }
        } else {
            result.setSuccess(false);
            result.setError("Usuário não encontrado");
        }

        return result;
    }

    /**
     * Realiza o logout do usuário.
     */
    public void logout() {
        usuarioLogado = null;
    }

    /**
     * Obtém o usuário atualmente logado.
     *
     * @return O usuário logado ou nulo se nenhum usuário estiver logado.
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
