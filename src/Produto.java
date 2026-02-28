import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Produto {
	private static final double MARGEM_PADRAO = 0.2;

	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;

	// Construtor com margem de lucro informada
	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}

	// Construtor utilizando margem padrão
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}

	// Método auxiliar de inicialização
	private void init(String desc, double precoCusto, double margemLucro) {
		this.descricao = desc;
		if (precoCusto <= 0) {
			throw new IllegalArgumentException();
		}
		this.precoCusto = precoCusto;
		if (margemLucro <= 0) {
			throw new IllegalArgumentException();
		}
		this.margemLucro = margemLucro;
	}

	// Calcula o valor de venda com base no custo e na margem
	public double valorVenda() {
		return precoCusto * (1 + margemLucro);
	}

	@Override
	public String toString() {
		return "Produto{" +
				"descricao='" + descricao + '\'' + "R$" +
				", precoCusto=" + String.format("%.2f", precoCusto) +
				", margemLucro=" + margemLucro +
				", valorVenda=" + String.format("%.2f", valorVenda()) +
				'}';
	}

	/**
	 * Igualdade de produtos: caso possuam o mesmo nome/descrição.
	 * 
	 * @param obj Outro produto a ser comparado
	 * @return booleano true/false conforme o parâmetro possua a descrição igual ou
	 *         não a este produto.
	 */
	@Override
	public boolean equals(Object obj) {
		Produto outro = (Produto) obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}

	/**
	 * Gera uma linha de texto a partir dos dados do produto
	 * 
	 * @return Uma string no formato "tipo;
	 *         descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	 */
	public abstract String gerarDadosTexto();

	/**
	 * Cria um produto a partir de uma linha de dados em formato texto. A linha de
	 * dados deve estar de acordo com a
	 * formatação
	 * "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	 * ou o funcionamento não será garantido. Os tipos são 1 para produto não
	 * perecível e 2 para perecível.
	 * 
	 * @param linha Linha com os dados do produto a ser criado.
	 * @return Um produto com os dados recebidos
	 */
	static Produto criarDoTexto(String linha) {
		Produto novoProduto = null;
		String[] linhaSplit = linha.split(";");
		if(linhaSplit.length < 3){
			throw new IllegalArgumentException();
		}
		int tipo = Integer.parseInt(linhaSplit[0].trim());
		String desc = linhaSplit[1].trim();
		double precoCusto = Double.parseDouble(linhaSplit[2].trim());
		double margemLucro =  Double.parseDouble(linhaSplit[3].trim());

		if(tipo == 1){
			novoProduto = new ProdutoNaoPerecivel(desc, precoCusto, margemLucro);
		}else{
			String dataStr = linhaSplit[4].trim();
			DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dataValidade = LocalDate.parse(dataStr, formatador);
			novoProduto = new ProdutoPerecivel(desc, precoCusto, margemLucro, dataValidade);
		}

		return novoProduto;
	}
}
