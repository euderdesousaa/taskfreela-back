# TaskFreela - Backend

## Introdução
TaskFreela é uma plataforma de gerenciamento de tarefas e produtividade projetada exclusivamente para freelancers. Nosso backend é desenvolvido para suportar funcionalidades essenciais de gestão de projetos, produtividade, e integração com módulos específicos voltados para freelancers.

## Funcionalidades Principais

- **Foco Exclusivo em Freelancers**: Arquitetura desenhada para atender às necessidades de quem trabalha solo ou com pequenos clientes, com operações simplificadas e práticas.
- **Gerenciamento de Clientes**: Organização de tarefas e projetos por cliente, com acompanhamento de progresso centralizado.
- **Relatórios de Produtividade Personalizados**: Criação de relatórios automáticos que mostram a distribuição de tempo e esforço entre projetos. *(Funcionalidade Premium)*
- **Área Financeira Integrada**: Sistema de faturamento e rastreamento de pagamentos para centralizar informações financeiras na plataforma.
  
## Como Funciona

1. **Criação e Organização de Tarefas**: Endpoint para adicionar tarefas, organizar em projetos e definir prazos de maneira eficiente.
2. **Acompanhamento do Progresso**: Controle de status de projeto por meio de status como "Em andamento", "Concluído", "Atrasado".
3. **Gestão de Prazos e Notificações**: Sistema de calendário e notificações automáticas para não perder prazos importantes.
4. **Relatórios e Cobrança**: Geração de relatórios detalhados e emissão de faturas diretamente pelo backend para facilitar a gestão de tempo e finanças. *(Funcionalidade Premium)*

## Modelo de Monetização

- **Plano Gratuito**: Funcionalidades básicas de criação de tarefas e projetos.
- **Plano Pago**: Inclui funcionalidades avançadas, como relatórios de produtividade, sistema de cobrança, criação de contratos e rastreamento de tempo.

## Funcionalidades Premium

### 1. Rastreio de Tempo
Monitoramento do tempo dedicado a cada tarefa e projeto, com opções para iniciar/parar o rastreamento, geração de relatórios detalhados, e exportação de dados para CSV ou PDF.

### 2. Criação de Contratos com IA
Utilização de inteligência artificial para gerar contratos personalizados com templates predefinidos, preenchimento inteligente, e suporte para assinaturas digitais e armazenamento seguro.

### 3. "To-Do" - Gerenciamento de Tarefas Diárias
Endpoint para gerenciamento de tarefas diárias utilizando checkboxes, incluindo opções para rotinas diárias e tarefas específicas do dia, com lembretes automáticos e histórico de produtividade.

---

## Tecnologias Utilizadas

Este projeto foi desenvolvido utilizando as seguintes tecnologias e bibliotecas:

### Linguagem de Programação
- **Java 21**: Base para o desenvolvimento de todos os serviços do projeto.

### Frameworks e Bibliotecas Principais
- **Spring Boot**: Framework para desenvolvimento rápido e simplificado de aplicações Java.
- **Spring Cloud**: Utilizado para construção de uma arquitetura de microsserviços.
- **Spring Security**: Implementação de autenticação e autorização.
- **Spring Data**: Fornece abstração de persistência de dados, incluindo suporte a JPA e MongoDB.
- **Spring Cloud Gateway**: Para gerenciamento e roteamento de tráfego entre microsserviços.
- **Springdoc OpenAPI**: Ferramenta para documentação automática de APIs REST.

### Bancos de Dados
- **PostgreSQL**: Utilizado para persistência de dados relacionais.
- **MongoDB**: Usado para armazenamento de dados no formato de documentos JSON.
- **Redis**: Armazenamento em cache e gerenciamento de sessões.

### Segurança e Autenticação
- **JWT (Java JWT, JJWT)**: Para autenticação baseada em tokens JSON Web Token.
- **OAuth2**: Implementação para autenticação segura, com servidores de autorização e recursos.
- **Bouncy Castle**: Biblioteca para funcionalidades avançadas de criptografia.

### Comunicação entre Serviços e APIs
- **Spring Cloud OpenFeign**: Cliente HTTP declarativo para comunicação entre microsserviços.
- **Eureka (Netflix Eureka)**: Serviço de descoberta, permitindo que os serviços se registrem e encontrem uns aos outros dinamicamente.

### Mensageria e Eventos
- **Kafka**: Para processamento assíncrono de eventos entre microsserviços (inferido pelo projeto).

### Utilitários e Ferramentas de Desenvolvimento
- **MapStruct**: Biblioteca para mapeamento automático de objetos (DTOs).
- **Lombok**: Para redução de código boilerplate, como getters e setters.
- **Thymeleaf**: Template engine utilizada na construção de emails em HTML.

### Ferramentas de Teste
- **JUnit**: Framework para desenvolvimento de testes unitários.
- **Spring Security Test**: Para testes específicos de segurança.
- **Reactor Test**: Para testes em fluxos reativos, especialmente útil com MongoDB reativo.

## Contribuição

Estamos em busca de desenvolvedores que acreditam em transformar a vida dos freelancers com soluções práticas e centradas no usuário. Contribua para o TaskFreela e faça parte dessa transformação.


