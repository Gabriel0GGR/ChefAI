# ChefAI - Sugestor Inteligente de Receitas

<div align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-blue" alt="Java 17+"/>
  <img src="https://img.shields.io/badge/BlueJ-Compatible-green" alt="BlueJ"/>
  <img src="https://img.shields.io/badge/OpenRouter-API-orange" alt="OpenRouter"/>
  <img src="https://img.shields.io/badge/Gson-Dependency-red" alt="Gson"/>
</div>

<br>

**ChefAI** é uma aplicação console que ajuda você a aproveitar ao máximo os ingredientes que tem em casa. Informe o que você possui na geladeira/despensa, suas restrições alimentares (vegetariano, sem lactose, sem glúten) e receba **3 receitas rápidas (máx. 30 minutos)** feitas sob medida com inteligência artificial.

---

### Funcionalidades
- Cadastro fácil de ingredientes com quantidades em gramas  
- Suporte a restrições: vegetariano, sem lactose, sem glúten  
- Sugestões realistas usando apenas (ou preferencialmente) seus ingredientes  
- Exibição clara: nome, tempo, ingredientes (com check do que você já tem), passo a passo  
- Integração com LLMs via **OpenRouter** (modelo gratuito incluso)

---

### Pré-requisitos
- Java 17+  
- [BlueJ](https://www.bluej.org/) (recomendado) ou qualquer IDE Java  
- Conexão com internet (para consultar a API)

### Dependência Externa
- **Gson** (Google JSON library)  
  Link direto: [gson-2.9.1.jar](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.9.1/gson-2.9.1.jar)

### Como configurar no BlueJ (passo a passo)

1. Baixe o arquivo `gson-2.9.1.jar`
2. No BlueJ:  
   `Tools → Preferences → Libraries → Add File`  
   Selecione o `.jar` baixado
3. Feche e reabra o BlueJ

### Chave da API OpenRouter (obrigatório)

1. Acesse [https://openrouter.ai/keys](https://openrouter.ai/keys)  
2. Crie uma conta gratuita e gere uma API Key  
3. Execute o projeto uma vez → será criado o arquivo `config.txt`  
4. Edite o arquivo e substitua:  
   ```txt
   openrouter_api_key=SUA_CHAVE_AQUI
5. Selecione outro modelo de AI caso queira (para isso basta editar a classe <b>LLMClient</b> em request.addProperty("model", "<b><i>*AQUI*</b></i>")) ou mantenha o padrão.

### Para executar

1. Abra o projeto no BlueJ
2. Clique com o botão direito na classe ChefAI
3. Selecione void main(String[] args)
4. Siga as instruções no console

![mermaid-drawing](https://github.com/user-attachments/assets/14d1a4c5-16bd-42c6-a5f0-5d07be54b07a)
