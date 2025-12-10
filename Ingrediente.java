public class Ingrediente {
    private String nome;
    private double quantidade;

    public Ingrediente(String nome, double quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public double getQuantidade() {
        return quantidade;
    }
    
    @Override
    public String toString() {
        return nome + ": " + quantidade + "g";
    }
}
