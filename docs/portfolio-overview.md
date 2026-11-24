# API Test Automation – Restful Booker

[![API Tests](https://github.com/LuisBokor/api-test-automation-restful-booker/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/LuisBokor/api-test-automation-restful-booker/actions/workflows/ci.yml?query=branch%3Amaster)
![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10.2-25A162?logo=junit5&logoColor=white)
![Rest Assured](https://img.shields.io/badge/Rest%20Assured-5.4.0-brightgreen)

Framework de automação de API em Java 17 com Rest Assured, JUnit 5, Allure, Postman/Newman e GitHub Actions. Exercita a API pública Restful Booker com cenários funcionais, negativos, de autenticação e autorização, além de validação de contratos JSON Schema.

## Destaques

- 29 execuções de testes JUnit, incluindo cenários parametrizados e testes locais de retry.
- 28 requests e 62 assertions na collection Postman executada com Newman.
- Maven com gates de qualidade Spotless, Checkstyle e SpotBugs.
- Dados de teste dinâmicos, cleanup por teste, timeouts e retry limitado a GET.
- Resultados Allure como artefato do CI; publicação no GitHub Pages preparada, ainda pendente de ativação e validação.

## Comece aqui

- [Documentação completa, arquitetura e execução](../README.md)
- [Checklist de evidências visuais](README.md)
- [Cenários manuais e rastreabilidade](../postman/Manual-Test-Scenarios.md)
- [Workflow de CI](../.github/workflows/ci.yml)

Com Java 17 e Maven instalados:

```bash
mvn -B -Pqa clean verify
```

Os testes usam um serviço público compartilhado e criam, alteram e excluem dados de demonstração. O relatório público pode conter detalhes de requisições e respostas; use apenas dados fictícios e credenciais públicas da demonstração.

## Publicação

O relatório Allure está pendente. A URL prevista é `https://luisbokor.github.io/api-test-automation-restful-booker/`. As seis screenshots ainda não foram capturadas; o checklist acima descreve as telas necessárias.

No GitHub, a branch padrão é `master`. Publique nela as alterações para apresentar o README completo na entrada do repositório.
