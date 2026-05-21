package com.nairbdev.academiasbackend.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDatabaseInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DevDatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        jdbcTemplate.execute("""
                DO $$
                BEGIN
                    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'genero') THEN
                        CREATE TYPE public.genero AS ENUM ('M', 'F');
                    END IF;
                END $$;
                """);

        jdbcTemplate.execute("ALTER TABLE public.alumno ADD COLUMN IF NOT EXISTS genero public.genero");
        jdbcTemplate.execute("UPDATE public.alumno SET genero = 'M' WHERE genero IS NULL");
        jdbcTemplate.execute("ALTER TABLE public.alumno ALTER COLUMN genero SET NOT NULL");
        jdbcTemplate.execute("UPDATE public.programa SET genero = 'M' WHERE genero IS NULL");
    }
}
