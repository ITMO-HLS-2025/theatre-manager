CREATE TABLE theatre (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE hall (
    id BIGSERIAL PRIMARY KEY,
    number INT NOT NULL,
    theatre_id BIGINT NOT NULL,
    CONSTRAINT fk_hall_theatre
        FOREIGN KEY (theatre_id) REFERENCES theatre(id)
);

CREATE TABLE seat (
    id BIGSERIAL PRIMARY KEY,
    row_number INT NOT NULL,
    seat_number INT NOT NULL,
    hall_id BIGINT NOT NULL,
    CONSTRAINT fk_seat_hall
        FOREIGN KEY (hall_id) REFERENCES hall(id)
);

CREATE TABLE seat_price (
    seat_id BIGINT NOT NULL,
    show_id BIGINT NOT NULL,
    price INT NOT NULL,
    PRIMARY KEY (seat_id, show_id),
    CONSTRAINT fk_seat_price_seat
        FOREIGN KEY (seat_id) REFERENCES seat(id)
);
