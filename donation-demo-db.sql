-- Database: donation_demo

-- DROP DATABASE IF EXISTS donation_demo;

CREATE DATABASE donation_demo
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- DROP SCHEMA IF EXISTS public ;

CREATE SCHEMA IF NOT EXISTS public
    AUTHORIZATION pg_database_owner;

COMMENT ON SCHEMA public
    IS 'standard public schema';

GRANT USAGE ON SCHEMA public TO PUBLIC;

GRANT ALL ON SCHEMA public TO pg_database_owner;

-- DROP TABLE IF EXISTS public.donation_type;

CREATE TABLE IF NOT EXISTS public.donation_type
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Donation_Types_pkey" PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.donation_type
    OWNER to postgres;
	
-- Table: public.donor

-- DROP TABLE IF EXISTS public.donor;

CREATE TABLE IF NOT EXISTS public.donor
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default",
    CONSTRAINT "Donors_pkey" PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.donor
    OWNER to postgres;
	
-- Table: public.donation

-- DROP TABLE IF EXISTS public.donation;

CREATE TABLE IF NOT EXISTS public.donation
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    donor_id integer NOT NULL,
    type_id integer NOT NULL DEFAULT 0,
    quantity character varying COLLATE pg_catalog."default" NOT NULL,
    date date NOT NULL,
    CONSTRAINT "Donations_pkey" PRIMARY KEY (id),
    CONSTRAINT "Donors_fkey" FOREIGN KEY (donor_id)
        REFERENCES public.donor (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT "Type_fkey" FOREIGN KEY (type_id)
        REFERENCES public.donation_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.donation
    OWNER to postgres;
