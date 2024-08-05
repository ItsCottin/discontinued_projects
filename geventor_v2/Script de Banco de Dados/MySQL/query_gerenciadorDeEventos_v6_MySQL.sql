-- SQLINES DEMO *** renciador de eventos

-- host: 172.16.48.10
-- usuario: pluri
-- senha: t@11ta341

-- tabela Usuario
 
USE TCCRCF;
 
-- SQLINES LICENSE FOR EVALUATION USE ONLY
create table TBL_USUARIO(
ID_USUA bigint(10) primary key auto_increment,
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
DT_ALTER_USUA DateTime,
AVATAR_DIR varchar(100) default 'default.png'
);

-- Tabela Evento
create table TBL_EVENTO(
ID_EVEN bigint(10) primary key auto_increment,
ID_USUA bigint(10),
ENDERECO_EVEN varchar(100) not null,
CIDADE_EVEN varchar(100) not null,
ESTADO_EVEN varchar(20) not null,
DATAINICIO_EVEN DateTime not null,
DATAFIM_EVEN DateTime not null,
TITULO_EVEN varchar(100) not null,
DESCRICAO_EVEN varchar(500) not null,
VAGAS int not null,
SITE_EVEN varchar(250),						-- 0.3.0
EMAIL_EVEN varchar(50) not null,
TELEFONE_EVEN varchar(15) not null,
PRECO_EVEN varchar(100) not null,
SITE_PROPRIO bool default true,
CEP_EVEN VARCHAR(10) not null, 				-- 0.2.9
BAIRRO_EVEN VARCHAR(100) not null,  		-- 0.2.9
NUMERO_LUGAR_EVEN VARCHAR(10) not null,		-- 0.2.9
DT_ALTER_EVEN DateTime,						-- 0.2.9
MY_ENDERECO_EVEN bool default false,		-- 0.2.9
MY_TELEFONE_EVEN bool default false,		-- 0.2.9
VLR_EVEN varchar(10) default 'Pago',		-- 0.2.9
GUID_EVEN varchar(100) not null,			-- 0.3.0
foreign key (ID_USUA) references TBL_USUARIO(ID_USUA)
);

-- SQLINES LICENSE FOR EVALUATION USE ONLY
select * from TBL_EVENTO;

-- tabela Atividade
-- SQLINES LICENSE FOR EVALUATION USE ONLY
create table TBL_ATIVIDADE(
ID_ATIVI bigint(10) primary key auto_increment,
ID_EVEN bigint(10),
NOME_ATIVI varchar(100) not null,
DATAINICIO_ATIVI DateTime not null,
DATAFIM_ATIVI DateTime not null,
DETALHES_ATIVI varchar(500) not null,
ORGANIZACAO_ATIVI varchar(100) null,
VAGAS_ATIVI int not null,
PRECO_ATIVI varchar(10) not null,
IS_PERIODO_EVEN_ATIVI bool default false,
foreign key (ID_EVEN) references  TBL_EVENTO(ID_EVEN)
);


CREATE TABLE TBL_USUARIO_ATIVIDADE (
ID_USUA_ATIV bigint(10) primary key auto_increment,
ID_USUA bigint(10),
ID_ATIVI bigint(10),
foreign key (ID_USUA) references  TBL_USUARIO(ID_USUA),
foreign key (ID_ATIVI) references  TBL_ATIVIDADE(ID_ATIVI),
STATUS VARCHAR(10) not null default 'PENDENTE'
);

CREATE TABLE TBL_NOTIFICACAO (
ID_NOTIF bigint(10) primary key auto_increment,
ID_USUA bigint(10),
DETALHES_NOTIF varchar(500) not null,
TITULO_NOTIF varchar(100) not null,
TIPO_NOTIF varchar(100) not null,
VISU_NOTIF bool,
DT_INC_NOTIF DateTime,
foreign key (ID_USUA) references TBL_USUARIO(ID_USUA),
);

-- Usuario
-- SQLINES LICENSE FOR EVALUATION USE ONLY
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
						'www.geventorqrcode.com?key=hdffoaerfss',
						40028922,
						'REMUNERADO');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
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
						'www.geventorqrcode.com?key=hdffoaerfss',
						40028922,
						'REMUNERADO');

-- SQLINES LICENSE FOR EVALUATION USE ONLY
SELECT * FROM TBL_USUARIO


-- alterações: 16/04/2023
-- 
-- alter table tbl_usuario add column AVATAR_DIR varchar(100) default 'default.png'
-- 
-- update tbl_usuario
-- set AVATAR_DIR = 'default.png'
-- where ID_USUA in ('1','2','3')

-- alterações: 17/04/2023 for version 0.2.9
-- alter table TBL_EVENTO add column CEP_EVEN VARCHAR(10) not null;
-- alter table TBL_EVENTO add column BAIRRO_EVEN VARCHAR(100) not null;
-- ALTER TABLE TBL_EVENTO DROP column LOCAL_EVEN;
-- alter table TBL_EVENTO add column ENDERECO_EVEN varchar(100) not null;
-- alter table TBL_EVENTO add column NUMERO_LUGAR_EVEN VARCHAR(10) not null;
-- alter table TBL_EVENTO add column DT_ALTER_EVEN DateTime;

-- alterações: 18/04/2023 for version 0.2.9
-- alter table TBL_EVENTO add COLUMN MY_ENDERECO_EVEN bool default false;
-- alter table TBL_EVENTO add COLUMN MY_TELEFONE_EVEN bool default false;

-- alter table TBL_EVENTO drop COLUMN PRECO_EVEN;
-- alter table TBL_EVENTO add COLUMN EVEN_PAGO varchar(10) default 'Pago'

-- alter table TBL_ATIVIDADE drop COLUMN ORGANIZACAO_ATIVI;
-- alter table TBL_ATIVIDADE add COLUMN ORGANIZACAO_ATIVI varchar(10) default 'Comum'
-- alter table TBL_ATIVIDADE add column DT_ALTER_ATIVI DateTime;

-- alter table TBL_ATIVIDADE add COLUMN IS_PERIODO_EVEN_ATIVI bool default false;

-- alter table TBL_EVENTO add column GUID_EVEN varchar(100) not null;
-- alter table TBL_EVENTO MODIFY COLUMN SITE_EVEN varchar(250);	

-- alter table TBL_NOTIFICACAO add TIPO_NOTIF varchar(100) not null;
