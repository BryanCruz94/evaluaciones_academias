-- ===============================
-- 1️⃣ ACADEMIA
-- ===============================
INSERT INTO academia (nombre, codigo)
VALUES ('Academia Prueba', 'ACAD-001');


-- ===============================
-- 2️⃣ USUARIOS
-- ===============================

-- SUPER ADMIN (sin academia_id)
INSERT INTO usuario (auth0_sub, email, nombres, rol)
VALUES ('auth0|superadmin1', 'superadmin@test.com', 'Super Admin', 'SUPER_ADMIN');

-- EVALUADOR (pertenece a academia 1)
INSERT INTO usuario (auth0_sub, email, nombres, rol, academia_id)
VALUES ('auth0|evaluador1', 'evaluador@test.com', 'Evaluador Uno', 'EVALUADOR', 1);


-- ===============================
-- 3️⃣ PROGRAMA
-- ===============================
INSERT INTO programa (academia_id, nombre, descripcion)
VALUES (1, 'Escuela Oficial Militar', 'Programa de preparación para oficiales');


-- ===============================
-- 4️⃣ ALUMNO
-- ===============================

-- Estatura 1.75m, peso 70kg → IMC = 22.86 aprox
INSERT INTO alumno (
    academia_id,
    programa_actual_id,
    nombres,
    apellidos,
    email,
    fecha_nacimiento,
    genero,
    estatura_cm,
    peso_kg,
    imc,
    fecha_ingreso,
    foto_url
)
VALUES (
           1,
           1,
           'Juan',
           'Pérez',
           'juan@test.com',
           '2005-05-10',
           'M',
           1.75,
           70,
           22.86,
           CURRENT_DATE,
           'https://mi-storage.com/fotos/juan.jpg'
       );


-- ===============================
-- 5️⃣ PRUEBAS FÍSICAS
-- ===============================

INSERT INTO prueba_fisica (academia_id, nombre, tipo_valor, unidad)
VALUES
    (1, '3200 metros', 'TIEMPO', 'segundos'),
    (1, 'Flexiones de codo', 'REPETICIONES', 'reps');


-- ===============================
-- 6️⃣ CATÁLOGO PROGRAMA - PRUEBAS
-- ===============================

-- 3200 metros: objetivo <= 750 segundos (12:30)
INSERT INTO programa_prueba_fisica (
    programa_id,
    prueba_fisica_id,
    orden,
    objetivo_valor,
    operador,
    etiqueta,
    genero
)
VALUES
    (1, 1, 1, 750, '<=', 'Tiempo máximo 12:30', 'M');

-- Flexiones: objetivo >= 50
INSERT INTO programa_prueba_fisica (
    programa_id,
    prueba_fisica_id,
    orden,
    objetivo_valor,
    operador,
    etiqueta,
    genero
)
VALUES
    (1, 2, 2, 50, '>=', 'En 1:30', 'M');


-- ===============================
-- 7️⃣ JORNADA FÍSICA
-- ===============================

INSERT INTO jornada_fisica (
    alumno_id,
    academia_id,
    programa_id,
    fecha,
    evaluador_id,
    observacion,
    peso_kg,
    imc
)
VALUES (
           1,
           1,
           1,
           CURRENT_DATE,
           2,
           'Primera evaluación física',
           70,
           22.86
       );


-- ===============================
-- 8️⃣ RESULTADOS FÍSICOS
-- ===============================

-- 3200 metros → hizo 760 segundos (12:40)
INSERT INTO resultado_fisico (
    jornada_fisica_id,
    prueba_fisica_id,
    valor_num,
    observacion
)
VALUES
    (1, 1, 760, 'Ligeramente por encima del objetivo');

-- Flexiones → hizo 48
INSERT INTO resultado_fisico (
    jornada_fisica_id,
    prueba_fisica_id,
    valor_num
)
VALUES
    (1, 2, 48);


-- ===============================
-- 9️⃣ MATERIA
-- ===============================

INSERT INTO materia (academia_id, nombre)
VALUES (1, 'Matemáticas');


-- ===============================
-- 🔟 CATÁLOGO PROGRAMA - MATERIA
-- ===============================

INSERT INTO programa_materia (
    programa_id,
    materia_id,
    orden,
    nota_objetivo
)
VALUES
    (1, 1, 1, 14);


-- ===============================
-- 1️⃣1️⃣ JORNADA ACADÉMICA
-- ===============================

INSERT INTO jornada_academica (
    alumno_id,
    academia_id,
    programa_id,
    fecha,
    evaluador_id,
    observacion
)
VALUES (
           1,
           1,
           1,
           CURRENT_DATE,
           2,
           'Evaluación mensual académica'
       );


-- ===============================
-- 1️⃣2️⃣ RESULTADO ACADÉMICO
-- ===============================

INSERT INTO resultado_academico (
    jornada_academica_id,
    materia_id,
    nota,
    observacion
)
VALUES
    (1, 1, 13.50, 'Debe reforzar álgebra');
