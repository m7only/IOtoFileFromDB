create table customers
(
    id        bigserial
        primary key,
    firstname varchar(255),
    lastname  varchar(255)
);

create table products
(
    id          bigserial
        primary key,
    price       integer,
    productname varchar(255)
);

create table purchases
(
    id           bigserial
        primary key,
    purchasedate timestamp(6),
    customer_id  bigint
        constraint fk9q5yt0n9s4ube31bxfamhir44
            references customers,
    product_id   bigint
        constraint fkcacbvw28fu31rv1vrhnkcbe28
            references products
);