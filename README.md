# spring-jpa
 São anotações e observações que escreverei aqui dos passos que seguirei na criação do projeto

# OBJETIVOS
- Criar projeto Spring Boot Java 
- Implementar modelo de domínio _(Modelo com as entidades do negócios, com vários relacionamentos)_
- Estruturar camadas lógicas: resource, service, repositor _(o backend e dividido em camadas resourcer(controladores rest): são a interface da aplicação com o backend, vão receber as requisições e responder
de acordo com o compartimento do sistema //todas as camadas se comunicao com a camada de entidades)_
- Configurar banco de dados de teste (H2) _(banco de dados em memoria do java)_
- Povoar o banco de dados
- CRUD - Create, Retrieve, Update, Delete _(operações de um cadastro completo de uma entidade)_
- Tratamento de exceções 

## FERRAMENTAS A SEREM UTILIZADAS
- Spring boot
- Apache Tomcat _(contêiner web para executar a aplicação)_
- maven _(gerenciado de dependências)_
- h2 _(banco de dados em memoria para testes)_
- postman
- PostgreSQL
- heroku _(Plataforma em nuvem como serviço)_

**OBS: O SPRING BOOT TA NO 3 A AULA TA NO 2, PRESTAR ATENÇÃO NISSO**
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
## PROJECT CREATED
### Checklist:
- Spring Initializr _(PROGAMA WEB PARA CRIAR PROJETOS SPRING BOOT, COLOCA NO GOOGLE)_
- Maven
- Java 17
- Packing JAR
- Dependencies: Spring Web
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
## USER ENTITY AND RESOURCES 
_(vai ser o recursos web correspondente a entidade user, vai disponibilizar 2 endpoints para recuperarmos os usuários cadastrados e usuários pelo id)_
### Basic entity checklist:
- Basic attributes
- Associations (instantiate collections)
- Constructors
- Getters & Setters (collections: only get)
- hashCode & equals
- Serializable //permite que o objeto seja transformado em cadeia de bytes, para que ele trafegue na rede, seja gravado em arquivos
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
## H2 DATABASE, TEST PROFILE, JPA
_(Configurar o banco de dados de testes (h2) ele e um banco de dados em memória ele já vem integrado ao projeto e so rodar que ele vai ser criado. Profile de teste e um perfil
do projeto especifico para fazer testes, existe outros perfis que podem ser difinidos com por exemplo um perfil de desenvolvimento com outro banco de dados específicos para não precisar toda vez que projeto roda,
perfil de produção que e quando o projeto já esta implando no cliente que vai usar o sistema)_
### Checklist:
- JPA & H2 dependencies _(as dependências poderiam ter sidos adicionadas no Spring Initializr)_
- application.properties _((/src/main/resources)para criar um perfil de teste vai nesse file e add (spring.profiles.active=test //nome
                                                                                                      spring.jpa.open-in-view=true //))_
- application-test.properties _(Criar esse arquivo src/main/resources/application-test.properties //vai ter as configurações do banco de dados h2)_
- Entity: JPA mapping _(So tem a entidade 'user' que e uma entidade no modelo de dominio, colocar anotações do JPA para insttruir a ele como ele vai converter os objetos para o modelo relacional)_
**OBS: NO SPRING 3 O 'Entity' e do pacote jakarta e colocar o @Table(name = "tb_user") em cima da classe //isso vai especificar o nome da tabela no bd, e necessario renomear pq o nome User e uma palavra reservado
do banco de dados H2 necessario para n dar conflito, entçao mudar o nome da tabela**

#### Explicação sobre o arquivo application.properties
Define os tipos de perfis a serem utilizados no caso foi criado um perfil de testes, mas outros poderiam ser criados la tbm e podemos fazer configurações especificas para os perfis
podendo criar uma classe de configuração que n e nem um dos 3 tipos ela e uma classe auxiliar que faz algumas configurações na aplicação

### Explicação sobre o arquivo application-test
#### DATASOURCE
#1. Define o driver que o Spring Boot vai usar para se conectar ao banco de dados o H2 é um banco de dados leve e embutido, ideal para testes.
#2. Define a URL de conexão do banco.jdbc:h2:mem:testdb Significa que o banco será criado na memória (mem) e chamado testdb.
#OBS: O banco some quando o aplicativo é encerrado, já que ele e em memória.
#3. Define o usuario
#4. A senha

#### H2 CLIENT

#1. Ativa a interface web do banco H2 permitindo acessar os dados pelo navegador.
#2. Define o endereço da interface web de adm -> http://localhost:8080/h2-console.


#### JPA, SQL
#1. Define o dialeto do Hibernate o Hibernate precisa saber como traduzir as consultas SQL para o banco H2.
#2. Faz o Spring esperar a criação do banco antes de rodar scripts SQL iniciais para testes, garantindo que o banco existe antes da inicialização.
#3. Exibe no console todas as consultas SQL executadas pelo Hibernate.
#4. Formatar a saída SQL no console para ficar mais legível
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
## JPA REPOSITORY, INJEÇÃO DE DEPENDENCIAS, DATABASE SEEDING 
_(Implementaçção do primeiro repositorio, utilizando o JPA repository do Spring data JPA que e um subframework do ecosistema spring. Injeçção de dependencias
automatica feita pelo container do framework. Database seeding - primeira intansiação do bd, inserir alguns dados automaticamente no bd)_
### Checklist:
_(o repositorio e a camada mais em baixo da arquitetura proposta, resource layer(rest controllers) => service layer => Data access layer(data repositories))_
- UserRepository extends JPARepository<User, Long>  //vai ser o repositorio responsavel por fazer operações com a entidade user. vc extend e passa o tipo e o tipo da chave dessa entidade.
_(OBS:Pacote repositories no plural, e o repositorio vai ser uma interface, pois o JPARespository tbm e interface e ele vai extender e não implementar. Não precisa implementar a interface, pois o spring data JPA
ja possui uma implementação padrão qquando vc define o JPARespository passando o tipo e a tipo da chave ele ja implementa)_
- Configuration class for "test" profile //Podemos fazer configurações especificas para o perfil na applicatiton.propeties //vamos criar uma classe auxiliar que não e nenhum dos 3 tipos, e ela vai fazer
_(algumas configurações na aplicação o nome da classe pode ser TestConfig e o pacotte e .config e para falar pro spring que e uma classe de configuração colocar a anotação @Configuration e para dizer que ela
  e especifica para o perfil de teste coloca outra anotação @Profile("Nome_Igual_Colocado_No_Profile") ai ele so vai rodar quando vc estiver no perfil de teste
  A classe qque salva os dados no bd e a Repository, sendo o primeiro caso de injeção de dependencia pq a classe de configuração precisa de um dependencia ao userRepository, Normalmente quando se esta usando
  um framework a injeção de dependencias implicito e automatico e para fazer isso vc tem que declarar essa dependencia e coloca em cima do atributo instanciado @AutoWired)_
- @Autowired UserRepository
- Instantiate objects in memory
- Persist objects 

_____________________________________________________________________________________________________________________________________________________________________________________________________________________
## SERVICE LAYER, COMPONENT REGISTRATION
_(existe em todo framework que faz injeção de dependencias no caso o component //na camada de serviços implementaremos regras de negocios, orquestrção de repositories (tipo //verificar o estoque ou salvar o itens para depois para salvar os pedidos, operações no geral) tem algumas desvantagens: algumas operações a camada de serviço vai passar para o repository a chamada de alguma //coisa como fazer um endppoint para recupera um usuario pelo Id, a chamada e passada para repository_
**OBS: não e obrigado a ter a camada de serviço, mas e bom para n sobrecarregar a camada de recursos com regras de negocios**
_(criar a classe UserService no pacote services, nela sera colocada a operação de buscar todos os usuario e por id)_
Order, Instant, ISO 8601
Basic new entity checklist:
- Entity
- "To many" association, lazy loading, JsonIgnore
- Repository
- Seed
- Service
- Resource 

**OBS: Quando vc tem um objeto eu vai poder ser injeto pelo mecanismo de injeção de dependência do spring a classe do objeto tem eu esta registrado no mecanismo de injeção de dependência e todo framework tem uma forma de fazer isso como a objeto service do tipo UserService no UserResource tem que estar registrada no spring voltando para a classe UserService usando anotação @Component ela já registra a classe como componente do string e ele vai pode ser injeto automaticamente, ele também tem alguma outras anotação especificas como @Repository para registra um repositório e @Service para registrar um service na camada de serviço, vamos usar o serviço já que o userService e um serviço, não precisa registrar o UserRepository pq ele ta extendendo a interface JpaRepositry que já esta registrado como componente do spring**
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# Order, Instant, ISO 8601
_(O ISO 8601 define várias possibilidades de formatos para a data sendo a mais comum "yyyy-MM-ddTHH:mm:ssZ" a letra "Z" avisa que ta no padrão UTC Timezone GNT)_ 
## Basic new entity checklist:
- Entity _(Para definir o relacionamento entre as tabelas colocar as anotações **@ManyToOne** na entidade Order, pois existe vários pedidos para 1 cliente e em baixo coloca **@JoinColumn(name = "client_id")** para mudar o nome na tabela para ficar mais fácil de visualizar qual e a chave estrangeira. Já no Client colocar **@OneToMany(mappedBy = "client")** para indicar que e uma relação um para muitos por parte do cliente e o  **mappedBy** e para indicar o nome do atributo do outro lado da associação(a chave estrangeira))_

-    "To many" association, lazy loading, JsonIgnore _(O **JPA** por padrão não carrega os objetos na relação '**ToMany**', isso se chama LazyLoading (Teria que colocar o **@JsonIgnore** no **@ManyToOne** e adicionar a linhas 'Spring.jpa.open-in-view=true' no arquivo 'application.properties') essa linha vai permitir que o **Jackson** no fim do ciclo de vida peça ao **JPA** ir no **BD** buscar os objetos)_
- Repository
- Seed _(File **TestConfig** para adicionar alguns pedidos na carga inicial do DB)_
- Service
- Resource
- _(Quando finalizar a criação dos passos anterior vai ter um problema no qual ao fazer uma requisição buscando um usuário pelo ID sera criado um loop, pois um usuário tem vários pedidos e o pedido tem um usuários e um usuário tem um pedido e assim por diante para evitar isso teremos que utilizar a anotação **@JsonIgnore** em pelo menos um dos dois lados(Colocar em cima do **@OneToMany**)_
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# OrderStatus enum 

Existe outro jeito de fazer o enum para o status do pedido (por padrão no db o java atribui um valor numérico em cada valor dos tipos enumerados ex:0, 1, 2), pois existe um problema na forma normal do
java no qual ao inserir outro tipo enum no meio ele causara problemas no valor de baixo na tabela ficando errado, pois o valor do debaixo serão incrementados em 1.
- Passo 1: Criar o Enum OrderStatus
- Passo 2: Atribuir valores numéricos aos tipos enum, Ex: PAID(2);
- Passo 3: Criar a variável code do tipo int
- Passo 4: Criar os métodos private OrderStatus(int code) _(Construtor)_; // public int getCode() return code; // public static OrderStatus valueOf(int code) _(Para retonar o status, e o métodos lança **excessão** )_;
- Passo 5: Criar uma variável tipo Integer na classe Order
- Passo 6: Alterar o get e o set para retornar um OrderStatus e modificar os comandos dentro dos métodos e no construtor usar o método set para fazer a injeção de dependência
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# Category e Products

A criação da classe Category e Products e do mesmo jeito que das classes anteriores _(Não esquecer das associações que nesse caso foram instanciadas usando o Set para que não haja ocorrência)_

## ASSOCIAÇÃO DE MUITOS PARA MUITOS 
_(Com JoinTable)_
- Quando temos a associação de muitos para muitos em POO, podemos simplesmente dizer que um produto possui várias categorias e eu uma categoria possui vários produtos, pois as listas armazenam a referencia para os objetos relacionados. Já no banco de dados isso não e possível já que ele não armazena essa referencia, e necessário criar uma tabela de associação com as chaves estrangeiras de ambas entidades associando uma as outras.
- Para fazer isso usar a anotação **@ManyToMany** e após isso a anotação **@JoinTable(name = "tb_product_category", joinColumns = @JoinColumn(name = "product_id"), 
inverseJoinColumns = @JoinColumn(name ="category_id"))** criando uma tabela de associação, após isso e necessário ir na classe category e adicionar a anotação para mapear a classe category 
**@ManyToMany(mappedBy = "categories")** junto com a anotação **@JsonIgnore** para criar um LazyLoading e evitar o loop infinito.
- Na classe de configuração e necessário indicar as associações eu serão feitas antes de salvar novamente a tabela products, Ex: p2.getCategories().add(cat1) /// p2.getCategories().add(cat3);
- **Obs: E necessário salvar novamente a tabela após indicar as associações**.
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# Entidades OrderItem com associação de muitos para muitos com atributos extras

## Checklist:
- ### OrderItemPK _(Classe auxiliar que representara o par Product e Order, na tabela possuindo as chaves estrangeiras assim criando uma chave composta, além de não possuir uma chave primaria própria, mas usara a chave composta como chave primaria dentro de OrderItem)_
- Criar uma classe auxiliar chamada OrderItemPK no pacote entities.pk, que terá referencia para as duas entidades
_(OBS: Uma classe auxiliar no java é uma classe criada para fornece suporte a outra classe. Costumam conter métodos estáticos ou instancias auxiliares para evitar repetição de código.
- A classe não terá construtor _( O JPA exige um construtor padrão (sem argumentos) para poder instanciar objetos automaticamente ao recuperar dados do banco. Como Java já cria um construtor padrão automaticamente, não é necessário escrevê-lo manualmente)_ 
- terá getters e setters, hashcode e equals, serializable
#### Anotações a serem adicionados 
- **@Embeddable** em cima da classe para indicar que não e uma classe independente, mas sim um componente que será incorporado em outra entidade
- **@ManyToOne** e uma relação de muitos para um _(Colocar em cima do atributo order e product)_
- @JoinColumn (name = "exemplo_id")
- ### OrderItem
- O primeiro atributo será o identificador correspondente a chave primaria _(OrderItemPK id = new OrderItemPK();)_
- Não colocar o id no construtor nem no set e get automaticamente _(será colocado manualmente)_ Ex do constutor: public OrderItem (Order order, Product product, Integer quantity, Double price) { 
//n id.setOrder(order); //n id.setProduct(product); 
- Ver o exemplo de get e set no código
- Colocar as anotttações **@EmbeddedId** em cima do atributo id e **@JsonIgnore** _(Representa a chave composta)_ em cima do getOrder, pois ele retornar a order e criara um loop.
- ### Order one-to-many association
- Nos temos uma associação um para muitos, um pedido podendo ter vários items criar uma coleção com OrderItem e colocar a anotação **@OneToMany(mappedBy = "id.order")**, já que no OrderItem temos o id e dentro do id temos o order.
- ### Seed 
- Semelhante aos outros ex: OrderItem oi1 = new OrderItem(o1, p1, 2, p1.getPrice());
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# Associação muitos para muitos entre Product e OrderItem

- Criar uma associação entre Product e OrderItem semelhante ao de Order
- Teremos que varrer a coleção de OrderItem associado ao produto e para cada OrderItem tem que associar ao objeto Oder associado ao OrderItem, devolvendo uma lista de Order.
-  _(Controller Rest responde requisição HTTP e retorna dados em forma de Json. Por padrão, a biblioteca **Jackson utiliza os métodos getters para serializar objetos Java em JSON e para desserializar JSON em objetos Java. Isso significa que, ao converter um objeto Java em JSON, Jackson chamará todos os métodos getters disponíveis para obter os valores dos campos)_

_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# Entidade Payment OneToOne

- Cria a classe normalmente, na associação com a classe Order colocar a anotação **@OneToOne** e **MapsId** _(e usada para mapear uma relação de chave primária composta, diz ao JPA que a chave primária da entidade atual deve ser igual a chave primária da entidade relacionada)_
- Colocar no atributo payment na classe Order a anotação **@OneToOne(mappedBy = "order", cascade = CascadeType.All)** _(O cascade significa eu todas as operações de persistência realizada em Order serão automaticamente aplicadas a Payment e o @OneToOne define as operações de persistência (como salvar, atualizar, deletar) realizadas na entidade principal serão propagadas  para as entidades relacionadas))_
- No arquivo de configuração de test instanciar um Payment e fazer uma associação de mão dupla associando pedido 1 com o payment 1

_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# Métodos de subtotal e total

- Adicionar o método **getSubTotal()** na classe **OrderItem** e o método **getTotal()** na classe Order
OBS: _**(Lembrar que o Jackson pega todos os gets por padrão)**_ e _**(Que trabalhamos com objetos)**_

_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# User insert
## Checklist:
- UserService: Adicionar o método insert que retorna um User
- UserResource: Adicionar um método insert com as anotações **@PostMapping** _(Indica que este método será chamado quando uma requisição POST for feita para o endpoint correspondente.Geralmente, POST é usado para criar novos recursos no servidor)_ e a anotação **@RequestBody** _(O método recebe um objeto JSON no corpo da requisição (@RequestBody User obj).Esse JSON é convertido automaticamente para um objeto User pelo Spring Boot)_
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# User delete 
## Checklist:
- UserService: Criar método delete recebendo o id e utilizando o método deleetById do JPARepository
- UserResource Cria um método com a anotação **@DeleteMapping (value = "/{id}")** _(faz com que o ID recebido na URL seja passado como argumento para o método))_ e a anotação **@PathVariable Long id** _(faz com que o ID recebido na URL seja passado como argumento para o método)_
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
# User update
## Checklist:
- UserService Criar os métodos **update** _(Recebendo os parâmetros Long(id) e User, após isso criando um objeto User e usar o obter uma referência do usuário no banco sem carregar os dados imediatamente usando o repository.getReferenceById(id), depois e só chamar o método updateData e retornar o o repository.save(entity))_ e **updateData** _(atualiza as informações utilizando os setters)_
- UserResource: cria o método update com a anotação **PutMapping** _(usada no Spring Boot para mapear requisições HTTP do tipo PUT para um método específico no controller)_ e recebendo como parâmetro do método **@PathVariable Long id** _(extrai o valor do {id} da URL e o passa como argumento para o método)_ e **@RequestBody User obj** _(o corpo da requisição HTTP (JSON enviado pelo cliente) é convertido automaticamente para um objeto User)_
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
