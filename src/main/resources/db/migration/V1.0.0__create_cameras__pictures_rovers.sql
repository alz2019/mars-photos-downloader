CREATE TABLE cameras
(
    id          SERIAL PRIMARY KEY,
    original_id INT       NOT NULL UNIQUE,
    name        TEXT      NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE rovers
(
    id           BIGSERIAL PRIMARY KEY,
    original_id  BIGINT    NOT NULL UNIQUE,
    name         TEXT      NOT NULL,
    landing_date DATE,
    launch_date  DATE,
    status       TEXT
);

CREATE TABLE photos
(
    id          BIGSERIAL PRIMARY KEY,
    original_id BIGINT    NOT NULL UNIQUE,
    img_src     TEXT      NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    camera_id   INT       NOT NULL REFERENCES cameras (id),
    rover_id   INT       NOT NULL REFERENCES rovers (id)
);
