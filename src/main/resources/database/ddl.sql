-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION academias_user;

-- DROP TYPE public."estado_alumno";

CREATE TYPE public."estado_alumno" AS ENUM (
	'ACTIVO',
	'BAJA',
	'GRADUADO',
	'REINGRESO');

-- DROP TYPE public."genero";

CREATE TYPE public."genero" AS ENUM (
	'M',
	'F');

-- DROP TYPE public."operador_objetivo";

CREATE TYPE public."operador_objetivo" AS ENUM (
	'GE',
	'LE',
	'EQ');

-- DROP TYPE public."rol_usuario";

CREATE TYPE public."rol_usuario" AS ENUM (
	'SUPER_ADMIN',
	'EVALUADOR');

-- DROP TYPE public."tipo_valor_prueba";

CREATE TYPE public."tipo_valor_prueba" AS ENUM (
	'TIEMPO',
	'REPETICIONES',
	'BOOLEANO',
	'NUMERICO');

-- DROP SEQUENCE public.academia_id_seq;

CREATE SEQUENCE public.academia_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.academia_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.academia_id_seq TO academias_user;

-- DROP SEQUENCE public.alumno_id_seq;

CREATE SEQUENCE public.alumno_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.alumno_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.alumno_id_seq TO academias_user;

-- DROP SEQUENCE public.jornada_academica_id_seq;

CREATE SEQUENCE public.jornada_academica_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.jornada_academica_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.jornada_academica_id_seq TO academias_user;

-- DROP SEQUENCE public.jornada_fisica_id_seq;

CREATE SEQUENCE public.jornada_fisica_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.jornada_fisica_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.jornada_fisica_id_seq TO academias_user;

-- DROP SEQUENCE public.materia_id_seq;

CREATE SEQUENCE public.materia_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.materia_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.materia_id_seq TO academias_user;

-- DROP SEQUENCE public.programa_id_seq;

CREATE SEQUENCE public.programa_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.programa_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.programa_id_seq TO academias_user;

-- DROP SEQUENCE public.programa_materia_id_seq;

CREATE SEQUENCE public.programa_materia_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.programa_materia_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.programa_materia_id_seq TO academias_user;

-- DROP SEQUENCE public.programa_prueba_fisica_id_seq;

CREATE SEQUENCE public.programa_prueba_fisica_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.programa_prueba_fisica_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.programa_prueba_fisica_id_seq TO academias_user;

-- DROP SEQUENCE public.resultado_academico_id_seq;

CREATE SEQUENCE public.resultado_academico_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.resultado_academico_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.resultado_academico_id_seq TO academias_user;

-- DROP SEQUENCE public.resultado_fisico_id_seq;

CREATE SEQUENCE public.resultado_fisico_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.resultado_fisico_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.resultado_fisico_id_seq TO academias_user;

-- DROP SEQUENCE public.usuario_id_seq;

CREATE SEQUENCE public.usuario_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE public.usuario_id_seq OWNER TO academias_user;
GRANT ALL ON SEQUENCE public.usuario_id_seq TO academias_user;
-- public.academia definition

-- Drop table

-- DROP TABLE public.academia;

