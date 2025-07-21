
# Formulário de Cadastro com JSF

Esta é uma aplicação web, desenvolvida como parte de uma prova técnica, para demonstrar habilidades no desenvolvimento de software com o ecossistema Java JSF. O objetivo principal da aplicação é gerenciar um cadastro de pessoas, permitindo a criação e visualização de registros em um formulário interativo.


## Funcionalidades

- Cadastro de Pessoas: Formulário de inscrição de dados de uma nova pessoa (nome, sobrenome, sexo, etc.).

- Listagem de Dados: Exibição de todas as pessoas cadastradas em uma tabela na mesma página.

- Atualização em Tempo Real: A tabela de pessoas é atualizada automaticamente via AJAX após um novo cadastro, sem a necessidade de recarregar a página.

- Validações de Formulário:

    - Verificação de campos obrigatórios.

    - Validação para impedir o cadastro de uma data de nascimento futura.

    - Verificação no backend para impedir o cadastro de pessoas com o mesmo nome e sobrenome.

- Integração com API Externa: Preenchimento automático do campo de endereço através de uma chamada à BrasilAPI, baseada no CEP digitado pelo usuário.

- Cálculo de Campo Derivado: A idade da pessoa é calculada dinamicamente com base na data de nascimento e exibida na tabela.




## Stack utilizada

Este projeto foi construído utilizando as seguintes tecnologias e frameworks:

Linguagem: Java 8

Plataforma: Java EE 7

Framework Web: JavaServer Faces (JSF) 2.2

Biblioteca de Componentes: PrimeFaces 8.0

Persistência de Dados: JPA 2.1 (com EclipseLink como implementação)

Banco de Dados: H2 Database (configurado para rodar em memória)

Injeção de Dependência: CDI 1.2 (Contexts and Dependency Injection)

Servidor de Aplicação: GlassFish 4.1

Gerenciamento de Dependências: Apache Maven

Testes Unitários: JUnit 5 e Mockito

APIs Externas: BrasilAPI para consulta de CEP.


## Rodando localmente

Clone o projeto

```bash
  git clone https://github.com/WesleyBueno/teste_tradepro.git
```

**Importar no Eclipse**

- Abra o Eclipse.

- Vá em File -> Import....

- Selecione Maven -> Existing Maven Projects e clique em Next.

- Navegue até a pasta onde você clonou o projeto e selecione-o.

- Clique em Finish. O Eclipse irá baixar as dependências do Maven.

- Execute o comando para acessar as pastas atualizadas da aplicação.
```bash
  git checkout wesley
```

**Configurar o Servidor GlassFish no Eclipse**

- Na aba "Servers", adicione um novo servidor do tipo "GlassFish 4".

- Aponte para o diretório de instalação do seu GlassFish.

- Na configuração do "Server Runtime Environment", certifique-se de que o JRE selecionado seja o JDK 8.

- Se o adaptador do GlassFish não estiver disponível, instale o "GlassFish Tools" via Help -> Install New Software... usando o repositório da Oracle para a sua versão do Eclipse: http://download.oracle.com/otn_software/oepe/12.2.1.8/oxygen/repository/dependencies/

**Execução**

**Adicionar o Projeto ao Servidor**

- Na aba "Servers", clique com o botão direito no servidor GlassFish e selecione Add and Remove....

- Mova o projeto da esquerda para a direita para fazer o deploy.

- Iniciar o Servidor

- Clique com o botão direito no servidor GlassFish e clique em Start.

- Observe a aba "Console" para acompanhar os logs de inicialização.

- Acessar a Aplicação

- Após o servidor iniciar, abra seu navegador e acesse a URL: http://localhost:8080/provajava/cadastro.xhtml 

(O nome /provajava/ pode variar de acordo com o artifactId definido no seu pom.xml).

## Referência

 - [Glassfish](https://glassfish.org/documentation.html)
 - [PrimeFaces](https://primefaces.github.io/primefaces/15_0_0/#/?id=main)
 - [Java JSF](https://www.devmedia.com.br/guia/jsf-javaserver-faces/38322)

