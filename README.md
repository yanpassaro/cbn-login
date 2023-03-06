# CBN LOGIN API

#### Gerenciamento do fluxo de contas dos usuários e responsavel também pelos ciclos de vida dos tokens.
Essa API é baseada em príncipios de microservices, sendo capaz de se comunicar com diversos outros serviços via RabbitMQ.

### Features

- [x] Login
- [x] Registro
- [ ] Autenticação de token
- [ ] Verificação via email

# Como usar

Esse projeto utiliza a versão 17 do Java. Para instalar clique [aqui](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

## Banco de dados

Os bancos de dados utilizados foram o [PostgreSQL](https://www.postgresql.org) e o [Redis](https://redis.io/). 

Já os testes utilizam o Hibernate. 

Eu recomendo o uso do Docker para instalação.

## Instalação

	git clone https://github.com/yanpassaro/login-api
	cd login-api

## Configuração

> É necessário a criação de um arquivo .env

> Nele você irá colocar as credenciais necessárias para utilização do ap, conforme o [application.properties](./src/main/resources/application.properties)

> Feito isso, o aplicativo já pode ser iniciado

## References

+ [Spring Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
+ [PostgreSQL Documentation](https://www.postgresql.org/docs/)
+ [Redis Documentation](https://redis.io/docs/)
