import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private List<Ingrediente> ingredientesDisponiveis;
    private boolean vegetariano;
    private boolean semLactose;
    private boolean semGluten;

    public Usuario() {
        this.ingredientesDisponiveis = new ArrayList<>();
        this.vegetariano = false;
        this.semLactose = false;
        this.semGluten = false;
    }

    public void adicionarIngrediente(Ingrediente ingrediente) {
        ingredientesDisponiveis.add(ingrediente);
    }
    
    public List<Ingrediente> getIngredientesDisponiveis() {
        return ingredientesDisponiveis;
    }
    
    public boolean isVegetariano() {
        return vegetariano;
    }

    public void setVegetariano(boolean vegetariano) {
        this.vegetariano = vegetariano;
    }

    public boolean isSemLactose() {
        return semLactose;
    }

    public void setSemLactose(boolean semLactose) {
        this.semLactose = semLactose;
    }

    public boolean isSemGluten() {
        return semGluten;
    }

    public void setSemGluten(boolean semGluten) {
        this.semGluten = semGluten;
    }
}
