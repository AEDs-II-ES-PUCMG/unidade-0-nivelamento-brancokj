import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /**
     * Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto
     */
    static String nomeArquivoDados;

    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /**
     * Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a
     * cada execução
     */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /**
     * Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma
     * classe Menu.
     * 
     * @return Um inteiro com a opção do usuário.
     */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no
     * formato
     * N (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em
     * caso de problemas com o arquivo.
     * 
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de
     *         leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        quantosProdutos = 0;
        try (Scanner arq = new Scanner(new File(nomeArquivoDados))) {
            if (!arq.hasNextLine())
                return vetorProdutos;
            int qtd = Integer.parseInt(arq.nextLine().trim());
            for (int i = 0; i < qtd && i < MAX_NOVOS_PRODUTOS && arq.hasNextLine(); i++) {
                String linha = arq.nextLine();
                try {
                    Produto p = Produto.criarDoTexto(linha);
                    vetorProdutos[quantosProdutos++] = p;
                } catch (Exception e) {
                    // Linha errada
                }
            }
        } catch (FileNotFoundException e) {
            // rquivo inexistente
        }
        return vetorProdutos;
    }

    static void listarTodosOsProdutos() {
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if (produtosCadastrados[i] != null)
                System.out.println(String.format("%02d - %s", (i + 1), produtosCadastrados[i].toString()));
        }
    }

    /**
     * Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus
     * dados.
     * A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime
     * mensagem padrão
     */
    static void localizarProdutos() {
        System.out.println("Nome do produto a buscar:");
        String nome = teclado.nextLine();
        boolean achou = false;
        for (Produto p : produtosCadastrados) {
            if (p != null && p.descricao.equalsIgnoreCase(nome)) {
                System.out.println(p);
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Produto não encontrado.");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto,
     * lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método
     * pode ser feito com um nível muito
     * melhor de modularização. As diversas fases da lógica poderiam ser
     * encapsuladas em outros métodos.
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão
     * Factory Method para criação dos objetos.
     */
    static void cadastrarProduto() {
        System.out.println("Qual o tipo do produto?\n1- Não Perecível, 2- Perecível");
        int param = teclado.nextInt();
        teclado.nextLine();
        if (quantosProdutos >= MAX_NOVOS_PRODUTOS) {
            System.out.println("Erro: Limite máximo de " + MAX_NOVOS_PRODUTOS + " produtos atingido!");
            return;
        }

        if (param > 2 || param < 1) {
            System.out.println("Número digitado incorretamente");
            pausa();
            return;
        }

        Object[] dados = coletarDadosProdutos();
        String nome = (String) dados[0];
        double precoDeCusto = (double) dados[1];
        double margemDeLucro = (double) dados[2];

        if (param == 1) {
            ProdutoNaoPerecivel p1;
            if (margemDeLucro == 0d) {
                p1 = new ProdutoNaoPerecivel(nome, precoDeCusto);
            } else {
                p1 = new ProdutoNaoPerecivel(nome, precoDeCusto, margemDeLucro);
            }
            produtosCadastrados[quantosProdutos] = p1;
        } else {
            ProdutoPerecivel p1;
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Qual a data de validade? - digite no padrão dd/mm/yyyy");
            String data = teclado.nextLine();
            LocalDate dataDeValidade = null;
            try {
                dataDeValidade = LocalDate.parse(data, formatador);
            } catch (Exception e) {
                System.out.println("Dado digitado incorretamente");
                pausa();
                return;
            }
            if (margemDeLucro == 0d) {
                System.out.println("Produto perecível precisa ter margem de lucro");
                pausa();
                return;
            }
            p1 = new ProdutoPerecivel(nome, precoDeCusto, margemDeLucro, dataDeValidade);
            produtosCadastrados[quantosProdutos] = p1;
        }

        quantosProdutos++;
        System.out.println("Produto cadastrado com sucesso!");
    }

    static Object[] coletarDadosProdutos() {
        String nome = "";
        double precoDeCusto = 0d;
        double margemDeLucro = 0d;

        try {
            System.out.println("Qual o nome do produto?");
            nome = teclado.nextLine();
            System.out.println("Qual o preço de custo?");
            precoDeCusto = teclado.nextDouble();
            System.out.println("Qual a margem de lucro?");
            margemDeLucro = teclado.nextDouble();
            teclado.nextLine();
            margemDeLucro /= 100;
        } catch (Exception e) {
            System.out.println("Dado digitado incorretamente");
            pausa();
        }

        return new Object[] { nome, precoDeCusto, margemDeLucro };
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve
     * todo o conteúdo do arquivo.
     * 
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        int contador = 0;
        for (Produto p : produtosCadastrados) {
            if (p != null)
                contador++;
        }
        try (FileWriter escritor = new FileWriter(nomeArquivo, false)) {
            escritor.write(String.valueOf(contador));
            escritor.write("\n");
            for (Produto p : produtosCadastrados) {
                if (p != null) {
                    String dados = p.gerarDadosTexto();
                    escritor.write(dados);
                    escritor.write("\n");
                }
            }
            escritor.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}
