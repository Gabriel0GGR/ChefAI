import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChefAI {
    private Usuario usuario;
    private QuickRecipeSuggester quickSuggester;
    private VegetarianRecipeSuggester vegetarianSuggester;
    
    public ChefAI() {
        this.usuario = new Usuario();
        this.quickSuggester = new QuickRecipeSuggester(new LLMClient());
        this.vegetarianSuggester = new VegetarianRecipeSuggester(new LLMClient());
    }
    
    public void iniciarAplicacao() throws ChefAIException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Bem-vindo ao ChefAI ===");
        System.out.println("Vamos começar cadastrando seus ingredientes disponíveis...\n");
        
        // Cadastrar ingredientes
        cadastrarIngredientes(scanner);
        
        // Configurar restrições alimentares
        configurarRestricoesAlimentares(scanner);
        
        // Obter sugestões de receitas
        obterSugestoesReceitas(scanner);
    }
    
    private void cadastrarIngredientes(Scanner scanner) {
        System.out.println("Adicione os ingredientes que você tem em casa:");
        while (true) {
            System.out.print("Nome do ingrediente (ou 'fim' para terminar): ");
            String nome = scanner.nextLine();
            
            if ("fim".equalsIgnoreCase(nome)) break;
            
            try {
                System.out.print("Quantidade em gramas: ");
                double quantidade = Double.parseDouble(scanner.nextLine());
                
                Ingrediente ingrediente = new Ingrediente(nome, quantidade);
                usuario.adicionarIngrediente(ingrediente);
                System.out.println("Ingredientes adicionado: " + ingrediente);
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida. Tente novamente.");
            }
        }
    }
    
    private void configurarRestricoesAlimentares(Scanner scanner) throws ChefAIException {
        try {
            System.out.println("\nDeseja aplicar algum filtro especial?");
            
            System.out.print("Vegetariano? (s/n): ");
            String resp = scanner.nextLine();
            usuario.setVegetariano(resp.equalsIgnoreCase("s"));
            
            System.out.print("Sem lactose? (s/n): ");
            resp = scanner.nextLine();
            usuario.setSemLactose(resp.equalsIgnoreCase("s"));
            
            System.out.print("Sem glúten? (s/n): ");
            resp = scanner.nextLine();
            usuario.setSemGluten(resp.equalsIgnoreCase("s"));
        } catch (Exception e) {
            throw new ChefAIException("Erro ao configurar restrições alimentares: " + e.getMessage());
        }
    }
    
    private void obterSugestoesReceitas(Scanner scanner) throws ChefAIException {
        try {
            System.out.println("\n=== Gerando sugestões de receitas ===");
            
            List<Ingrediente> ingredientes = usuario.getIngredientesDisponiveis();
            boolean vegetariano = usuario.isVegetariano();
            boolean semLactose = usuario.isSemLactose();
            boolean semGluten = usuario.isSemGluten();
            
            if (ingredientes.isEmpty()) {
                throw new ChefAIException("Nenhum ingrediente cadastrado.");
            }
            
            List<Receita> sugestoes = quickSuggester.sugerir(ingredientes, vegetariano, semLactose, semGluten);
            
            System.out.println("\n=== Sugestões de Receitas ===");
            int count = 1;
            for (Receita receita : sugestoes) {
                if (count > 3) break; // Mostra apenas as primeiras 3
                
                System.out.println("\n--- Receita " + count + " ---");
                System.out.println("Nome: " + receita.getNome());
                System.out.println("Tempo de preparo estimado: " + receita.getTempoPreparo() + " minutos");
                
                System.out.println("\nIngredientes necessários:");
                for (Ingrediente ing : receita.getIngredientesNecessarios()) {
                    boolean tem = false;
                    for (Ingrediente disp : usuario.getIngredientesDisponiveis()) {
                        if (disp.getNome().equalsIgnoreCase(ing.getNome())) {
                            tem = true;
                            break;
                        }
                    }
                    System.out.println("  " + ing.toString() + 
                          (tem ? " ✓ Você já tem" : " ✗ Necessário"));
                }
                
                System.out.println("\nModo de preparo:");
                String[] passos = receita.getModoPreparo().split("\n");
                for (int i = 0; i < passos.length; i++) {
                    System.out.println("  " + (i+1) + ". " + passos[i]);
                }
                
                count++;
            }
        } catch (Exception e) {
            throw new ChefAIException("Erro ao obter sugestões: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {
            // Verificar se a chave está configurada
            ConfigManager.carregarConfiguracao();
            
            String apiKey = ConfigManager.getApiKey();
            if (apiKey.isEmpty() || apiKey.equals("YOUR_XAI_API_KEY_HERE")) {
                System.out.println("\nATENÇÃO: Para usar a API xAI:");
                System.out.println("1. Crie um arquivo config.txt na raiz do projeto");
                System.out.println("2. Adicione a linha: xai_api_key=SUA_CHAVE_AQUI");
                System.out.println("3. Recarregue o projeto no BlueJ e execute novamente");
            }
            
            ChefAI chefai = new ChefAI();
            chefai.iniciarAplicacao();
        } catch (ChefAIException e) {
            System.err.println("Erro no ChefAI: " + e.getMessage());
        }
    }
}
