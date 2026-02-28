public class ProdutoNaoPerecivel extends Produto {
    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro){
        super(desc, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String desc, double precoCusto){
        super(desc, precoCusto);
    }

    @Override
    public double valorVenda(){
        return precoCusto * (1 + margemLucro);
    }

    @Override
    public String gerarDadosTexto() {
        String precoFormatado = String.format("%2.f", precoCusto).replace(".", ",");
        String margemFormatada = String.format("%2.f", margemLucro).replace(".", ",");
        return String.format("1;%s;%2.f;%2.f", descricao, precoFormatado, margemFormatada);
    }
}
