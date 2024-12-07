DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS meal_history;
DROP TABLE IF EXISTS foods;
DROP TABLE IF EXISTS users;

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

-- CREATE TABLE IF NOT EXISTS users
-- (
--     id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
--     username character varying(255) NOT NULL,
--     email character varying(255) NOT NULL,
--     password character varying(255) NOT NULL,
--     created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
--     updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT users_pkey PRIMARY KEY (id),
--     CONSTRAINT users_email_key UNIQUE (email),
--     CONSTRAINT users_username_key UNIQUE (username)
-- );



-- CREATE TABLE IF NOT EXISTS foods
-- (
--     id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
--     name character varying(255) NOT NULL,
--     protein double precision NOT NULL,
--     fat double precision NOT NULL,
--     carbs double precision NOT NULL,
--     calories double precision NOT NULL,
--     created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
--     updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT foods_pkey PRIMARY KEY (id),
--     CONSTRAINT foods_name_key UNIQUE (name)
-- );

-- CREATE INDEX IF NOT EXISTS pgroonga_content_index
-- ON foods USING pgroonga (name);



-- CREATE TABLE IF NOT EXISTS meal_history
-- (
--     id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
--     user_id bigint,
--     food_id bigint,
--     quantity integer NOT NULL,
--     consumed_at date DEFAULT CURRENT_TIMESTAMP,
--     created_at timestamp(6) without time zone,
--     updated_at timestamp(6) without time zone,
--     CONSTRAINT meal_history_pkey PRIMARY KEY (id),
--     CONSTRAINT meal_history_food_id_fkey FOREIGN KEY (food_id)
--         REFERENCES public.foods (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE NO ACTION,
--     CONSTRAINT meal_history_user_id_fkey FOREIGN KEY (user_id)
--         REFERENCES public.users (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE
-- );



-- CREATE TABLE IF NOT EXISTS reports
-- (
--     id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
--     user_id bigint,
--     report_date date NOT NULL,
--     total_calories double precision NOT NULL,
--     total_protein double precision NOT NULL,
--     total_carbs double precision NOT NULL,
--     total_fat double precision NOT NULL,
--     created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT reports_pkey PRIMARY KEY (id),
--     CONSTRAINT reports_user_id_fkey FOREIGN KEY (user_id)
--         REFERENCES public.users (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE
-- );