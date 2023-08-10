package Repositorio;

/**
 * Classe que representa um objeto para tratamento de erros e resultados de operações.
 *
 * @param <T> Tipo de valor associado ao erro.
 */
public class ErroData<T> {

    private boolean sucesso = false; // Indica se a operação foi bem-sucedida.
    private String erro; // Mensagem de erro, caso a operação tenha falhado.
    private T value; // Valor associado à operação, se aplicável.

    /**
     * Verifica se a operação foi bem-sucedida.
     *
     * @return true se a operação foi bem-sucedida, false caso contrário.
     */
    public boolean isSuccess() {
        return sucesso;
    }

    /**
     * Define o status de sucesso da operação.
     *
     * @param sucesso true para indicar sucesso, false caso contrário.
     */
    public void setSuccess(boolean sucesso) {
        this.sucesso = sucesso;
    }

    /**
     * Obtém a mensagem de erro, caso a operação tenha falhado.
     *
     * @return Mensagem de erro, ou null se a operação foi bem-sucedida.
     */
    public String getError() {
        return erro;
    }

    /**
     * Define a mensagem de erro associada à operação.
     *
     * @param erro Mensagem de erro.
     */
    public void setError(String erro) {
        this.erro = erro;
    }

    /**
     * Obtém o valor associado à operação, se aplicável.
     *
     * @return O valor associado à operação, ou null se não houver valor.
     */
    public T getValue() {
        return value;
    }

    /**
     * Define o valor associado à operação.
     *
     * @param value O valor a ser associado.
     */
    public void setValue(T value) {
        this.value = value;
    }
}
