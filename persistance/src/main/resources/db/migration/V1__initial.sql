CREATE TABLE IF NOT EXISTS categories (
                                    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    name VARCHAR(255));
CREATE TABLE IF NOT EXISTS mcc_codes (
                                         mcc_code VARCHAR(10) NOT NULL,
                                         category_id BIGINT NOT NULL REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS subcategories (
                                       subcategory_id BIGINT NOT NULL REFERENCES categories(id),
                                       parent_category_id BIGINT NOT NULL REFERENCES categories(id),
                                       primary key(subcategory_id, parent_category_id)
);

CREATE TABLE IF NOT EXISTS transactions (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
                                      name VARCHAR(255),
                                      value FLOAT NOT NULL,
                                      date DATE,
                                      mcc_code VARCHAR(10)
);