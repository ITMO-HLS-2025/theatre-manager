create table theatre (
    id bigserial primary key,
    name varchar(255) not null,
    city varchar(255) not null,
    address varchar(255) not null
);

create table hall (
    id bigserial primary key,
    number int not null,
    theatre_id bigint not null references theatre(id)
);

create table seat (
    id bigserial primary key,
    row_number int not null,
    seat_number int not null,
    hall_id bigint not null references hall(id)
);

create table seat_price (
    seat_id bigint not null references seat(id),
    show_id bigint not null,
    price int not null,
    primary key (seat_id, show_id)
);
