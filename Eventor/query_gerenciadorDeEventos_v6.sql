-- Banco de dados gerenciador de eventos

-- host: 172.16.48.10
-- usuario: pluri
-- senha: t@11ta341

-- tabela Usuario
create database GEVENTOR
GO
USE GEVENTOR
GO
create table TBL_USUARIO(
ID_USUA bigint primary key identity,
LOGIN_USUA varchar(50) not null,
FUNCAO_USUA varchar(25),
AVALIACAO_USUA float not null default '0.0',
NOME_USUA varchar(100) not null,
CPF_CNPJ_USUA varchar(14),
EMAIL_USUA varchar(50) not null,
ENDERECO_USUA varchar(150),
SENHA_USUA varchar(800) not null,
TP_PESSOA_USUA varchar(8),
QR_CODE_USUA varchar(200),
TELEFONE_USUA varchar(15),
TP_COLAB_USUA varchar(25)
)

-- tabela Demanda
/*create table TBL_DEMANDA(
ID_DEMAN bigint primary key identity,
CUSTO_PREVISTO_DEMAN float not null,
CUSTO_REALIZADO_DEMAN float default '0.00',
VALOR_INICIAL_DEMAN float default '0.00',
INICIO_DEMAN datetime not null,
FINAL_DEMAN datetime not null
)*/

-- Tabela Evento
create table TBL_EVENTO(
ID_EVEN bigint primary key identity,
ID_USUA bigint foreign key references TBL_USUARIO(ID_USUA),
--ID_DEMAN bigint foreign key references TBL_DEMANDA(ID_DEMAN),
LOCAL_EVEN varchar(300) not null,
CIDADE_EVEN varchar(20) not null,
ESTADO_EVEN varchar(20) not null,
DATAINICIO_EVEN DateTime not null,
DATAFIM_EVEN DateTime not null,
TITULO_EVEN varchar(100) not null,
DESCRICAO_EVEN varchar(500) not null,
VAGAS int not null,
RESPONSAVEL_EVEN varchar(50) not null,
TWITTER_EVEN varchar(200),
FACEBOOK_EVEN varchar(200),
SITE_EVEN varchar(50),
EMAIL_EVEN varchar(50) not null,
TELEFONE_EVEN varchar(15) not null,
PRECO_EVEN varchar(100) not null
)

select * from TBL_EVENTO

-- tabela notificação
create table TBL_NOTIFICACAO(
ID_NOTIF bigint primary key identity,
ID_EVEN bigint foreign key references TBL_EVENTO(ID_EVEN),
--ID_DEMAN bigint foreign key references TBL_DEMANDA(ID_DEMAN),
LINK_NOTIF varchar(1000),
TITULO_NOTIF varchar(50) not null,
DESCRICAO_NOTIF varchar(200) not null
) 

-- tabela USUARIO/NOTIFICAÇÂO
create table TBL_USUARIO_NOTIFICACAO(
ID_USUA_NOTIF bigint primary key identity,
ID_USUA bigint foreign key references TBL_USUARIO(ID_USUA),
ID_NOTIF bigint foreign key references TBL_NOTIFICACAO(ID_NOTIF),
VISUALIZADO bit default '0'
)

-- Tabela Controle
/*create table TBL_CONTROLE(
ID_CONTR bigint primary key identity,
ID_EVEN bigint foreign key references TBL_EVENTO(ID_EVEN),
CRACH_CONTR varchar(300),
CREDE_CONTR varchar(700)
)*/

-- Tabela Visitante
create table TBL_VISITANTE(
ID_VISIT bigint primary key identity,
NOME_VISIT varchar(100) not null,
EMAIL_VISIT varchar(100) not null,
TELEFONE_VISIT varchar(18),
ENDERECO_RESIDENT_VISIT varchar(500),
ENDERECO_COMERC_VISIT varchar(500),
CARGO_VISIT varchar(20),
EMPRESA_VISIT varchar(20),
DATANASCIMENTO_VISIT date,
CPF_VISIT varchar(20),
CNPJ_VISIT varchar(20)
)

-- Tabela associativa EVENTO/VISITANTE
create table TBL_EVENTO_VISITANTE(
ID_EVEN_VISIT bigint primary key identity,
ID_VISIT bigint foreign key references TBL_VISITANTE(ID_VISIT),
ID_EVEN bigint foreign key references TBL_EVENTO(ID_EVEN),
PRESENCA_COLAB varchar(10) default 'ACEITO'
)

