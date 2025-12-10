import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;

public class LLMClient {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = ConfigManager.getApiKey();

    public List<Receita> obterSugestoes(List<Ingrediente> ingredientes,
                                       boolean vegetariano, boolean semLactose, boolean semGluten) {
        try {
            String prompt = montarPrompt(ingredientes, vegetariano, semLactose, semGluten);

            JsonArray messages = new JsonArray();
            JsonObject userMsg = new JsonObject();
            userMsg.addProperty("role", "user");
            userMsg.addProperty("content", prompt);
            messages.add(userMsg);

            JsonObject request = new JsonObject();
            request.addProperty("model", "amazon/nova-2-lite-v1:free");  // Modelo gratuito OpenRouter
            request.add("messages", messages);
            request.addProperty("temperature", 0.7);
            request.addProperty("max_tokens", 2000);

            String resposta = fazerRequisicaoAPI(request.toString());
            return parsearRespostaReal(resposta);

        } catch (Exception e) {
            System.err.println("Erro OpenRouter: " + e.getMessage());
            if (e.getMessage().contains("429") || e.getMessage().contains("rate")) {
                System.out.println("Limite de rate atingido (50 reqs/dia grátis). Compre US$10 para 1000 reqs/dia.");
            }
            return new ArrayList<>();  // Sempre lista vazia, nunca null
        }
    }

    private String montarPrompt(List<Ingrediente> ingredientes, boolean veg, boolean sl, boolean sg) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sugira exatamente 3 receitas rápidas (máx 30 min) usando só estes ingredientes:\n");
        for (Ingrediente i : ingredientes) {
            sb.append("• ").append(i.getNome()).append(" (").append((int)i.getQuantidade()).append("g)\n");
        }
        if (veg) sb.append("→ Deve ser vegetariana\n");
        if (sl) sb.append("→ Sem lactose\n");
        if (sg) sb.append("→ Sem glúten\n");
        if (!veg && !sl && !sg) sb.append("→ Sem restrições\n");

        sb.append("\nResponda APENAS com um array JSON válido, sem markdown ou texto extra:\n");
        sb.append("[{\"nome\":\"Nome da receita\",\"tempoPreparo\":15,\"ingredientes\":[{\"nome\":\"ovo\",\"quantidade\":100.0}],\"modoPreparo\":\"1. Bata os ovos\\n2. Frite\"}]");
        return sb.toString();
    }

    private String fazerRequisicaoAPI(String body) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        // Headers opcionais para OpenRouter (para rankings, mas úteis)
        conn.setRequestProperty("HTTP-Referer", "https://chefai.local");  // Seu site/app
        conn.setRequestProperty("X-Title", "ChefAI App");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        int code = conn.getResponseCode();
        InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String resposta = lerStream(stream);

        if (code != 200) {
            throw new IOException("HTTP " + code + " → " + resposta);
        }
        return resposta;
    }

    private String lerStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private List<Receita> parsearRespostaReal(String jsonResponse) {
    try {
        JsonObject obj = JsonParser.parseString(jsonResponse).getAsJsonObject();
        String content = obj.getAsJsonArray("choices")
                           .get(0).getAsJsonObject()
                           .get("message").getAsJsonObject()
                           .get("content").getAsString();

        System.out.println("\n=== Resposta bruta do modelo ===");
        System.out.println(content);
        System.out.println("=====================================\n");

        // REMOVE TUDO QUE VIER DEPOIS DO ÚLTIMO } SEGUIDO DE ]
        // e força fechar o JSON se estiver aberto
        String fixed = content
            .replaceAll("```json|```", "")
            .trim();

        // Se não tem ], tenta fechar manualmente
        if (!fixed.contains("]")) {
            // Conta chaves abertas
            int openBrackets = fixed.length() - fixed.replace("[", "").length();
            int closeBrackets = fixed.length() - fixed.replace("]", "").length();
            int openBraces = fixed.length() - fixed.replace("{", "").length();
            int closeBraces = fixed.length() - fixed.replace("}", "").length();

            StringBuilder sb = new StringBuilder(fixed);
            while (closeBraces < openBraces) { sb.append("}"); closeBraces++; }
            while (closeBrackets < openBrackets) { sb.append("]"); closeBrackets++; }
            fixed = sb.toString();
        }

        // Extrai apenas do primeiro [ até o último ]
        int inicio = fixed.indexOf('[');
        int fim = fixed.lastIndexOf(']') + 1;
        if (inicio == -1 || fim <= inicio) {
            throw new Exception("JSON inválido mesmo após correção");
        }

        String jsonFinal = fixed.substring(inicio, fim);
        System.out.println("=== JSON corrigido e usado ===");
        System.out.println(jsonFinal);
        System.out.println("============================\n");

        JsonArray array = JsonParser.parseString(jsonFinal).getAsJsonArray();
        List<Receita> receitas = new ArrayList<>();

        for (JsonElement el : array) {
            JsonObject r = el.getAsJsonObject();
            String nome = r.has("nome") ? r.get("nome").getAsString() : "Receita";
            int tempo = r.has("tempoPreparo") ? r.get("tempoPreparo").getAsInt() : 15;
            String modo = r.has("modoPreparo") ? r.get("modoPreparo").getAsString().replace("\\n", "\n").replaceAll("(?m)^\\d+\\.\\s*", "") : "Sem instruções";

            List<Ingrediente> ings = new ArrayList<>();
            if (r.has("ingredientes") && r.get("ingredientes").isJsonArray()) {
                JsonArray ingArray = r.getAsJsonArray("ingredientes");
                for (JsonElement ie : ingArray) {
                    JsonObject ing = ie.getAsJsonObject();
                    String n = ing.has("nome") ? ing.get("nome").getAsString() : "ingrediente";
                    double q = ing.has("quantidade") ? ing.get("quantidade").getAsDouble() : 100.0;
                    ings.add(new Ingrediente(n, q));
                }
            }
            receitas.add(new Receita(nome, ings, modo, tempo));
        }

        System.out.println("SUCESSO! " + receitas.size() + " receitas carregadas da API!");
        return receitas;

    } catch (Exception e) {
        System.out.println("Erro final: " + e.getMessage());
        return criarReceitasSimuladas(new ArrayList<>());
    }
}

    private List<Receita> criarReceitasSimuladas(List<Ingrediente> ingredientes) {
        return List.of(
            new Receita("Omelete Rápido", List.of(new Ingrediente("ovo", 2)), "Bata e frite em 5 min", 10),
            new Receita("Salada Simples", List.of(new Ingrediente("tomate", 100)), "Corte e tempere", 5),
            new Receita("Torrada com Manteiga", List.of(new Ingrediente("pão", 2)), "Torre e unte", 3)
        );
    }
}