public class ItemDePedido {

    // Atributos encapsulados
    protected Produto produto;
    private int quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda deve ser capturado do produto no momento da criação do item,
     * garantindo que alterações futuras no preço do produto não afetem este pedido.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
        if (produto.equals(null)){
            throw new IllegalArgumentException();
        }
        if(quantidade == 0 || quantidade < 0 || precoVenda == 0 || precoVenda < 0){
            throw new IllegalArgumentException();
        }
        this.produto = produto;
        if(this.produto.verificaSePodeReduzir(quantidade)){
            this.quantidade = quantidade;
        }else{
            throw new IllegalArgumentException();
        }
        
        this.precoVenda = precoVenda;
    }

    public double calcularSubtotal() {
        double total = 0d;
        total = precoVenda * quantidade;
        return total;
    }

    // --- Sobrescrita do método equals ---

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto.
     */
    @Override
    public boolean equals(Object obj) {
        boolean param = false;
        ItemDePedido outro = (ItemDePedido)obj;
        if(this.produto.equals(outro.produto)){
            param = true;
        }
        return param;
    }
}
