-- 1. Security Table: Users (Affiliates, Analysts, Admins)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Domain Table: Affiliates (Linked to User)
CREATE TABLE affiliates (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    address VARCHAR(255),
    salary DECIMAL(15, 2) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_affiliate_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 3. Domain Table: Credit Applications
CREATE TABLE credit_applications (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    term_months INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_application_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliates(id)
);

-- 4. Domain Table: Risk Evaluations
CREATE TABLE risk_evaluations (
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    score INTEGER NOT NULL,
    failure_reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_evaluation_application FOREIGN KEY (application_id) REFERENCES credit_applications(id)
);

-- Indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_applications_status ON credit_applications(status);