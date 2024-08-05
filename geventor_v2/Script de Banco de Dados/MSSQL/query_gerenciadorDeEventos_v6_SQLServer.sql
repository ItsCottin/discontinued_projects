-- SQLINES LICENSE FOR EVALUATION USE ONLY
create table TBL_G_USUARIO(
ID_USUA bigint primary key identity,
LOGIN_USUA varchar(50) not null,
NOME_USUA varchar(100) not null,
CPF_CNPJ_USUA varchar(14),
EMAIL_USUA varchar(50) not null,
ENDERECO_USUA varchar(150),
SENHA_USUA varchar(800) not null,
TP_PESSOA_USUA varchar(8),
TELEFONE_USUA varchar(15),
CELULAR_USUA varchar(15),
TP_USUA VARCHAR(10) default 'COMUM',
CEP_USUA VARCHAR(10),
BAIRRO_USUA VARCHAR(100),
UF_USUA VARCHAR(10),
CIDADE_USUA VARCHAR(100),
NUMERO_CASA_USUA VARCHAR(10),
DT_ALTER_USUA DATETIME2(0),
AVATAR_DIR varchar(100) default 'default.png'
);

-- Tabela Evento
-- SQLINES LICENSE FOR EVALUATION USE ONLY
create table TBL_G_EVENTO(
ID_EVEN bigint primary key identity,
ID_USUA bigint,
ENDERECO_EVEN varchar(100) not null,
CIDADE_EVEN varchar(100) not null,
ESTADO_EVEN varchar(20) not null,
DATAINICIO_EVEN DATETIME2(0) not null,
DATAFIM_EVEN DATETIME2(0) not null,
TITULO_EVEN varchar(100) not null,
DESCRICAO_EVEN varchar(500) not null,
VAGAS int not null,
SITE_EVEN varchar(250),						-- 0.3.0
EMAIL_EVEN varchar(50) not null,
TELEFONE_EVEN varchar(15) not null,
SITE_PROPRIO bit default 1,
CEP_EVEN VARCHAR(10) not null, 				-- 0.2.9
BAIRRO_EVEN VARCHAR(100) not null,  		-- 0.2.9
NUMERO_LUGAR_EVEN VARCHAR(10) not null,		-- 0.2.9
DT_ALTER_EVEN DATETIME2(0),						-- 0.2.9
MY_ENDERECO_EVEN bit default 0,		-- 0.2.9
MY_TELEFONE_EVEN bit default 0,		-- 0.2.9
VLR_EVEN varchar(10) default 'Pago',		-- 0.2.9
GUID_EVEN varchar(100) not null,			-- 0.3.0
foreign key (ID_USUA) references TBL_G_USUARIO(ID_USUA)
);

-- tabela Atividade
-- SQLINES DEMO *** OR EVALUATION USE ONLY
-- SQLINES LICENSE FOR EVALUATION USE ONLY
create table TBL_G_ATIVIDADE(
ID_ATIVI bigint primary key identity,
ID_EVEN bigint,
NOME_ATIVI varchar(100) not null,
DATAINICIO_ATIVI DATETIME2(0) not null,
DATAFIM_ATIVI DATETIME2(0) not null,
DETALHES_ATIVI varchar(500) not null,
ORGANIZACAO_ATIVI varchar(100) null,
VAGAS_ATIVI int not null,
PRECO_ATIVI varchar(10) not null,
IS_PERIODO_EVEN_ATIVI bit default 0,
DT_ALTER_ATIVI DATETIME2(0) not null
foreign key (ID_EVEN) references  TBL_G_EVENTO(ID_EVEN)
);


-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE TBL_G_USUARIO_ATIVIDADE (
ID_USUA_ATIV bigint primary key identity,
ID_USUA bigint,
ID_ATIVI bigint,
foreign key (ID_USUA) references  TBL_G_USUARIO(ID_USUA),
foreign key (ID_ATIVI) references  TBL_G_ATIVIDADE(ID_ATIVI),
STATUS VARCHAR(10) not null default 'PENDENTE'
);

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE TBL_G_NOTIFICACAO (
ID_NOTIF bigint primary key identity,
ID_USUA bigint,
DETALHES_NOTIF varchar(500) not null,
TITULO_NOTIF varchar(100) not null,
TIPO_NOTIF varchar(100) not null,
VISU_NOTIF bit,
DT_INC_NOTIF DATETIME2(0),
foreign key (ID_USUA) references TBL_G_USUARIO(ID_USUA),
);