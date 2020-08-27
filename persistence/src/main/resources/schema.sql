CREATE TABLE customer (id SERIAL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255), email VARCHAR (255));
CREATE TABLE destination (id SERIAL PRIMARY KEY, name VARCHAR(255), time_zone VARCHAR(50));
CREATE TABLE reservation (reservation_id VARCHAR (255) PRIMARY KEY, start_date TIMESTAMP, end_date TIMESTAMP ,
price DECIMAL, cancellation_policy VARCHAR(50), customer_id BIGINT, destination_id BIGINT,
 cancellation_timestamp  TIMESTAMP
 NULL, creation_timestamp  TIMESTAMP
 , status VARCHAR (50));
CREATE TABLE refund (id SERIAL PRIMARY KEY, reservation_id VARCHAR (255), refund_amount DECIMAL,  creation_timestamp  TIMESTAMP );

 ALTER TABLE refund
    ADD FOREIGN KEY (reservation_id)
    REFERENCES reservation(reservation_id);