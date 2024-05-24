-- USERS

INSERT INTO users (email, name, password)
VALUES ('admin@gmail.com', 'Admin', '$2a$12$IKEQb00u5QpZMx4v5zMweu.3wrq0pS7XLCHO4yHZ.BW/yvWu1feo2'); --123
INSERT INTO users (email, name, password)
VALUES ('bya.mng@gmail.com', 'Byan', '$2a$12$IKEQb00u5QpZMx4v5zMweu.3wrq0pS7XLCHO4yHZ.BW/yvWu1feo2'); --123
INSERT INTO users (email, name, password)
VALUES ('byambaa.mng@gmail.com', 'Mr. Byan', '$2a$12$IKEQb00u5QpZMx4v5zMweu.3wrq0pS7XLCHO4yHZ.BW/yvWu1feo2');
--123

-- PROPERTY
INSERT INTO properties (title, description, price, address, image, city, state, zip_code, listing_type, property_type,
                        property_status, owner_id)
VALUES ('Property 1', 'Awesome', 50000, '1000N',
        'https://images.unsplash.com/photo-1460472178825-e5240623afd5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8YnVpbGRpbmdzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60',
        'fairfield', 'iowa', '52557', 'SALE', 'CONDO', 'UNAVAILABLE', 2);

INSERT INTO properties (title, description, price, address, image, city, state, zip_code, listing_type, property_type,
                        property_status, owner_id)
VALUES ('Property 2', 'Very Good', 90000, '1000N',
        'https://images.unsplash.com/photo-1486325212027-8081e485255e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8YnVpbGRpbmdzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60',
        'fairfield', 'iowa', '52557', 'RENT', 'HOUSE', 'AVAILABLE', 2);

INSERT INTO properties (title, description, price, address, image, city, state, zip_code, listing_type, property_type,
                        property_status, owner_id)
VALUES ('Property 3', 'Excellent', 100000, '1000N',
        'https://images.unsplash.com/photo-1423768164017-3f27c066407f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8YnVpbGRpbmdzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60',
        'fairfield', 'iowa', '52557', 'SALE', 'APARTMENT', 'AVAILABLE', 2);

INSERT INTO properties (title, description, price, address, image, city, state, zip_code, listing_type, property_type,
                        property_status, owner_id)
VALUES ('Property 4', 'Nice', 200000, '1000N',
        'https://images.unsplash.com/photo-1423768164017-3f27c066407f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8YnVpbGRpbmdzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60',
        'fairfield', 'iowa', '52557', 'RENT', 'APARTMENT', 'PENDING', 2);

INSERT INTO properties (title, description, price, address, image, city, state, zip_code, listing_type, property_type,
                        property_status, owner_id)
VALUES ('Property 5', 'Beautiful', 200000, '1000N',
        'https://plus.unsplash.com/premium_photo-1673686854151-e56e836758f8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTN8fGJ1aWxkaW5nc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60',
        'fairfield', 'iowa', '52557', 'RENT', 'APARTMENT', 'CONTINGENT', 2);


-- -- OFFERS
INSERT INTO offers (message, offer_status, price, property_id, user_id)
VALUES ('I Like this Property', 'APPROVED', 80000, 2, 3);

INSERT INTO offers (message, offer_status, price, property_id, user_id)
VALUES ('I want to offer Property', 'APPROVED', 80000, 3, 3);

INSERT INTO offers (message, offer_status, price, property_id, user_id)
VALUES ('I am in love', 'PENDING', 80000, 5, 3);

-- -- ROLES
    INSERT
INTO roles (role)
VALUES ('ADMIN');
INSERT INTO roles (role)
VALUES ('OWNER');
INSERT INTO roles (role)
VALUES ('CUSTOMER');


INSERT INTO users_roles (user_id, roles_id)
VALUES (1, 1);
INSERT INTO users_roles (user_id, roles_id)
VALUES (2, 2);
INSERT INTO users_roles (user_id, roles_id)
VALUES (3, 3);