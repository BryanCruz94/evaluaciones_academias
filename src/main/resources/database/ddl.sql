CREATE TYPE rol_usuario AS ENUM ('SUPER_ADMIN', 'EVALUADOR');

CREATE TYPE genero AS ENUM ('M', 'F');

CREATE TYPE estado_alumno AS ENUM ('ACTIVO', 'BAJA', 'GRADUADO', 'REINGRESO');

CREATE TYPE tipo_valor_prueba AS ENUM ('TIEMPO', 'REPETICIONES', 'BOOLEANO', 'NUMERICO');

CREATE TYPE operador_objetivo AS ENUM ('>=', '<=', '=');


CREATE TABLE academia (
                          id            BIGSERIAL PRIMARY KEY,
                          nombre        VARCHAR(150) NOT NULL,
                          codigo        VARCHAR(50) UNIQUE,
                          activo        BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);



CREATE TABLE usuario (
                         id            BIGSERIAL PRIMARY KEY,
                         auth0_sub     VARCHAR(255) NOT NULL UNIQUE,
                         email         VARCHAR(254) NOT NULL,
                         nombres       VARCHAR(150) NOT NULL,
                         rol           rol_usuario NOT NULL,
                         academia_id   BIGINT NULL REFERENCES academia(id) ON DELETE RESTRICT,
                         activo        BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                         CONSTRAINT chk_super_admin_academia_null
                             CHECK (
                                 (rol = 'SUPER_ADMIN' AND academia_id IS NULL)
                                     OR
                                 (rol = 'EVALUADOR' AND academia_id IS NOT NULL)
                                 )
);

CREATE INDEX idx_usuario_academia ON usuario(academia_id);


CREATE TABLE programa (
                          id            BIGSERIAL PRIMARY KEY,
                          academia_id   BIGINT NOT NULL REFERENCES academia(id) ON DELETE RESTRICT,
                          nombre        VARCHAR(150) NOT NULL,
                          descripcion   TEXT,
                          genero        genero NULL, -- ✅ movido aquí
                          activo        BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                          CONSTRAINT uq_programa_nombre UNIQUE (academia_id, nombre)
);

CREATE INDEX idx_programa_academia ON programa(academia_id);

CREATE INDEX idx_programa_academia ON programa(academia_id);


CREATE TABLE alumno (
                        id                  BIGSERIAL PRIMARY KEY,
                        academia_id         BIGINT NOT NULL REFERENCES academia(id) ON DELETE RESTRICT,
                        programa_actual_id  BIGINT NULL REFERENCES programa(id) ON DELETE SET NULL,

                        nombres             VARCHAR(150) NOT NULL,
                        apellidos           VARCHAR(150) NOT NULL,
                        email               VARCHAR(254),
                        fecha_nacimiento    DATE,
                        cedula              VARCHAR(10) UNIQUE,

                        genero              genero NOT NULL,
                        estatura_cm         double,
                        peso_kg             double,
                        imc                 NUMERIC(5,2),

                        estado              estado_alumno NOT NULL DEFAULT 'ACTIVO',
                        fecha_ingreso       DATE,
                        fecha_salida        DATE,

                        foto_url            TEXT,

                        created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                        CONSTRAINT uq_alumno_email UNIQUE (academia_id, email)
);

CREATE INDEX idx_alumno_academia ON alumno(academia_id);


CREATE TABLE prueba_fisica (
                               id            BIGSERIAL PRIMARY KEY,
                               academia_id   BIGINT NOT NULL REFERENCES academia(id) ON DELETE RESTRICT,
                               nombre        VARCHAR(150) NOT NULL,
                               tipo_valor    tipo_valor_prueba NOT NULL,
                               unidad        VARCHAR(30),
                               activo        BOOLEAN NOT NULL DEFAULT TRUE,
                               created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                               CONSTRAINT uq_prueba_fisica_nombre UNIQUE (academia_id, nombre)
);

CREATE INDEX idx_prueba_fisica_academia ON prueba_fisica(academia_id);


CREATE TABLE programa_prueba_fisica (
                                        id                  BIGSERIAL PRIMARY KEY,
                                        programa_id         BIGINT NOT NULL REFERENCES programa(id) ON DELETE RESTRICT,
                                        prueba_fisica_id    BIGINT NOT NULL REFERENCES prueba_fisica(id) ON DELETE RESTRICT,
                                        orden               INT NOT NULL CHECK (orden > 0),
                                        objetivo_valor      NUMERIC(12,2),
                                        operador            operador_objetivo,
                                        etiqueta            VARCHAR(80),
                                        activo              BOOLEAN NOT NULL DEFAULT TRUE,
                                        created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                        updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    -- ✅ UNIQUE más coherente para que no repitas la misma prueba en el mismo programa
                                        CONSTRAINT uq_programa_prueba UNIQUE (programa_id, prueba_fisica_id),

                                        CONSTRAINT chk_objetivo_operador
                                            CHECK (
                                                (objetivo_valor IS NULL AND operador IS NULL)
                                                    OR
                                                (objetivo_valor IS NOT NULL AND operador IS NOT NULL)
                                                )
);

