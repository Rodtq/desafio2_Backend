# desafio2_Backend
desafio 2 para Lenovo - projeto do Backend

1- Passos para rodar o projeto
1.1 ter instalado o JAVA 8 e o JRE em sua máquina
1.2 ter configurado o JAVA 8 e o JRE em sua máquina https://www.java.com/pt_BR/download/help/download_options.xml
1.3 ter um servidor como o TomCat instalado http://tomcat.apache.org/
1.4 se for de sua preferência poderá rodá-lo no ambiente de desenvolvimento para isso será necessário instalar uma IDE como Netbeans.
1.5 ao instalar e configurar a IDE você deverá baixar o projeto do github e abrí-lo em sua Ide 
1.6 após ter aberto o projeto será necessário fazer o procedimento de build, para o Maven(repositório de bibliotecas e automatizador de tarefas) baixar as dependencias do projeto.
1.7 após a build concluida, você poderá rodar o programa
1.8 é necessessário instalar o banco de dados MySQL https://www.mysql.com/
1.9 é necessário instalar o gerenciador de banco do MySQL, MySQL Workbench
2.0 é necessário criar um schema chamado machinemanager para possibilitar a persistencia dos dados.

2 - Cobertura do desafio
2.1 Cadastrar uma máquina, fornecendo seu endereço, o usuário e a senha da máquina;
A máquina esta sendo cadastrada no Simulador por meio da interface gráfica, pelo segundo menu de navegação "Cadastro de máquinas" é possível cadastra e editar as informações de uma máquina referente a sua configuração. Informações como temperatura e Status  são mudados dinamicamente conforme comandos são disparados contra a máquina escolhida. A senha para todas as máquinas criadas é padrão:
username: user;
password: password;
pela simplicidade do desafio 1 e tempo disponível não foi possível uma evolução na parte de autenticação e autorização, o menu para autenticação está disponibilizado no menu "Ações" localizado na tabela na aba de gerenciamento de máquinas. A autenticação está configurada para atender a conexão com as máquinas, os outros entry points estão configurados para não pedir autenticação. Desta maneira temos um cenário melhor de simulação de máquinas. 
  
2.2 Conectar-se a uma máquina cadastrada (basicamente, garantir que as credenciais são válidas);
Está implementado, o comando de conexão pode ser encontrado no menu "Ações" localizado na tabela na aba de gerenciamento de máquinas. Para conectar, é necessário entrar com as credenciais básicas configuradas.
username: user;
password: password;
Após ligar é possível notar que o enderço ip da máquina mudou um gerador fake de Ip foi implemntado no desafio 1, é através deste enderço que será feita a conexão na máquina. Então para conectar na máquina é necessário ligá-la antes

2.3 Obter periodicamente e exibir o inventário da máquina (as informações de hardware dela);
Implementado por meio de uma rotina Javascript de 5 em 5 minutos ocorre uma atualização na tela com todas as máquias existentes no pool

2.4 Editar uma informação da máquina gerenciada (por exemplo, o nome);  
Implementado todas as informações pertinentes configuração da máquina podem ser editadas, os status são caracteristicas que podem ser editadas ao disparar um comando 

2.5 Esta aplicação deverá ser acessível via navegador, e persistir os dados de maneira simples.
Implementado, pode ser acessado via browser, faciltando , utilizando uma ide podemos subir a aplicação em poucos minutos.
Os dados estão sendo persistidos em um banco de dados MySQL com o nome de "machinemanager" as tabelas são criadas automaticamente.
Foi desenvolvida uma feature de sincronização entre o pool de máquinas e o banco de dados, caso o pool tenha uma quantidade de máquinas diferente da do banco de dados, é automaticamente feito um pareamento com base no banco de dados. Toda a modificação de configuração que for aplicada a uma máquina no Pool, também deverá ser aplicada ao banco. o Banco não persiste dados como status e temperatura, a temperatura pode ser modificada a partir da utilização da url de edição do desafio1 pois temperatura não é uma caracteristica que pode ser facilmente gerenciada
    //localhost:9001/edit
    //Payload example:
    //{
    //"id": 1,
    //"model": "teste1",
    //"serialNumber": "asd1",
    //"name": "nome",
    //"processor": "i7",
    //"memory": "8g",
    //"hd": "1tb",
    //"temperature": "23",
    //"powerStatus": 0,
    // "address":[0,0,0,0] 
    //}


2.6 O back end deverá ser escrito em Java, obrigatoriamente;
Implementado todo o backend e o desafio 1 do simulador foram escritos em JAVA com a ajuda do framework springboot

2.7 O frontend pode ser escrito na linguagem de programação à sua escolha;
Frontend foi desenvolvido com:
AngularJs: framework javascript que segue arquitetura mvvm
HTML5: para montar as views e páginas
Twitter Bootstrap: para estilizar as páginas com CSS3

2.8 As chamadas ao back end devem ser disponibilizadas através de uma interface HTTP REST
Toda a comunicação entre os programas foram feitas a partir de HTTP REST e seus verbos GET e POST

