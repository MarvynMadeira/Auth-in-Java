CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);

//test:
INSERT INTO users (name, email, password, active)
VALUES ('Admin User', 'admin@email.com', '$2a$10$3g5v1.c.28xfszQ.E1.KL.21IS.H6a.KjEXT24.G33s5.pp.3.LwS', true)
ON CONFLICT (email) DO NOTHING;

