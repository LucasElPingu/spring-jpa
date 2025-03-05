# spring-jpa
 São anotações e observações que escreverei aqui dos passos que seguirei na criação do projeto

#OBJETIVOS
 Criar projeto Spring Boot Java
 Implementar modelo de domínio //Modelo com as entidades do negócios, com vários relacionamentos
 Estruturar camadas lógicas: resource, service, repositor //o backend e dividido em camadas resourcer(controladores rest): são a interface da aplicação com o backend, vão receber as requisições e responder
de acordo com o compartimento do sistema //todas as camadas se comunicao com a camada de entidades
 Configurar banco de dados de teste (H2) //banco de dados em memoria do java
 Povoar o banco de dados
 CRUD - Create, Retrieve, Update, Delete //operações de um cadastro completo de uma entidade
 Tratamento de exceções 

#FERRAMENTAS A SEREM UTILIZADAS
.Spring boot
.Apache Tomcat //contêiner web para executar a aplicação
.maven //gerenciado de dependências
.h2 //banco de dados em memoria para testes
.postman
.PostgreSQL //
.heroku //Plataforma em nuvem como serviço

OBS: O SPRING BOOT TA NO 3 A AULA TA NO 2, PRESTAR ATENÇÃO NISSO
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
#PROJECT CREATED
Checklist:
 Spring Initializr //PROGAMA WEB PARA CRIAR PROJETOS SPRING BOOT, COLOCA NO GOOGLE
o Maven
o Java 17
o Packing JAR
o Dependencies: Spring Web
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
#USER ENTITY AND RESOURCES 
//vai ser o recursos web correspondente a entidade user, vai disponibilizar 2 endpoints para recuperarmos os usuários cadastrados e usuários pelo id.
Basic entity checklist:
 Basic attributes
 Associations (instantiate collections)
 Constructors
 Getters & Setters (collections: only get)
 hashCode & equals
 Serializable //permite que o objeto seja transformado em cadeia de bytes, para que ele trafegue na rede, seja gravado em arquivos
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
#H2 DATABASE, TEST PROFILE, JPA
//Configurar o banco de dados de testes (h2) ele e um banco de dados em memória ele já vem integrado ao projeto e so rodar que ele vai ser criado. Profile de teste e um perfil
do projeto especifico para fazer testes, existe outros perfis que podem ser difinidos com por exemplo um perfil de desenvolvimento com outro banco de dados específicos para não precisar toda vez que projeto roda,
perfil de produção que e quando o projeto já esta implando no cliente que vai usar o sistema.
Checklist:
 JPA & H2 dependencies //as dependências poderiam ter sidos adicionadas no Spring Initializr
 application.properties //src/main/resources //para criar um perfil de teste vai nesse file e add (spring.profiles.active=test //nome /n spring.jpa.open-in-view=true //)
 application-test.properties //Criar esse arquivo src/main/resources/application-test.properties //vai ter as configurações do banco de dados h2
 Entity: JPA mapping //So tem a entidade 'user' que e uma entidade no modelo de dominio, colocar anotações do JPA para insttruir a ele como ele vai converter os objetos para o modelo relacional
OBS: NO SPRING 3 O 'Entity' e do pacote jakarta e colocar o @Table(name = "tb_user") em cima da classe //isso vai especificar o nome da tabela no bd, e necessario renomear pq o nome User e uma palavra reservado
do banco de dados H2 necessario para n dar conflito, entçao mudar o nome da tabela.

Explicação sobre o arquivo application.properties
Define os tipos de perfis a serem utilizados no caso foi criado um perfil de testes, mas outros poderiam ser criados la tbm e podemos fazer configurações especificas para os perfis
podendo criar uma classe de configuração que n e nem um dos 3 tipos ela e uma classe auxiliar que faz algumas configurações na aplicação

Explicação sobre o arquivo application-test
DATASOURCE
#1. Define o driver que o Spring Boot vai usar para se conectar ao banco de dados o H2 é um banco de dados leve e embutido, ideal para testes.
#2. Define a URL de conexão do banco.jdbc:h2:mem:testdb Significa que o banco será criado na memória (mem) e chamado testdb.
#OBS: O banco some quando o aplicativo é encerrado, já que ele e em memória.
#3. Define o usuario
#4. A senha

