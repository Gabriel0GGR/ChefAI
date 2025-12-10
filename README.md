<h1>ChefAI</h1>
O projeto se trata de uma aplicação que indica receitas a partir de igredientes selecionados.
</br>
<h3>Dependências:</h3>
<ul>
<li><a href="https://repo1.maven.org/maven2/com/google/code/gson/gson/2.3.1/gson-2.3.1.jar">com.google.code.gson</a></li>
</ul>
</br>
Primeiramente para o projeto funcionar corretamente é necessário baixar as dependências e carrega-las no BlueJ.
<ul>
<li>1. Baixe o .jar da dependência.</li>
<li>2. No BlueJ vá na aba <b>Tools</b> e clique em <b>Preferences</b>.</li>
<li>3. Depois disso vá na <b>Libraries</b> e clique em <b>Add File</b>.</li>
<li>4. Selecione o .jar que foi baixado, feche e reabra o BlueJ.</li>
</ul>
Depois disso é necessário adicionar a sua chave do <a href="https://openrouter.ai/settings/keys"><b>OpenRouter</b></a> para o projeto funcionar.
<ul>
<li>1. Crie uma conta no <b>OpenRouter</b> e gere uma chave.</li>
<li>2. Adicione a chave ao arquivo <b>config.txt</b> que é gerado depois que você tenta rodar o programa pela primeira vez.</li>
<li>3. Selecione o modelo de AI caso queira ou mantenha o padrão (para isso basta editar a classe <b>LLMClient</b> em request.addProperty("model", "<b><i>*AQUI*</b></i>")).</li>
</ul>
