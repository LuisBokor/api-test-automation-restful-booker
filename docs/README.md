# Checklist de screenshots do portfólio

Este guia define como capturar as evidências visuais do projeto. No momento, nenhum dos seis arquivos abaixo existe; capture as telas reais e salve os PNGs nesta pasta. Não crie imagens ilustrativas para substituir evidências.

## Preparação comum

- Use PNG e a resolução indicada em cada item; mantenha a escala do sistema em 100% e ajuste o zoom do navegador/terminal para que o texto fique legível.
- Feche abas e janelas que não façam parte da evidência. Oculte notificações, dados de conta, tokens, cookies, variáveis secretas e qualquer dado de ambiente privado.
- Antes da captura, confirme que os resultados correspondem à execução atual e que os valores exibidos não contradizem o README. A execução verde anterior não inclui o deploy; capture uma nova execução após publicar o workflow atualizado.
- Não adicione uma imagem ao README principal enquanto o arquivo correspondente não existir no repositório.

## 1. GitHub Actions com sucesso

- [ ] **Onde capturar:** aba Actions deste repositório, no workflow **API Tests** e na execução verde mais recente da branch principal.
- **Passo a passo:**
  1. Abra `https://github.com/LuisBokor/api-test-automation-restful-booker/actions/workflows/ci.yml`.
  2. Selecione a execução mais recente com conclusão **success** em `master`, após publicar o workflow atualizado.
  3. Abra o job `test` e confirme que `Run Tests` e `Upload Allure Results` terminaram sem erro.
  4. Confirme também o job `publish-allure` e a etapa `Deploy GitHub Pages`.
  5. Ajuste o zoom/rolagem para mostrar o título e estado da execução e os jobs concluídos; capture a tela.
- **Área recomendada:** cabeçalho com status, branch e commit, além da lista de jobs/etapas `test` e `publish-allure`.
- **Resolução recomendada:** 1600 × 1000 px; se não couber, capture o cabeçalho e a lista de jobs em uma composição sem cortar os status.
- **Remover ou ocultar:** menus laterais desnecessários, informações de perfil, notificações, erros de execuções antigas e qualquer URL ou valor de variável confidencial.
- **Nome final:** `docs/github-actions-success.png`
- **Alt text:** `Workflow API Tests concluído com sucesso, mostrando os testes Maven e a publicação do relatório Allure no GitHub Pages.`

## 2. Allure — visão geral

- [ ] **Onde capturar:** relatório público no GitHub Pages, na aba **Overview**.
- **Passo a passo:**
  1. Abra `https://luisbokor.github.io/api-test-automation-restful-booker/` depois de uma publicação bem-sucedida.
  2. Selecione **Overview** no menu do Allure.
  3. Aguarde os gráficos e cartões carregarem; capture o cabeçalho, o resumo de status e as métricas visíveis.
- **Área recomendada:** barra de navegação do Allure e cartões/gráficos de resultados, sem cortar rótulos nem valores.
- **Resolução recomendada:** 1600 × 900 px.
- **Remover ou ocultar:** abas pessoais do navegador, URLs de outros ambientes e qualquer informação confidencial nos anexos.
- **Nome final:** `docs/allure-overview.png`
- **Alt text:** `Visão geral do Allure Report com o resumo dos resultados dos testes automatizados da API.`

## 3. Allure — suítes e detalhes

- [ ] **Onde capturar:** o mesmo relatório publicado, na aba **Suites**.
- **Passo a passo:**
  1. Abra o relatório e selecione **Suites**.
  2. Expanda a árvore até mostrar as classes de teste.
  3. Abra um teste representativo que terminou com sucesso.
  4. Expanda os steps para mostrar a chamada e as asserções; capture a árvore e os detalhes legíveis.
- **Área recomendada:** árvore de suítes à esquerda e painel do teste selecionado com steps expandidos.
- **Resolução recomendada:** 1600 × 900 px.
- **Remover ou ocultar:** tokens, cookies, cabeçalhos de autorização e valores sensíveis em anexos de request/response. Prefira um teste que não exponha esses valores.
- **Nome final:** `docs/allure-suites.png`
- **Alt text:** `Suítes do Allure com classes de teste, steps e detalhes de uma execução bem-sucedida.`

## 4. Allure — histórico

- [ ] **Onde capturar:** aba **Overview** do relatório público depois de pelo menos duas publicações bem-sucedidas.
- **Passo a passo:**
  1. Confirme que duas ou mais execuções foram publicadas na branch `gh-pages`.
  2. Abra o relatório atual e selecione **Overview**.
  3. Localize o widget **History Trend** e confirme que ele mostra mais de uma execução.
  4. Ajuste o zoom para deixar o gráfico e a legenda legíveis; capture a tela.
- **Área recomendada:** widget **History Trend**, incluindo legenda/eixos e o cabeçalho do relatório.
- **Resolução recomendada:** 1600 × 900 px.
- **Remover ou ocultar:** gráficos cortados, histórico vazio ou dados que identifiquem ambientes privados.
- **Nome final:** `docs/allure-history.png`
- **Alt text:** `Tendência histórica do Allure mostrando resultados de múltiplas execuções do workflow.`

## 5. Collection do Postman

- [ ] **Onde capturar:** aplicativo Postman com a collection deste repositório importada.
- **Passo a passo:**
  1. Importe `postman/RestfulBooker.postman_collection.json` e `postman/RestfulBooker.postman_environment.json`.
  2. Selecione o environment importado sem abrir ou revelar valores de variáveis.
  3. Expanda a collection e organize o painel para mostrar suas pastas e requests.
  4. Selecione a collection para exibir o nome e os detalhes principais; capture a tela.
- **Área recomendada:** árvore da collection com as pastas expandidas e o painel central com a collection selecionada.
- **Resolução recomendada:** 1600 × 900 px.
- **Remover ou ocultar:** valores de `token`, `bookingId`, cookies, credenciais, abas pessoais e dados de outras collections.
- **Nome final:** `docs/postman-collection.png`
- **Alt text:** `Collection Restful Booker no Postman, organizada por cenários de API.`

## 6. Execução Newman

- [ ] **Onde capturar:** terminal na raiz do repositório, após uma execução bem-sucedida do Newman.
- **Passo a passo:**
  1. Abra um terminal na raiz do repositório, que contém `pom.xml` e `postman/`.
  2. Execute:
     ```bash
     npx newman run postman/RestfulBooker.postman_collection.json \
       -e postman/RestfulBooker.postman_environment.json
     ```
  3. Aguarde o resumo final e confirme 28 requests, 62 assertions e zero falhas.
  4. Ajuste o tamanho da fonte/área do terminal para mostrar o comando e o resumo; capture a tela.
- **Área recomendada:** comando executado e resumo final do Newman (requests, assertions e falhas).
- **Resolução recomendada:** 1600 × 900 px.
- **Remover ou ocultar:** variáveis de ambiente, tokens, cookies, credenciais, caminhos com dados pessoais e saídas de outros comandos.
- **Nome final:** `docs/newman-execution.png`
- **Alt text:** `Resumo de execução do Newman com 28 requests, 62 assertions e nenhuma falha.`

## Depois de capturar

- [ ] Verifique que cada imagem está no formato PNG e que o texto continua legível em tamanho de tela.
- [ ] Confirme que nenhum segredo ou dado privado ficou visível, inclusive em anexos e barras do navegador.
- [ ] Atualize a seção **Screenshots** do README principal para incluir apenas os arquivos que realmente existem em `docs/`; não mantenha imagens quebradas.
