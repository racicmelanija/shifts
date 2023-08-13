CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS shifts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    date DATE NOT NULL,
    start TIMESTAMP NOT NULL,
    finish TIMESTAMP NOT NULL,
    cost NUMERIC(13, 4)
);

CREATE TABLE IF NOT EXISTS breaks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shift_id UUID NOT NULL,
    start TIMESTAMP NOT NULL,
    finish TIMESTAMP NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (shift_id)
        REFERENCES shifts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS allowances (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shift_id UUID NOT NULL,
    value FLOAT NOT NULL,
    cost NUMERIC(13, 4) NOT NULL,
    FOREIGN KEY (shift_id)
        REFERENCES shifts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS award_interpretations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shift_id UUID NOT NULL,
    date DATE NOT NULL,
    units FLOAT NOT NULL,
    cost NUMERIC(13, 4) NOT NULL,
    FOREIGN KEY (shift_id)
        REFERENCES shifts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS kpis (
    kpi_id SERIAL PRIMARY KEY,
    kpi_name VARCHAR(255) NOT NULL,
    kpi_date DATE NOT NULL,
    kpi_value NUMERIC(8, 2) NOT NULL,
    UNIQUE (kpi_name, kpi_date)
)