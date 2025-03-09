# Projeto web services com spring boot e jpa/hibernate
 São anotações e observações que escreveri aqui dos passos que segui na criação do projeto do curso "Curso mais didático e completo de Java e POO, UML, JDBC, JavaFX, Spring Boot, JPA, Hibernate, MySQL, MongoDB e muito mais" do Professor Nelio Alves na Udemy

- UML: 
![Captura de tela 2025-03-09 192102](https://github.com/user-attachments/assets/0d04f9d6-dbe1-41ce-ba22-1deb8f492ea8)

- Instancias:
![Captura de tela 2025-03-09 192114](https://github.com/user-attachments/assets/c9ebc2fd-f734-4a7e-aaab-081967ba1d2f)

# OBJETIVOS

- Criar projeto Spring Boot Java
- Implementar modelo de domínio *(Modelo com as entidades do negócio, com vários relacionamentos)*
- Estruturar camadas lógicas: resource, service, repository  
  *(O backend é dividido em camadas:*
  - **Resource (Controladores REST):** são a interface da aplicação com o backend, recebem requisições e respondem de acordo com o comportamento do sistema.
  - **Todas as camadas se comunicam com a camada de entidades.**
- Configurar banco de dados de teste (H2) *(Banco de dados em memória do Java)*
- Povoar o banco de dados
- CRUD - *Create, Retrieve, Update, Delete* (operações de um cadastro completo de uma entidade)
- Tratamento de exceções  

---

# TECNOLOGIAS A SEREM UTILIZADAS

- **Spring Boot**
- **Apache Tomcat** *(Contêiner web para executar a aplicação)*
- **Maven** *(Gerenciador de dependências)*
- **H2** *(Banco de dados em memória para testes)*
- **Postman**
- **PostgreSQL**
- **Heroku** *(Plataforma em nuvem como serviço)*

> **OBS:** O *Spring Boot* está na versão **3**, e a aula está na versão **2**. Prestar atenção nisso.

---

# PROJECT CREATED

### Checklist:

- **Spring Initializr** *(Programa web para criar projetos Spring Boot, disponível no Google)*
  - Maven
  - Java 17
  - Packing JAR
  - Dependencies: Spring Web

---

# USER ENTITY AND RESOURCES 

*(Vai ser o recurso web correspondente à entidade User. Disponibilizará 2 endpoints para recuperarmos os usuários cadastrados e usuários pelo ID.)*

### **Basic entity checklist:**

- Basic attributes
- Associations *(instantiate collections)*
- Constructors
- Getters & Setters *(Collections: only get)*
- `hashCode` & `equals`
- **Serializable** *(Permite que o objeto seja transformado em cadeia de bytes, para que ele trafegue na rede ou seja gravado em arquivos.)*

---

# H2 DATABASE, TEST PROFILE, JPA

*(Configurar o banco de dados de testes (H2), um banco de dados em memória que já vem integrado ao projeto.)*

### **Checklist:**

- **JPA & H2 dependencies** *(Podem ser adicionadas no Spring Initializr.)*
- **application.properties** *(Local: `src/main/resources`)*
  - Criar um perfil de teste adicionando:
    ```properties
    spring.profiles.active=test
    spring.jpa.open-in-view=true
    ```
- **application-test.properties** *(Criar em `src/main/resources/application-test.properties` para as configurações do H2.)*
- **Entity: JPA mapping** *(Usar anotações do JPA para instruir como os objetos serão convertidos para o modelo relacional.)*

> **OBS:** No Spring Boot **3**, a anotação `@Entity` vem do pacote **jakarta**. Além disso, é necessário adicionar `@Table(name = "tb_user")` na classe User para evitar conflito com palavras reservadas no H2.

---

# JPA REPOSITORY, INJEÇÃO DE DEPENDÊNCIAS, DATABASE SEEDING

*(Implementação do primeiro repositório, utilizando o JPA Repository do Spring Data JPA.)*

### **Checklist:**

- **Repository Layer:**
  ```java
  public interface UserRepository extends JpaRepository<User, Long> {}
  ```
  *(A classe será uma interface, pois o `JpaRepository` já possui uma implementação padrão.)*
- **Configuração para o perfil de teste** *(Criar uma classe auxiliar `TestConfig` no pacote `config`, com a anotação `@Configuration` e `@Profile("test")`.)*
- **Injeção de dependências**
  ```java
  @Autowired
  private UserRepository userRepository;
  ```
- **Persistência inicial dos dados (seeding)**

---

# SERVICE LAYER, COMPONENT REGISTRATION

*(Implementar a camada de serviços, onde são aplicadas as regras de negócio e a orquestração dos repositórios.)*

### **Checklist:**

- **Criar classe `UserService` no pacote `services`**
- **Registrar componentes do Spring**
  ```java
  @Service
  public class UserService {}
  ```
- **Criar operações de busca de usuários (todos e por ID)**
- **Injeção de dependências no `UserResource`**

> **OBS:** Classes do tipo *Service* devem ser registradas com `@Service`. Já *repositories* não precisam, pois `JpaRepository` já é registrado automaticamente.

---

# Order, Instant, ISO 8601

*(O ISO 8601 define várias possibilidades de formatos para a data, sendo a mais comum `yyyy-MM-ddTHH:mm:ssZ`.)*

### **Basic new entity checklist:**

- **Entity:** *(Definir relacionamentos entre tabelas com anotações JPA.)*
  ```java
  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;
  ```
- **Lazy Loading e `@JsonIgnore`**
  ```java
  @OneToMany(mappedBy = "client")
  @JsonIgnore
  private List<Order> orders;
  ```

> **OBS:** O *Lazy Loading* evita que o JPA carregue automaticamente objetos relacionados. Para permitir que o Jackson busque os dados ao final do ciclo de vida, adicionar `spring.jpa.open-in-view=true` no `application.properties`.

---

# OrderStatus ENUM

*(Melhoria na implementação do Enum para evitar problemas ao adicionar novos valores.)*

### **Passos:**

1. Criar o `OrderStatus` enum.
2. Atribuir valores numéricos aos tipos enum:
   ```java
   PAID(2);
   ```
3. Criar a variável `code` do tipo `int`.
4. Criar os métodos:
   ```java
   private OrderStatus(int code) { this.code = code; }
   public int getCode() { return code; }
   public static OrderStatus valueOf(int code) {}
   ```
5. Criar uma variável `Integer` na classe `Order` para armazenar o status.
6. Alterar `get` e `set` para retornar um `OrderStatus`, ajustando a conversão.

---

## Formatação Markdown

- `_()`_ para observações
- `-` para listas
- `** **` para **negrito**
- `#` para títulos

# Projeto Spring Boot com JPA e PostgreSQL

---

## 1. Criação das Entidades Category e Product

### Associação de Muitos para Muitos (com JoinTable)

Quando temos a associação de muitos para muitos em POO, podemos simplesmente dizer que um produto possui várias categorias e uma categoria possui vários produtos. No banco de dados, isso não é possível diretamente, sendo necessário criar uma tabela de associação com as chaves estrangeiras de ambas as entidades.

#### Passos:
1. Utilizar **@ManyToMany** e **@JoinTable**:
   ```java
   @ManyToMany
   @JoinTable(name = "tb_product_category", 
       joinColumns = @JoinColumn(name = "product_id"), 
       inverseJoinColumns = @JoinColumn(name = "category_id"))
   ```
2. Na classe `Category`, mapear com **@ManyToMany(mappedBy = "categories")** e adicionar **@JsonIgnore** para evitar loop infinito.
3. Indicar as associações na classe de configuração antes de salvar novamente a tabela `Product`.
4. **Importante**: Salvar novamente a tabela após indicar as associações.

---

## 2. Entidade OrderItem com Associação de Muitos para Muitos com Atributos Extras

### Classe Auxiliar OrderItemPK
- Criar a classe `OrderItemPK` no pacote `entities.pk`, que representará o par `Product` e `Order`, possuindo as chaves estrangeiras.
- A classe **não** terá construtor explícito.
- Implementar `Serializable`, `getters`, `setters`, `hashCode` e `equals`.
- Adicionar anotações:
  ```java
  @Embeddable
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
  
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
  ```

### Classe OrderItem
- Criar `OrderItemPK id = new OrderItemPK();` como identificador (chave composta).
- Adicionar anotações:
  ```java
  @EmbeddedId
  private OrderItemPK id;

  @JsonIgnore
  public Order getOrder() {
      return id.getOrder();
  }
  ```
- Criar `@OneToMany(mappedBy = "id.order")` na classe `Order` para mapear a associação.

### Seed (População Inicial)
```java
OrderItem oi1 = new OrderItem(o1, p1, 2, p1.getPrice());
```

---

## 3. Associação Muitos para Muitos entre Product e OrderItem

- Criar associação semelhante à de `Order`.
- Iterar sobre `OrderItem` associado ao `Product` e associar ao `Order` correspondente.

---

## 4. Entidade Payment OneToOne

### Passos:
1. Criar a classe `Payment` e mapear `@OneToOne` com `@MapsId`.
2. Na classe `Order`, adicionar `@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)`.
3. No arquivo de configuração de teste, instanciar `Payment` e associá-lo ao `Order` correspondente.

---

## 5. Métodos de Subtotal e Total

- Criar método `getSubTotal()` em `OrderItem`.
- Criar método `getTotal()` em `Order`.
- **Atenção**: O Jackson pega todos os `getters` por padrão.

---

## 6. User Insert

### Passos:
1. Criar método `insert` em `UserService`.
2. Criar método `insert` em `UserResource` com:
   ```java
   @PostMapping
   @RequestBody
   ```

---

## 7. User Delete

### Passos:
1. Criar método `delete` em `UserService` utilizando `deleteById` do `JpaRepository`.
2. Criar método `delete` em `UserResource` com:
   ```java
   @DeleteMapping(value = "/{id}")
   @PathVariable Long id
   ```

---

## 8. User Update

### Passos:
1. Criar método `update` e `updateData` em `UserService`.
2. Criar método `update` em `UserResource` com:
   ```java
   @PutMapping
   @PathVariable Long id
   @RequestBody User obj
   ```

---

## 9. Exception Handling - findById

### Passos:
1. Criar `ResourceNotFoundException` (`services.exceptions`).
2. Criar `StandardError` (`resources.exceptions`).
3. Criar `ResourceExceptionHandler` com:
   ```java
   @ControllerAdvice
   @ExceptionHandler(ResourceNotFoundException.class)
   ```
4. Alterar `findById` em `UserService` para:
   ```java
   return op.orElseThrow(() -> new ResourceNotFoundException(id));
   ```

---

## 10. Exception Handling - delete

### Passos:
1. Criar `DatabaseException` (`services.exceptions`).
2. Adicionar tratamento de `EmptyResultDataAccessException` e `DataIntegrityViolationException`.

---

## 11. Exception Handling - update

### Passos:
1. Capturar `EntityNotFoundException` no `UserService` e lançar `ResourceNotFoundException`.

---

## 12. Deploy no Heroku 
> **OBS:** A partir daqui será apenas na teória.
> 
### Checklist:
1. Criar Heroku App e provisionar PostgreSQL.
2. Criar banco localmente com:
   ```sql
   CREATE DATABASE springboot_course ENCODING 'UTF8';
   ```
3. Configurar PostgreSQL no projeto.
4. Criar `application-dev.properties`.
5. Configurar Heroku CLI e realizar o deploy.

---

## 13. Configuração do PostgreSQL Local

### Checklist:
- Download: [PostgreSQL](https://www.postgresql.org/download/)
- Configuração:
  - Superuser: `postgres`
  - Senha: `1234567`
  - Porta: `5432`
- Criar database: `springboot_course`

---

## 14. Deploy no Heroku - Configuração Final

### Passos:
1. Criar `application-prod.properties` com:
   ```properties
   spring.datasource.url=${DATABASE_URL}
   spring.jpa.hibernate.ddl-auto=none
   spring.jpa.show-sql=false
   spring.jpa.properties.hibernate.format_sql=false
   ```
2. Criar `system.properties`:
   ```
   java.runtime.version=17
   ```
3. Enviar para o Heroku:
   ```bash
   git add .
   git commit -m "Deploy app to Heroku"
   git push heroku main
