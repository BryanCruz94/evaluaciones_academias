CREATE TABLE IF NOT EXISTS public.consulta_alumno_auditoria (
    id bigserial NOT NULL,
    fecha_consulta timestamptz NOT NULL,
    nombre_consultor varchar(200) NOT NULL,
    cedula_consultada varchar(10) NOT NULL,
    alumno_id int8 NULL,
    nombre_alumno varchar(300) NULL,
    resultado_encontrado bool NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    updated_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT consulta_alumno_auditoria_pkey PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_consulta_alumno_cedula
    ON public.consulta_alumno_auditoria USING btree (cedula_consultada);

CREATE INDEX IF NOT EXISTS idx_consulta_alumno_fecha
    ON public.consulta_alumno_auditoria USING btree (fecha_consulta);

ALTER TABLE public.consulta_alumno_auditoria OWNER TO academias_user;
GRANT ALL ON TABLE public.consulta_alumno_auditoria TO academias_user;
