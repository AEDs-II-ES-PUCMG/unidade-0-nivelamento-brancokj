import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ProdutoPerecivel extends Produto {
    private static int tipo = 1;
    private static double DESCONTO = 0.25;
    private static int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
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
        String precoFormatado = String.format("%2.f", precoCusto).replace(".", ",");
        String margemFormatada = String.format("%2.f", margemLucro).replace(".", ",");
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("1;%s;%2.f;%2.f", descricao, precoFormatado, margemFormatada, this.dataDeValidade.format(formatador));
    }
}
