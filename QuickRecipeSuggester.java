import java.util.List;

public class QuickRecipeSuggester extends RecipeSuggester {
    public QuickRecipeSuggester(LLMClient client) {
        super(client);
    }
    
    @Override
    public List<Receita> sugerir(List<Ingrediente> ingredientes, 
                                boolean vegetariano, boolean semLactose, boolean semGluten) {
        return client.obterSugestoes(ingredientes, vegetariano, semLactose, semGluten);
    }
}
