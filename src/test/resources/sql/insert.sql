insert into theatre (id, name, city, address) values (1, 'Mariinsky Theatre', 'Moscow', 'Main Street 1');
insert into theatre (id, name, city, address) values (2, 'Bolshoi Theatre', 'Moscow', 'Main Street 2');
insert into theatre (id, name, city, address) values (3, 'Alexandrinsky Theatre', 'Saint Petersburg', 'Main Street 3');

insert into hall (id, number, theatre_id) values (1, 1, 1);
insert into hall (id, number, theatre_id) values (2, 2, 1);

insert into seat (id, row_number, seat_number, hall_id) values (1, 1, 1, 1);
insert into seat (id, row_number, seat_number, hall_id) values (2, 1, 2, 1);
insert into seat_price (seat_id, show_id, price) values (1, 100, 1500);
insert into seat_price (seat_id, show_id, price) values (2, 100, 1800);

select setval('theatre_id_seq', 3, true);
select setval('hall_id_seq', 2, true);
select setval('seat_id_seq', 2, true);
