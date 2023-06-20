CREATE TABLE IF NOT EXISTS Kingdom (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       food_id BIGINT NOT NULL,
                                       gold_id BIGINT NOT NULL,
                                       location_x INT NOT NULL,
                                       location_y INT NOT NULL,
                                       player_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS players (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       player_name VARCHAR(50) NOT NULL UNIQUE,
                                       password VARCHAR(255) NOT NULL,
                                       email VARCHAR(50) NOT NULL,
                                       verified BOOLEAN,
                                       role_type VARCHAR(20),
                                       kingdom_id BIGINT
);

CREATE TABLE IF NOT EXISTS troop (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     type VARCHAR(255),
                                     level INT NOT NULL,
                                     hp INT NOT NULL,
                                     attack INT NOT NULL,
                                     defense INT NOT NULL,
                                     started_at TIMESTAMP,
                                     finished_at TIMESTAMP,
                                     kingdom_id BIGINT,
                                     valid BOOLEAN
);

CREATE TABLE IF NOT EXISTS buildings (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         hp INT NOT NULL,
                                         level INT NOT NULL,
                                         started_at DATETIME NOT NULL,
                                         finished_at DATETIME NOT NULL,
                                         building_type VARCHAR(255) NOT NULL,
                                         kingdom_id BIGINT
);

CREATE TABLE IF NOT EXISTS Resource (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        quantity INT NOT NULL,
                                        valid BOOLEAN NOT NULL,
                                        kingdom_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS Gold (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    quantity INT NOT NULL,
                                    valid BOOLEAN NOT NULL,
                                    kingdom_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS Food (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    quantity INT NOT NULL,
                                    valid BOOLEAN NOT NULL,
                                    kingdom_id BIGINT NOT NULL
);

CREATE TABLE confirmation_tokens (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     token VARCHAR(255) NOT NULL UNIQUE,
                                     expires_at TIMESTAMP NOT NULL,
                                     player_id BIGINT NOT NULL
);

ALTER TABLE Kingdom ADD CONSTRAINT FK_Kingdom_Food FOREIGN KEY (food_id) REFERENCES Food(id);

ALTER TABLE Kingdom ADD CONSTRAINT FK_Kingdom_Gold FOREIGN KEY (gold_id) REFERENCES Gold(id);

ALTER TABLE Kingdom ADD CONSTRAINT FK_Kingdom_Player FOREIGN KEY (player_id) REFERENCES players(id);

ALTER TABLE players ADD CONSTRAINT FK_players_Kingdom FOREIGN KEY (kingdom_id) REFERENCES Kingdom(id) ON DELETE CASCADE;

ALTER TABLE troop ADD CONSTRAINT FK_troop_Kingdom FOREIGN KEY (kingdom_id) REFERENCES Kingdom(id) ON DELETE CASCADE;

ALTER TABLE buildings ADD CONSTRAINT FK_buildings_Kingdom FOREIGN KEY (kingdom_id) REFERENCES Kingdom(id) ON DELETE CASCADE;

ALTER TABLE Resource ADD CONSTRAINT FK_Resource_Kingdom FOREIGN KEY (kingdom_id) REFERENCES Kingdom(id) ON DELETE CASCADE;

ALTER TABLE Gold ADD CONSTRAINT FK_Gold_Kingdom FOREIGN KEY (kingdom_id) REFERENCES Kingdom(id) ON DELETE CASCADE;

ALTER TABLE Food ADD CONSTRAINT FK_Food_Kingdom FOREIGN KEY (kingdom_id) REFERENCES Kingdom(id) ON DELETE CASCADE;

ALTER TABLE confirmation_tokens ADD CONSTRAINT fk_confirmation_tokens_player FOREIGN KEY (player_id) REFERENCES players(id);
