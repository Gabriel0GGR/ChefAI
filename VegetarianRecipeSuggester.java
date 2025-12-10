import java.util.List;

public class VegetarianRecipeSuggester extends RecipeSuggester {
    public VegetarianRecipeSuggester(LLMClient client) {
        super(client);
    }
    
    @Override
    public List<Receita> sugerir(List<Ingrediente> ingredientes, 
                                boolean vegetariano, boolean semLactose, boolean semGluten) {
        // Forçar vegetariano para esse tipo de sugestão
        return client.obterSugestoes(ingredientes, true, semLactose, semGluten);
    }
}
