import java.util.List;

public abstract class RecipeSuggester {
    protected LLMClient client;
    
    public RecipeSuggester(LLMClient client) {
        this.client = client;
    }
    
    public abstract List<Receita> sugerir(List<Ingrediente> ingredientes, 
                                          boolean vegetariano, boolean semLactose, boolean semGluten);
}
