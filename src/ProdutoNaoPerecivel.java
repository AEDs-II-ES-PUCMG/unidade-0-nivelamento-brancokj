public class ProdutoNaoPerecivel extends Produto {
    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro, int quantidadeEmEstoque){
        super(desc, precoCusto, margemLucro, quantidadeEmEstoque);
    }

    public ProdutoNaoPerecivel(String desc, double precoCusto, int quantidadeEmEstoque){
        super(desc, precoCusto, quantidadeEmEstoque);
    }

    @Override
    public double valorVenda(){
        return precoCusto * (1 + margemLucro);
    }

    @Override
    public String gerarDadosTexto() {
        String precoFormatado = String.format("%.2f", precoCusto).replace(".", ",");
        String margemFormatada = String.format("%.2f", margemLucro).replace(".", ",");
        return String.format("1;%s;%s;%s;%i", descricao, precoFormatado, margemFormatada, getquantidadeEmEstoque());
    }
}