CREATE TABLE public.academia (
                                 id bigserial NOT NULL,
                                 nombre varchar(150) NOT NULL,
                                 codigo varchar(50) NULL,
                                 activo bool DEFAULT true NOT NULL,
                                 created_at timestamptz DEFAULT now() NOT NULL,
                                 updated_at timestamptz DEFAULT now() NOT NULL,
                                 CONSTRAINT academia_codigo_key UNIQUE (codigo),
                                 CONSTRAINT academia_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE public.academia OWNER TO academias_user;
GRANT ALL ON TABLE public.academia TO academias_user;


-- public.materia definition

-- Drop table

-- DROP TABLE public.materia;

CREATE TABLE public.materia (
                                id bigserial NOT NULL,
                                academia_id int8 NOT NULL,
                                nombre varchar(150) NOT NULL,
                                activo bool DEFAULT true NOT NULL,
                                created_at timestamptz DEFAULT now() NOT NULL,
                                updated_at timestamptz DEFAULT now() NOT NULL,
                                CONSTRAINT materia_pkey PRIMARY KEY (id),
                                CONSTRAINT uq_materia_nombre UNIQUE (academia_id, nombre),
                                CONSTRAINT materia_academia_id_fkey FOREIGN KEY (academia_id) REFERENCES public.academia(id) ON DELETE RESTRICT
);

-- Permissions

ALTER TABLE public.materia OWNER TO academias_user;
GRANT ALL ON TABLE public.materia TO academias_user;


-- public.programa definition

-- Drop table

-- DROP TABLE public.programa;

CREATE TABLE public.programa (
                                 id bigserial NOT NULL,
                                 academia_id int8 NOT NULL,
                                 nombre varchar(150) NOT NULL,
                                 descripcion text NULL,
                                 activo bool DEFAULT true NOT NULL,
                                 created_at timestamptz DEFAULT now() NOT NULL,
                                 updated_at timestamptz DEFAULT now() NOT NULL,
                                 "genero" public."genero" NULL,
                                 CONSTRAINT programa_pkey PRIMARY KEY (id),
                                 CONSTRAINT uq_programa_nombre UNIQUE (academia_id, nombre),
                                 CONSTRAINT programa_academia_id_fkey FOREIGN KEY (academia_id) REFERENCES public.academia(id) ON DELETE RESTRICT
);
CREATE INDEX idx_programa_academia ON public.programa USING btree (academia_id);

-- Permissions

ALTER TABLE public.programa OWNER TO academias_user;
GRANT ALL ON TABLE public.programa TO academias_user;


-- public.programa_materia definition

-- Drop table

-- DROP TABLE public.programa_materia;

CREATE TABLE public.programa_materia (
                                         id bigserial NOT NULL,
                                         programa_id int8 NOT NULL,
                                         materia_id int8 NOT NULL,
                                         orden int4 NOT NULL,
                                         nota_objetivo numeric(5, 2) NULL,
                                         activo bool DEFAULT true NOT NULL,
                                         created_at timestamptz DEFAULT now() NOT NULL,
                                         updated_at timestamptz DEFAULT now() NOT NULL,
                                         CONSTRAINT programa_materia_nota_objetivo_check CHECK (((nota_objetivo >= (0)::numeric) AND (nota_objetivo <= (20)::numeric))),
                                         CONSTRAINT programa_materia_orden_check CHECK ((orden > 0)),
                                         CONSTRAINT programa_materia_pkey PRIMARY KEY (id),
                                         CONSTRAINT uq_programa_materia UNIQUE (programa_id, materia_id),
                                         CONSTRAINT programa_materia_materia_id_fkey FOREIGN KEY (materia_id) REFERENCES public.materia(id) ON DELETE RESTRICT,
                                         CONSTRAINT programa_materia_programa_id_fkey FOREIGN KEY (programa_id) REFERENCES public.programa(id) ON DELETE RESTRICT
);

-- Permissions

ALTER TABLE public.programa_materia OWNER TO academias_user;
GRANT ALL ON TABLE public.programa_materia TO academias_user;


-- public.programa_prueba_fisica definition

-- Drop table

-- DROP TABLE public.programa_prueba_fisica;

CREATE TABLE public.programa_prueba_fisica (
                                               id bigserial NOT NULL,
                                               programa_id int8 NOT NULL,
                                               objetivo_valor numeric(12, 2) NULL,
                                               operador public."operador_objetivo" NULL,
                                               etiqueta varchar(80) NULL,
                                               activo bool DEFAULT true NOT NULL,
                                               created_at timestamptz DEFAULT now() NOT NULL,
                                               updated_at timestamptz DEFAULT now() NOT NULL,
                                               nombre varchar(150) NOT NULL,
                                               descripcion text NULL,
                                               tipo_valor public."tipo_valor_prueba" NOT NULL,
                                               unidad varchar(30) NULL,
                                               CONSTRAINT chk_objetivo_operador CHECK ((((objetivo_valor IS NULL) AND (operador IS NULL)) OR ((objetivo_valor IS NOT NULL) AND (operador IS NOT NULL)))),
                                               CONSTRAINT programa_prueba_fisica_pkey PRIMARY KEY (id),
                                               CONSTRAINT programa_prueba_fisica_programa_id_fkey FOREIGN KEY (programa_id) REFERENCES public.programa(id) ON DELETE RESTRICT
);
CREATE INDEX idx_ppf_programa ON public.programa_prueba_fisica USING btree (programa_id);

-- Permissions

ALTER TABLE public.programa_prueba_fisica OWNER TO academias_user;
GRANT ALL ON TABLE public.programa_prueba_fisica TO academias_user;


-- public.usuario definition

-- Drop table

-- DROP TABLE public.usuario;

CREATE TABLE public.usuario (
                                id bigserial NOT NULL,
                                auth0_sub varchar(255) NOT NULL,
                                email varchar(254) NOT NULL,
                                nombres varchar(150) NOT NULL,
                                rol public."rol_usuario" NOT NULL,
                                academia_id int8 NULL,
                                activo bool DEFAULT true NOT NULL,
                                created_at timestamptz DEFAULT now() NOT NULL,
                                updated_at timestamptz DEFAULT now() NOT NULL,
                                CONSTRAINT chk_super_admin_academia_null CHECK ((((rol = 'SUPER_ADMIN'::rol_usuario) AND (academia_id IS NULL)) OR ((rol = 'EVALUADOR'::rol_usuario) AND (academia_id IS NOT NULL)))),
                                CONSTRAINT usuario_auth0_sub_key UNIQUE (auth0_sub),
                                CONSTRAINT usuario_pkey PRIMARY KEY (id),
                                CONSTRAINT usuario_academia_id_fkey FOREIGN KEY (academia_id) REFERENCES public.academia(id) ON DELETE RESTRICT
);
CREATE INDEX idx_usuario_academia ON public.usuario USING btree (academia_id);

-- Permissions

ALTER TABLE public.usuario OWNER TO academias_user;
GRANT ALL ON TABLE public.usuario TO academias_user;


-- public.alumno definition

-- Drop table

-- DROP TABLE public.alumno;

CREATE TABLE public.alumno (
                               id bigserial NOT NULL,
                               academia_id int8 NOT NULL,
                               programa_actual_id int8 NULL,
                               nombres varchar(150) NOT NULL,
                               apellidos varchar(150) NOT NULL,
                               email varchar(254) NULL,
                               fecha_nacimiento date NULL,
                               "genero" public."genero" NOT NULL,
                               estatura_cm float8 NULL,
                               estado public."estado_alumno" DEFAULT 'ACTIVO'::estado_alumno NOT NULL,
                               fecha_ingreso date NULL,
                               fecha_salida date NULL,
                               foto_url text NULL,
                               created_at timestamptz DEFAULT now() NOT NULL,
                               updated_at timestamptz DEFAULT now() NOT NULL,
                               peso_kg float8 NULL,
                               imc numeric(5, 2) NULL,
                               cedula varchar(10) NULL,
                               CONSTRAINT alumno_pkey PRIMARY KEY (id),
                               CONSTRAINT uq_alumno_academia_cedula UNIQUE (academia_id, cedula),
                               CONSTRAINT uq_alumno_email UNIQUE (academia_id, email),
                               CONSTRAINT alumno_academia_id_fkey FOREIGN KEY (academia_id) REFERENCES public.academia(id) ON DELETE RESTRICT,
                               CONSTRAINT alumno_programa_actual_id_fkey FOREIGN KEY (programa_actual_id) REFERENCES public.programa(id) ON DELETE SET NULL
);
CREATE INDEX idx_alumno_academia ON public.alumno USING btree (academia_id);
CREATE INDEX idx_alumno_academia_cedula ON public.alumno USING btree (academia_id, cedula);

-- Permissions

ALTER TABLE public.alumno OWNER TO academias_user;
GRANT ALL ON TABLE public.alumno TO academias_user;


-- public.jornada_academica definition

-- Drop table

-- DROP TABLE public.jornada_academica;

CREATE TABLE public.jornada_academica (
                                          id bigserial NOT NULL,
                                          alumno_id int8 NOT NULL,
                                          academia_id int8 NOT NULL,
                                          programa_id int8 NOT NULL,
                                          fecha date NOT NULL,
                                          evaluador_id int8 NOT NULL,
                                          observacion text NULL,
                                          created_at timestamptz DEFAULT now() NOT NULL,
                                          updated_at timestamptz DEFAULT now() NOT NULL,
                                          CONSTRAINT jornada_academica_pkey PRIMARY KEY (id),
                                          CONSTRAINT uq_jornada_academica UNIQUE (alumno_id, fecha),
                                          CONSTRAINT jornada_academica_academia_id_fkey FOREIGN KEY (academia_id) REFERENCES public.academia(id) ON DELETE RESTRICT,
                                          CONSTRAINT jornada_academica_alumno_id_fkey FOREIGN KEY (alumno_id) REFERENCES public.alumno(id) ON DELETE RESTRICT,
                                          CONSTRAINT jornada_academica_evaluador_id_fkey FOREIGN KEY (evaluador_id) REFERENCES public.usuario(id) ON DELETE RESTRICT,
                                          CONSTRAINT jornada_academica_programa_id_fkey FOREIGN KEY (programa_id) REFERENCES public.programa(id) ON DELETE RESTRICT
);
CREATE INDEX idx_ja_programa_fecha ON public.jornada_academica USING btree (programa_id, fecha);

-- Permissions

ALTER TABLE public.jornada_academica OWNER TO academias_user;
GRANT ALL ON TABLE public.jornada_academica TO academias_user;


-- public.jornada_fisica definition

-- Drop table

-- DROP TABLE public.jornada_fisica;

CREATE TABLE public.jornada_fisica (
                                       id bigserial NOT NULL,
                                       alumno_id int8 NOT NULL,
                                       academia_id int8 NOT NULL,
                                       programa_id int8 NOT NULL,
                                       fecha date NOT NULL,
                                       evaluador_id int8 NOT NULL,
                                       observacion text NULL,
                                       peso_kg numeric(4, 2) NULL,
                                       imc numeric(5, 2) NULL,
                                       created_at timestamptz DEFAULT now() NOT NULL,
                                       updated_at timestamptz DEFAULT now() NOT NULL,
                                       CONSTRAINT jornada_fisica_pkey PRIMARY KEY (id),
                                       CONSTRAINT uq_jornada_fisica UNIQUE (alumno_id, fecha),
                                       CONSTRAINT jornada_fisica_academia_id_fkey FOREIGN KEY (academia_id) REFERENCES public.academia(id) ON DELETE RESTRICT,
                                       CONSTRAINT jornada_fisica_alumno_id_fkey FOREIGN KEY (alumno_id) REFERENCES public.alumno(id) ON DELETE RESTRICT,
                                       CONSTRAINT jornada_fisica_evaluador_id_fkey FOREIGN KEY (evaluador_id) REFERENCES public.usuario(id) ON DELETE RESTRICT,
                                       CONSTRAINT jornada_fisica_programa_id_fkey FOREIGN KEY (programa_id) REFERENCES public.programa(id) ON DELETE RESTRICT
);
CREATE INDEX idx_jf_programa_fecha ON public.jornada_fisica USING btree (programa_id, fecha);

-- Permissions

ALTER TABLE public.jornada_fisica OWNER TO academias_user;
GRANT ALL ON TABLE public.jornada_fisica TO academias_user;


-- public.resultado_academico definition

-- Drop table

-- DROP TABLE public.resultado_academico;

CREATE TABLE public.resultado_academico (
                                            id bigserial NOT NULL,
                                            jornada_academica_id int8 NOT NULL,
                                            materia_id int8 NOT NULL,
                                            nota numeric(5, 2) NOT NULL,
                                            observacion text NULL,
                                            created_at timestamptz DEFAULT now() NOT NULL,
                                            updated_at timestamptz DEFAULT now() NOT NULL,
                                            CONSTRAINT resultado_academico_nota_check CHECK (((nota >= (0)::numeric) AND (nota <= (20)::numeric))),
                                            CONSTRAINT resultado_academico_pkey PRIMARY KEY (id),
                                            CONSTRAINT uq_resultado_academico UNIQUE (jornada_academica_id, materia_id),
                                            CONSTRAINT resultado_academico_jornada_academica_id_fkey FOREIGN KEY (jornada_academica_id) REFERENCES public.jornada_academica(id) ON DELETE CASCADE,
                                            CONSTRAINT resultado_academico_materia_id_fkey FOREIGN KEY (materia_id) REFERENCES public.materia(id) ON DELETE RESTRICT
);
CREATE INDEX idx_ra_jornada ON public.resultado_academico USING btree (jornada_academica_id);

-- Permissions

ALTER TABLE public.resultado_academico OWNER TO academias_user;
GRANT ALL ON TABLE public.resultado_academico TO academias_user;


-- public.resultado_fisico definition

-- Drop table

-- DROP TABLE public.resultado_fisico;

CREATE TABLE public.resultado_fisico (
                                         id bigserial NOT NULL,
                                         jornada_fisica_id int8 NOT NULL,
                                         valor_num numeric(12, 2) NULL,
                                         valor_bool bool NULL,
                                         observacion text NULL,
                                         created_at timestamptz DEFAULT now() NOT NULL,
                                         updated_at timestamptz DEFAULT now() NOT NULL,
                                         programa_prueba_fisica_id int8 NULL,
                                         CONSTRAINT resultado_fisico_pkey PRIMARY KEY (id),
                                         CONSTRAINT uq_resultado_fisico UNIQUE (jornada_fisica_id, programa_prueba_fisica_id),
                                         CONSTRAINT fk_resultado_programa_prueba FOREIGN KEY (programa_prueba_fisica_id) REFERENCES public.programa_prueba_fisica(id) ON DELETE RESTRICT,
                                         CONSTRAINT resultado_fisico_jornada_fisica_id_fkey FOREIGN KEY (jornada_fisica_id) REFERENCES public.jornada_fisica(id) ON DELETE CASCADE
);
CREATE INDEX idx_rf_jornada ON public.resultado_fisico USING btree (jornada_fisica_id);

-- Permissions

ALTER TABLE public.resultado_fisico OWNER TO academias_user;
GRANT ALL ON TABLE public.resultado_fisico TO academias_user;




-- Permissions

GRANT ALL ON SCHEMA public TO academias_user;
GRANT USAGE ON SCHEMA public TO public;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT INSERT, UPDATE, TRUNCATE, TRIGGER, REFERENCES, MAINTAIN, DELETE, SELECT ON TABLES TO academias_user;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT USAGE, UPDATE, SELECT ON SEQUENCES TO academias_user;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT EXECUTE ON FUNCTIONS TO academias_user;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT USAGE ON TYPES TO academias_user;