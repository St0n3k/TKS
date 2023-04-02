INSERT INTO clients (id, version, type, username, first_name, last_name, personal_id, city, street, house_number) VALUES ('bdbe2fcf-6203-47d6-8908-ca65b9689396', 0, 'Client', 'client', 'Mariusz', 'Pudzianowski', '54352353', 'Warszawa', 'Stalowa', 16);
INSERT INTO clients (id, version, type, username, first_name, last_name, personal_id, city, street, house_number) VALUES ('a524d75e-927a-4a10-8c46-6321fff6979e', 0, 'Client', 'jakub2', 'Jakub', 'Bukaj', '3584873', 'Krakow', 'Smutna', 13);
INSERT INTO clients (id, version, type, username, first_name, last_name, personal_id, city, street, house_number) VALUES ('dbc028ea-a233-4280-b953-564a69da1810', 0, 'Client', 'jakub3', 'Kuba', 'Bokaj', '3584173', 'Krakow', 'Smutna', 13);


INSERT INTO room (id, version, price, room_number, size, type) VALUES ('9acac245-25b3-492d-a742-4c69bfcb90cf', 0, 250.00, 643, 6, 'Room');
INSERT INTO room (id, version, price, room_number, size, type, balconyArea) VALUES ('dba673f8-0526-4cea-941e-3c8ddd5e4f92', 0, 707.19, 836, 1, 'Apartment', 54);
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('9f3d02be-f106-4081-a732-21b69438773e', 0, 170.08, 644, 2, 'Room');
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('7f25a03c-b1f1-46cb-89d4-b1f813ca6dfd', 0, 821.95, 504, 9, 'Room');
INSERT INTO room (id, version, price, room_number, size, type, balconyArea) VALUES ('b0f9495e-13a7-4da1-989c-c403ece4e22d', 0, 92.27, 957, 6, 'Apartment', 22);
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('7009adc1-94c7-4b15-b068-9cf00d325adf', 0, 1471.35, 958, 5, 'Room');
INSERT INTO room (id, version, price, room_number, size, type, balconyArea) VALUES ('36f81e94-3832-4a66-a15d-ff05f01c0e98', 0, 736.75, 498, 5, 'Apartment', 35);
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('0533a035-22de-45e8-b922-5fa8b103b1a2', 0, 956.51, 792, 4, 'Room');
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('b9573aa2-42fa-43cb-baa1-42d06e1bdc8d', 0, 1396.79, 392, 9, 'Room');
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9', 0, 541.56, 244, 4, 'Room');
INSERT INTO room (id, version, price, room_number, size, type, balconyArea) VALUES ('561d7a3a-6447-417e-a2ad-2053ec777a8b', 0, 353.4, 598, 9, 'Apartment', 54.5);
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('b72205fb-27bc-4015-b3ce-1773706d175e', 0, 638.8, 380, 4, 'Room');
INSERT INTO room (id, version, price, room_number, size, type) VALUES ('8378b753-6d05-454b-8447-efb125846fc7', 0, 903.54, 793, 5, 'Room');
INSERT INTO room (id, version, price, room_number, size, type, balconyArea) VALUES ('66208864-7b61-4e6e-8573-53863bd93b35', 0, 352.18, 372, 8, 'Apartment', 12.1);
INSERT INTO room (id, version, price, room_number, size, type, balconyArea) VALUES ('12208864-7b61-4e6e-8573-53863bd93b35', 0, 785.55, 124, 8, 'Apartment', 7);

INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('22208864-7b61-4e6e-8573-53863bd93b35', 0, '2023-10-02 11:00:00.000000', true, '2023-10-05 10:00:00.000000', 1000, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '9acac245-25b3-492d-a742-4c69bfcb90cf');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('32208864-7b61-4e6e-8573-53863bd93b35', 0, '2023-10-06 11:00:00.000000', true, '2023-10-10 10:00:00.000000', 2000, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '9acac245-25b3-492d-a742-4c69bfcb90cf');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('42208864-7b61-4e6e-8573-53863bd93b35', 0, '2023-10-11 11:00:00.000000', true, '2023-10-21 10:00:00.000000', 3000, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '9acac245-25b3-492d-a742-4c69bfcb90cf');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('52208864-7b61-4e6e-8573-53863bd93b35', 0, '2022-10-11 11:00:00.000000', FALSE, '2023-02-01 10:00:00.000000', 30000, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '561d7a3a-6447-417e-a2ad-2053ec777a8b');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('6ddee1ee-9eba-4222-a031-463a849e1886', 0, '2022-05-01 11:00:00.000000', FALSE, '2022-05-09 10:00:00.000000', 3000, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '561d7a3a-6447-417e-a2ad-2053ec777a8b');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('831351a7-6d6a-4900-8dd8-edd13c484dee', 0, '2024-10-11 11:00:00.000000', FALSE, '2024-11-21 10:00:00.000000', 3000, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '561d7a3a-6447-417e-a2ad-2053ec777a8b');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('931351a7-6d6a-4900-8dd8-edd13c484dee', 0, '2021-10-11 11:00:00.000000', FALSE, '2021-11-21 10:00:00.000000', 430, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '66208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('931251a7-6d6a-4900-8dd8-edd13c484dee', 0, '2024-10-11 11:00:00.000000', FALSE, '2024-11-21 10:00:00.000000', 430, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '66208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO rent (id, version, begin_time, board, end_time, final_cost, client_id, room_id) VALUES ('931151a7-6d6a-4900-8dd8-edd13c484dee', 0, '2025-10-11 11:00:00.000000', FALSE, '2025-11-21 10:00:00.000000', 430, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '66208864-7b61-4e6e-8573-53863bd93b35');
