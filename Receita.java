import java.util.List;

public class Receita {
    private String nome;
    private List<Ingrediente> ingredientesNecessarios;
    private String modoPreparo;
    private int tempoPreparo; // em minutos

    public Receita(String nome, List<Ingrediente> ingredientesNecessarios, 
                   String modoPreparo, int tempoPreparo) {
        this.nome = nome;
        this.ingredientesNecessarios = ingredientesNecessarios;
        this.modoPreparo = modoPreparo;
        this.tempoPreparo = tempoPreparo;
    }

    public String getNome() {
        return nome;
    }
    
    public List<Ingrediente> getIngredientesNecessarios() {
        return ingredientesNecessarios;
    }
    
    public String getModoPreparo() {
        return modoPreparo;
    }
    
    public int getTempoPreparo() {
        return tempoPreparo;
    }
}