#1. Ativa a interface web do banco H2 permitindo acessar os dados pelo navegador.
#2. Define o endereço da interface web de adm -> http://localhost:8080/h2-console.
H2 CLIENT

JPA, SQL
#1. Define o dialeto do Hibernate o Hibernate precisa saber como traduzir as consultas SQL para o banco H2.
#2. Faz o Spring esperar a criação do banco antes de rodar scripts SQL iniciais para testes, garantindo que o banco existe antes da inicialização.
#3. Exibe no console todas as consultas SQL executadas pelo Hibernate.
#4. Formatar a saída SQL no console para ficar mais legível
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
#JPA REPOSITORY, INJEÇÃO DE DEPENDENCIAS, DATABASE SEEDING 
//Implementaçção do primeiro repositorio, utilizando o JPA repository do Spring data JPA que e um subframework do ecosistema spring. Injeçção de dependencias
//automatica feita pelo container do framework. Database seeding - primeira intansiação do bd, inserir alguns dados automaticamente no bd. 
Checklist:
//o repositorio e a camada mais em baixo da arquitetura proposta, resource layer(rest controllers) => service layer => Data access layer(data repositories)
 UserRepository extends JPARepository<User, Long>  //vai ser o repositorio responsavel por fazer operações com a entidade user. vc extend e passa o tipo e o tipo da chave dessa entidade.
//OBS:Pacote repositories no plural, e o repositorio vai ser uma interface, pois o JPARespository tbm e interface e ele vai extender e não implementar. Não precisa implementar a interface, pois o spring data JPA
//ja possui uma implementação padrão qquando vc define o JPARespository passando o tipo e a tipo da chave ele ja implementa.
 Configuration class for "test" profile //Podemos fazer configurações especificas para o perfil na applicatiton.propeties //vamos criar uma classe auxiliar que não e nenhum dos 3 tipos, e ela vai fazer
//algumas configurações na aplicação o nome da classe pode ser TestConfig e o pacotte e .config e para falar pro spring que e uma classe de configuração colocar a anotação @Configuration e para dizer que ela
//e especifica para o perfil de teste coloca outra anotação @Profile("Nome_Igual_Colocado_No_Profile") ai ele so vai rodar quando vc estiver no perfil de teste
//A classe qque salva os dados no bd e a Repository, sendo o primeiro caso de injeção de dependencia pq a classe de configuração precisa de um dependencia ao userRepository, Normalmente quando se esta usando
//um framework a injeção de dependencias implicito e automatico e para fazer isso vc tem que declarar essa dependencia e coloca em cima do atributo instanciado @AutoWired
 @Autowired UserRepository
 Instantiate objects in memory
 Persist objects 

_____________________________________________________________________________________________________________________________________________________________________________________________________________________
SERVICE LAYER, COMPONENT REGISTRATION
//existe em todo framework que faz injeção de dependencias no caso o component //na camada de serviços implementaremos regras de negocios, orquestrção de repositories (tipo //verificar o estoque ou salvar o itens para depois para salvar os pedidos, operações no geral) tem algumas desvantagens: algumas operações a camada de serviço vai passar para o repository a chamada de alguma //coisa como fazer um endppoint para recupera um usuario pelo Id, a chamada e passada para repository
//OBS: não e obrigado a ter a camada de serviço, mas e bom para n sobrecarregar a camada de recursos com regras de negocios
//criar a classe UserService no pacote services, nela sera colocada a operação de buscar todos os usuario e por id
Order, Instant, ISO 8601
Basic new entity checklist:
 Entity
o "To many" association, lazy loading, JsonIgnore
 Repository
 Seed
 Service
 Resource 

OBS: Quando vc tem um objeto eu vai poder ser injeto pelo mecanismo de injeção de dependência do spring a classe do objeto tem eu esta registrado no mecanismo de injeção de dependência e todo framework tem uma forma de fazer isso como a objeto service do tipo UserService no UserResource tem que estar registrada no spring voltando para a classe UserService usando anotação @Component ela já registra a classe como componente do string e ele vai pode ser injeto automaticamente, ele também tem alguma outras anotação especificas como @Repository para registra um repositório e @Service para registrar um service na camada de serviço, vamos usar o serviço já que o userService e um serviço, não precisa registrar o UserRepository pq ele ta extendendo a interface JpaRepositry que já esta registrado como componente do spring
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
_____________________________________________________________________________________________________________________________________________________________________________________________________________________
