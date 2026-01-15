INSERT INTO theatre (id, name, city, address)
VALUES (1, 'Мариинский театр', 'Санкт-Петербург', 'Театральная пл., 1'),
       (2, 'Большой театр', 'Москва', 'Театральная пл., 2');

INSERT INTO hall (id, number, theatre_id)
VALUES (1, 1, 1),
       (2, 2, 1),
       (3, 1, 2);

INSERT INTO seat (id, row_number, seat_number, hall_id)
VALUES (1, 1, 1, 1),
       (2, 1, 2, 1),
       (3, 2, 1, 1);

INSERT INTO seat_price (seat_id, show_id, price)
VALUES (1, 1, 1000),
       (2, 1, 1200),
       (3, 1, 900);
