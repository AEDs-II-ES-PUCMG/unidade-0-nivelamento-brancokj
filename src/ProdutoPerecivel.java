import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ProdutoPerecivel extends Produto {
    private static double DESCONTO = 0.25;
    private static int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade, int quantidadeEmEstoque) {
        super(desc, precoCusto, margemLucro, quantidadeEmEstoque);
        if (validade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException();
        }
        this.dataDeValidade = validade;
    }

    @Override
    public double valorVenda() {
        long diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), dataDeValidade);
        double preco = precoCusto * (1 + margemLucro);

        if (diasParaVencer <= PRAZO_DESCONTO) {
            return preco * (1 - DESCONTO);
        }
        return preco;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString() +  "\nVálido até " + formato.format(dataDeValidade);
    }

    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String precoFormatado = String.format("%.2f", precoCusto).replace(".", ",");
        String margemFormatada = String.format("%.2f", margemLucro).replace(".", ",");
        return String.format("2;%s;%s;%s;%s;%i", descricao, precoFormatado, margemFormatada,
                this.dataDeValidade.format(formatador), getquantidadeEmEstoque());
    }
}
