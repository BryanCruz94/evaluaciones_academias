-- Reparacion para bases locales antiguas que no tienen alumno.genero
-- o tienen programas creados antes de que genero fuera obligatorio.

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'genero') THEN
        CREATE TYPE public.genero AS ENUM ('M', 'F');
    END IF;
END $$;

ALTER TABLE public.alumno
    ADD COLUMN IF NOT EXISTS genero public.genero;

UPDATE public.alumno
SET genero = 'M'
WHERE genero IS NULL;

ALTER TABLE public.alumno
    ALTER COLUMN genero SET NOT NULL;

UPDATE public.programa
SET genero = 'M'
WHERE genero IS NULL;
