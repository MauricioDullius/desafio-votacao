CREATE TABLE agenda (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        description VARCHAR(500) NOT NULL,
                        start DATETIME,
                        end DATETIME,
                        state INT NOT NULL,
                        assembly_id BIGINT NOT NULL,
                        CONSTRAINT fk_agenda_assembly FOREIGN KEY (assembly_id)
                            REFERENCES assembly(id)
);