CREATE INDEX idx_ppf_programa ON programa_prueba_fisica(programa_id);


CREATE TABLE jornada_fisica (
                                id            BIGSERIAL PRIMARY KEY,
                                alumno_id     BIGINT NOT NULL REFERENCES alumno(id) ON DELETE RESTRICT,
                                academia_id   BIGINT NOT NULL REFERENCES academia(id) ON DELETE RESTRICT,
                                programa_id   BIGINT NOT NULL REFERENCES programa(id) ON DELETE RESTRICT,
                                fecha         DATE NOT NULL,
                                evaluador_id  BIGINT NOT NULL REFERENCES usuario(id) ON DELETE RESTRICT,
                                observacion   TEXT,
                                peso_kg       NUMERIC(4,2),
                                imc           NUMERIC(5,2),
                                created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                CONSTRAINT uq_jornada_fisica UNIQUE (alumno_id, fecha)
);

CREATE INDEX idx_jf_programa_fecha ON jornada_fisica(programa_id, fecha);


CREATE TABLE resultado_fisico (
                                  id                    BIGSERIAL PRIMARY KEY,
                                  jornada_fisica_id     BIGINT NOT NULL REFERENCES jornada_fisica(id) ON DELETE CASCADE,
                                  prueba_fisica_id      BIGINT NOT NULL REFERENCES prueba_fisica(id) ON DELETE RESTRICT,
                                  valor_num             NUMERIC(12,2),
                                  valor_bool            BOOLEAN,
                                  observacion           TEXT,
                                  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                  updated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                  CONSTRAINT uq_resultado_fisico
                                      UNIQUE (jornada_fisica_id, prueba_fisica_id)
);

CREATE INDEX idx_rf_jornada ON resultado_fisico(jornada_fisica_id);


CREATE TABLE materia (
                         id            BIGSERIAL PRIMARY KEY,
                         academia_id   BIGINT NOT NULL REFERENCES academia(id) ON DELETE RESTRICT,
                         nombre        VARCHAR(150) NOT NULL,
                         activo        BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                         CONSTRAINT uq_materia_nombre UNIQUE (academia_id, nombre)
);


CREATE TABLE programa_materia (
                                  id            BIGSERIAL PRIMARY KEY,
                                  programa_id   BIGINT NOT NULL REFERENCES programa(id) ON DELETE RESTRICT,
                                  materia_id    BIGINT NOT NULL REFERENCES materia(id) ON DELETE RESTRICT,
                                  orden         INT NOT NULL CHECK (orden > 0),
                                  nota_objetivo NUMERIC(5,2) CHECK (nota_objetivo >= 0 AND nota_objetivo <= 20),
                                  activo        BOOLEAN NOT NULL DEFAULT TRUE,
                                  created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                  updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                  CONSTRAINT uq_programa_materia
                                      UNIQUE (programa_id, materia_id)
);


CREATE TABLE jornada_academica (
                                   id            BIGSERIAL PRIMARY KEY,
                                   alumno_id     BIGINT NOT NULL REFERENCES alumno(id) ON DELETE RESTRICT,
                                   academia_id   BIGINT NOT NULL REFERENCES academia(id) ON DELETE RESTRICT,
                                   programa_id   BIGINT NOT NULL REFERENCES programa(id) ON DELETE RESTRICT,
                                   fecha         DATE NOT NULL,
                                   evaluador_id  BIGINT NOT NULL REFERENCES usuario(id) ON DELETE RESTRICT,
                                   observacion   TEXT,
                                   created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                   updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                   CONSTRAINT uq_jornada_academica UNIQUE (alumno_id, fecha)
);

CREATE INDEX idx_ja_programa_fecha ON jornada_academica(programa_id, fecha);


CREATE TABLE resultado_academico (
                                     id                      BIGSERIAL PRIMARY KEY,
                                     jornada_academica_id    BIGINT NOT NULL REFERENCES jornada_academica(id) ON DELETE CASCADE,
                                     materia_id              BIGINT NOT NULL REFERENCES materia(id) ON DELETE RESTRICT,
                                     nota                    NUMERIC(5,2) NOT NULL CHECK (nota >= 0 AND nota <= 20),
                                     observacion             TEXT,
                                     created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                     updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                     CONSTRAINT uq_resultado_academico
                                         UNIQUE (jornada_academica_id, materia_id)
);

CREATE INDEX idx_ra_jornada ON resultado_academico(jornada_academica_id);


-- 1) Quitar unique de cedula (ajusta el nombre del constraint/index)
ALTER TABLE alumno DROP CONSTRAINT IF EXISTS alumno_cedula_key;

-- 2) Crear unique compuesto por academia + cedula
ALTER TABLE alumno
    ADD CONSTRAINT uq_alumno_academia_cedula UNIQUE (academia_id, cedula);

-- Recomendado para búsquedas
CREATE INDEX IF NOT EXISTS idx_alumno_academia_cedula ON alumno(academia_id, cedula);
