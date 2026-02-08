SELECT setval(
    pg_get_serial_sequence('theatre', 'id'),
    COALESCE((SELECT MAX(id) FROM theatre), 1),
    (SELECT MAX(id) IS NOT NULL FROM theatre)
);

SELECT setval(
    pg_get_serial_sequence('hall', 'id'),
    COALESCE((SELECT MAX(id) FROM hall), 1),
    (SELECT MAX(id) IS NOT NULL FROM hall)
);

SELECT setval(
    pg_get_serial_sequence('seat', 'id'),
    COALESCE((SELECT MAX(id) FROM seat), 1),
    (SELECT MAX(id) IS NOT NULL FROM seat)
);
