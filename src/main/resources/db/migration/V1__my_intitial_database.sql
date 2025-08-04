CREATE TYPE gender AS ENUM('MALE', 'FEMALE');
CREATE TYPE marriage_status AS ENUM('SINGLE', 'MARRIED', 'DIVORCE');

CREATE TABLE users(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(20) UNIQUE NOT NULL,
    real_name VARCHAR(20) NOT NULL,
    email VARCHAR(300) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL ,
    user_gender gender,
--     CONSTRAINT valid_gender_check CHECK ( user_gender IN ('MALE', 'FEMALE') ),
    occupation VARCHAR(20) NOT NULL ,
    nationality VARCHAR(20) NOT NULL ,
    description TEXT ,
    height_in_cm DECIMAL(5,2) NOT NULL ,
    marital_status marriage_status NOT NULL DEFAULT 'SINGLE',
--     CONSTRAINT valid_marital_status_check CHECK ( marital_status IN ('SINGLE', 'MARRIED', 'DIVORCE') ),
    family_size SMALLINT NOT NULL ,
    siblings_count SMALLINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TYPE acc_type AS ENUM('SAVINGS', 'CURRENT');
CREATE TYPE acc_status AS ENUM('ACTIVE', 'FROZEN', 'CLOSED');
CREATE TABLE accounts(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_number TEXT UNIQUE NOT NULL ,
    account_type acc_type NOT NULL DEFAULT 'SAVINGS' ,
    account_status acc_status NOT NULL DEFAULT 'ACTIVE' ,
    account_balance NUMERIC(15,4) NOT NULL DEFAULT 0 ,
    user_id UUID NOT NULL REFERENCES users(id) ,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TYPE trans_status AS ENUM('PENDING', 'SUCCESS', 'REVERSED', 'FAILED');
CREATE TABLE transactions(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference TEXT UNIQUE NOT NULL,
    amount NUMERIC(15,2) NOT NULL ,
    transaction_status trans_status NOT NULL DEFAULT 'PENDING' ,
    source_account_id UUID REFERENCES  accounts(id) NOT NULL ,
    destination_account_id UUID REFERENCES accounts(id) NOT NULL ,
    note TEXT,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE TYPE draw_result_status AS ENUM('PENDING', 'COMPLETED', 'FAILED');
CREATE TABLE draw_results(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    winner_acc_number TEXT REFERENCES accounts(account_number),
    amount_transferred NUMERIC(15,2) NOT NULL ,
    draw_reference TEXT UNIQUE NOT NULL ,
    status draw_result_status DEFAULT 'PENDING' ,
    triggered_by UUID REFERENCES users(id) ,
    created_at TIMESTAMP DEFAULT now()
);