-- Tabela Medalha
/*create table TBL_MEDALHA(
ID_RECOM bigint primary key identity,
ID_EVEN bigint foreign key references TBL_EVENTO(ID_EVEN),
NOME_MEDAL varchar(30)
)*/

-- Tabela associativa Colaborador e Medalha
/*create table TBL_MEDALHA_USUARIO(
ID_RECOM bigint foreign key references TBL_MEDALHA(ID_RECOM),
ID_USUA bigint foreign key references TBL_USUARIO(ID_USUA)
)*/

-- tabela Atividade
create table TBL_ATIVIDADE(
ID_ATIVI bigint primary key identity,
ID_EVEN bigint foreign key references TBL_EVENTO(ID_EVEN),
NOME_ATIVI varchar(100) not null,
DATAINICIO_ATIVI DateTime not null,
DATAFIM_ATIVI DateTime not null,
DETALHES_ATIVI varchar(500) not null,
ORGANIZACAO_ATIVI varchar(100) not null,
VAGAS_ATIVI int not null,
PRECO_ATIVI varchar(10) not null
)

create table TBL_PARTICIPANTE(
ID_PART bigint primary key identity,
TIPO_PART varchar(20) not null
)

create table TBL_PARTICIPANTE_USUARIO(
ID_USUA bigint foreign key references TBL_USUARIO(ID_USUA),
ID_PART bigint foreign key references TBL_PARTICIPANTE(ID_PART)
)

-- Demanda
/*INSERT INTO TBL_DEMANDA(CUSTO_PREVISTO_DEMAN,CUSTO_REALIZADO_DEMAN,VALOR_INICIAL_DEMAN,INICIO_DEMAN,FINAL_DEMAN) VALUES (100.00,100,100,CONVERT(DATETIME,'10/10/2016',103),CONVERT(DATETIME,'09/11/2016',103));
INSERT INTO TBL_DEMANDA(CUSTO_PREVISTO_DEMAN,CUSTO_REALIZADO_DEMAN,VALOR_INICIAL_DEMAN,INICIO_DEMAN,FINAL_DEMAN) VALUES (200,300,100,CONVERT(DATETIME,'10/12/2016',103),CONVERT(DATETIME,'27/12/2016',103));
*/

-- Evento
--INSERT INTO TBL_EVENTO(RECURSOS_DEMAN,LOCAL_DEMAN,DATA_DEMAN,DURACAO_DEMAN,NOME_DEMAN,DESCRICAO_DEMAN,RECOMENSA_DEMAN) VALUES ('TESTE RECURSOS_DEMAN','TESTE LOCAL_DEMAN',CONVERT(DATETIME,'10/10/2016',103),'10','TESTE NOME_DEMAN','TESTE DESAC','RECOMENSA_DEMAN');

-- Usuario
INSERT INTO TBL_USUARIO(LOGIN_USUA,
						FUNCAO_USUA,
						NOME_USUA,
						CPF_CNPJ_USUA,
						EMAIL_USUA,
						ENDERECO_USUA,
						SENHA_USUA,
						TP_PESSOA_USUA,
						QR_CODE_USUA,
						TELEFONE_USUA,
						TP_COLAB_USUA) 
				VALUES ('roger',
						'programador',
						'rodrigo',
						'430.345.122-56',
						'cottingfontes@hotmail.com',
						'pq arariba',
						'e10adc3949ba59abbe56e057f20f883e',
						'FISICA',
						'www.geventorqrcode.com\?key=hdffoaerfss',
						40028922,
						'REMUNERADO');
INSERT INTO TBL_USUARIO(LOGIN_USUA,
						FUNCAO_USUA,
						NOME_USUA,
						CPF_CNPJ_USUA,
						EMAIL_USUA,
						ENDERECO_USUA,
						SENHA_USUA,
						TP_PESSOA_USUA,
						QR_CODE_USUA,
						TELEFONE_USUA,
						TP_COLAB_USUA) 
				VALUES ('rfontes',
						'programador',
						'Rodrigo Cotting',
						'430.345.122-56',
						'cottingfontes@hotmail.com',
						'pq vila maria',
						'',
						'FISICA',
						'www.geventorqrcode.com\?key=hdffoaerfss',
						40028922,
						'REMUNERADO');

SELECT * FROM TBL_USUARIO


