DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS meal_history;
DROP TABLE IF EXISTS foods;
DROP TABLE IF EXISTS users;

CREATE EXTENSION IF NOT EXISTS pgroonga;

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username character varying(255) NOT NULL UNIQUE,
    email character varying(255) NOT NULL UNIQUE,
    password character varying(255) NOT NULL,
    goal_protein integer DEFAULT 2000,
    goal_carbs integer DEFAULT 75,
    goal_fat integer DEFAULT 56,
    goal_calories integer DEFAULT 300,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS foods
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) NOT NULL UNIQUE,
    protein double precision NOT NULL,
    fat double precision NOT NULL,
    carbs double precision NOT NULL,
    calories double precision NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS pgroonga_content_index
ON foods USING pgroonga (name);

CREATE TABLE IF NOT EXISTS meal_history
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id bigint NOT NULL REFERENCES public.users(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    food_id bigint NOT NULL REFERENCES public.foods(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    quantity integer NOT NULL,
    meal_number integer NOT NULL,
    delete_flg boolean DEFAULT false,
    consumed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS meal_history_user_date_idx
ON meal_history (user_id, consumed_at);

CREATE TABLE IF NOT EXISTS reports
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id bigint NOT NULL REFERENCES public.users(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    report_date date NOT NULL,
    total_calories double precision NOT NULL,
    total_protein double precision NOT NULL,
    total_carbs double precision NOT NULL,
    total_fat double precision NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

