BEGIN;

CREATE TABLE accelerators (
    accelerator_id VARCHAR(10) PRIMARY KEY,
    accelerator_name VARCHAR(50) NOT NULL
);

CREATE TABLE accelerator_connections (
    id SERIAL PRIMARY KEY,
    source_accelerator_id VARCHAR(10) NOT NULL,
    target_accelerator_id VARCHAR(10) NOT NULL,
    distance INTEGER NOT NULL,
    FOREIGN KEY (source_accelerator_id) REFERENCES accelerators (accelerator_id),
    FOREIGN KEY (target_accelerator_id) REFERENCES accelerators (accelerator_id)
);

END;