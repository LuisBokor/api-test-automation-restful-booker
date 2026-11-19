# Guia de screenshots

As imagens ainda não estão incluídas no repositório. Para adicionar evidências, salve-as nesta
pasta com os nomes abaixo e inclua as referências no README principal.

```
docs/
├── github-actions-success.png
├── allure-overview.png
├── allure-suites.png
├── allure-history.png
└── postman-collection.png
```

| Arquivo | Onde obter | Área a capturar |
|---|---|---|
| `github-actions-success.png` | Aba **Actions** do repositório, workflow **API Tests**, execução verde mais recente | Cabeçalho da execução (commit, status, duração) e o job com as etapas concluídas, incluindo a execução do Maven e o artefato `allure-results` |
| `allure-overview.png` | `mvn clean verify`, depois `allure serve target/allure-results`, aba **Overview** | Gráfico de status, total de testes (29), taxa de sucesso e duração |
| `allure-suites.png` | Mesmo relatório, aba **Suites** | Árvore com as classes de teste expandida e um teste aberto mostrando steps e anexos de request/response |
| `allure-history.png` | Relatório Allure com histórico (ver observação), aba **Overview**, widget **History Trend** | Widget de tendência com duas ou mais execuções |
| `postman-collection.png` | Postman, após importar `postman/RestfulBooker.postman_collection.json` e o environment | Painel esquerdo com as 9 pastas e os resultados do Collection Runner (28 requests, 0 falhas) |

## Observações

- Formato PNG, cerca de 1600 px de largura, sem dados pessoais ou tokens visíveis.
- **Histórico do Allure:** o workflow publica apenas `allure-results` como artefato e não publica relatório em GitHub Pages. Para gerar o histórico, copie a pasta `history` de um relatório anterior para `target/allure-results` antes de gerar o próximo (`allure generate target/allure-results -o target/allure-report`), repetindo em pelo menos duas execuções.
