import java.io.*;
import java.util.HashMap;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.txt";
    private static HashMap<String, String> config = new HashMap<>();

    public static void carregarConfiguracao() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) {
                criarArquivoConfig();
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.contains("=") && !linha.trim().startsWith("#")) {
                    int index = linha.indexOf('=');
                    String chave = linha.substring(0, index).trim();
                    String valor = linha.substring(index + 1).trim();
                    config.put(chave, valor);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Erro ao carregar config.txt: " + e.getMessage());
        }
    }

    private static void criarArquivoConfig() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            writer.println("# Configurações do ChefAI");
            writer.println("# Cole sua chave OpenRouter aqui (pegue em openrouter.ai/keys)");
            writer.println("openrouter_api_key=sk-or-v1-sua-chave-aqui");
            System.out.println("Arquivo config.txt criado! Edite com sua chave OpenRouter.");
        } catch (IOException e) {
            System.err.println("Erro ao criar config.txt");
        }
    }

    public static String getApiKey() {
        carregarConfiguracao();
        String key = config.getOrDefault("openrouter_api_key", "");
        if (key.isEmpty() || key.contains("sk-or-v1-sua-chave-aqui")) {
            System.err.println("AVISO: Configure openrouter_api_key no config.txt[](https://openrouter.ai/keys)");
            return "";
        }
        return key;
    }
}