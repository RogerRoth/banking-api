ALTER TABLE account
ADD COLUMN email VARCHAR(255) NOT NULL,
ADD COLUMN password VARCHAR(255) NOT NULL,
ADD CONSTRAINT unique_email UNIQUE (email);
