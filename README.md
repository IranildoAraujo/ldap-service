# ldap-service

## Instruções

O objetivo é fornecer uma cópia do projeto para fins de desenvolvimento e teste.

## Pré-requisitos

- Sistema Operacional Ubuntu(Versão em que a API foi testada: 24.04 LTS)
- Configurar seu ambiente para encode UTF8
- Instalar o Java 17.
- Instalar o Spring tool Suite 4
- Instalar o servidor LDAP
- Instalar a ferramenta para testes de requisições(Insomnia, Postman ou outro).
- Instalar o Lombok
- Instalar o Docker(version 24.0.7)
- Instalar o Maven
- (Opcional) [Link para instalação do sonarlint](https://marketplace.eclipse.org/content/sonarlint)
- (Opcional) [Link para instalação do Eclemma](https://www.eclemma.org/jacoco/)

## Importar

Importar o projeto como um projeto Maven existente.

## Variáveis de ambiente

- SPRING_PROFILES_ACTIVE=dev
- LDAP_SERVICE={Adicionar o nome do serviço ldap}
- LDAP_URL={Adicione a url de conexão com o servidor ldap}
- LDAP_BASE={Adicione o nome da base}
- LDAP_USER={Adicione o usuário de ldap}
- LDAP_PASSWORD={Adicione o password de autênticação com o ldap}

## Utilização

O servidor LDAP usado para testes no projeto foi fornecido 
pelo docker hub atráves do seguinte comando no terminal:

```bash
docker run --name my-openldap-container \
           -p 389:389 \
           -p 636:636 \
           -e LDAP_ORGANISATION="Example Inc" \
           -e LDAP_DOMAIN="example.com" \
           -e LDAP_ADMIN_PASSWORD="adminpassword" \
           -d osixia/openldap:1.5.0
```

## Parâmetros explicados:

- Nome do container= `--name my-openldap-container`
- Mapeia a porta LDAP padrão para a porta local= `-p 389:389`
- Mapeia a porta LDAPS (LDAP sobre SSL) para a porta local= `-p 636:636` 
- Define o nome da organização= `-e LDAP_ORGANISATION="Example Inc"`
- Define o domínio LDAP= `-e LDAP_DOMAIN="example.com"`
- Define a senha do administrador= `-e LDAP_ADMIN_PASSWORD="adminpassword"`

## Comandos para gerenciar a imagem docker do OpenLDAP:

- Verificar container em execução:

```bash
sudo docker ps
```

- Parar o container por CONTAINER_ID ou o NAMES:

```bash
sudo docker stop <CONTAINER_ID ou o NAMES>
```

- Inicializar o container por CONTAINER_ID ou o NAMES:

```bash
sudo docker start <CONTAINER_ID ou o NAMES>
```

- Remover o container:

```bash
sudo docker rm <CONTAINER ID ou o NAMES>
```

<span style="color:red"><strong>ATENÇÃO: </strong></span> 
Ter permissões de administrador no sistema operacional testado.
O Docker não vai deixar remover se ele estiver em execução.