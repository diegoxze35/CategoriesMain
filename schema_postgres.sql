-- Tablas
CREATE TABLE IF NOT EXISTS category (
    id_category SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS event (
    id_event SERIAL PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    date_event DATE NOT NULL,
    name VARCHAR(250) NOT NULL,
    id_category INT,
    FOREIGN KEY (id_category) REFERENCES category (id_category)
);

-- Procedimientos almacenados
CREATE OR REPLACE PROCEDURE create_category(
    IN _name VARCHAR(100),
    IN _description VARCHAR(250),
    INOUT _id INTEGER DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO category(name, description)
    VALUES (_name, _description)
    RETURNING id_category INTO _id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_category(IN _id_category INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM category WHERE id_category = _id_category;
END;
$$;

CREATE OR REPLACE PROCEDURE update_category(
    IN _id_category INT,
    IN _name VARCHAR(100),
    IN _description VARCHAR(250)
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE category
    SET name = _name,
        description = _description
    WHERE id_category = _id_category;
END;
$$;

CREATE OR REPLACE FUNCTION select_one(_id_category INT)
RETURNS TABLE (
    id_category INT,
    name VARCHAR(100),
    description VARCHAR(250)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT c.id_category, c.name, c.description
    FROM category c
    WHERE c.id_category = _id_category;
END;
$$;

CREATE OR REPLACE FUNCTION select_all()
RETURNS TABLE (
    id_category INT,
    name VARCHAR(100),
    description VARCHAR(250)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT c.id_category, c.name, c.description
    FROM category c;
END;
$$;

CREATE OR REPLACE FUNCTION save_event(
    p_id_event INT,
    p_id_category INT,
    p_category_name VARCHAR(100),
    p_category_description VARCHAR(250),
    p_event_description VARCHAR(100),
    p_event_date DATE,
    p_event_name VARCHAR(250)
)
RETURNS TABLE (id_event INT, id_category INT)
LANGUAGE plpgsql
AS $$
DECLARE
    v_category_id INT;
    v_event_exists INT;
BEGIN
    -- Verificar si el evento existe
    IF p_id_event IS NOT NULL THEN
        SELECT COUNT(*) INTO v_event_exists
        FROM event
        WHERE id_event = p_id_event;

        IF v_event_exists = 0 THEN
            RAISE EXCEPTION 'Error: El ID de evento no existe';
        END IF;
    END IF;

    -- Manejo de la categoría
    IF p_id_category IS NULL THEN
        INSERT INTO category (name, description)
        VALUES (p_category_name, p_category_description)
        RETURNING category.id_category INTO v_category_id;
    ELSE
        IF NOT EXISTS (SELECT 1 FROM category WHERE category.id_category = p_id_category) THEN
            RAISE EXCEPTION 'Error: El ID de categoría no existe';
        END IF;
        v_category_id := p_id_category;
    END IF;

    -- Operación INSERT o UPDATE
    IF p_id_event IS NULL THEN
        INSERT INTO event (description, date_event, name, id_category)
        VALUES (p_event_description, p_event_date, p_event_name, v_category_id)
        RETURNING event.id_event INTO id_event;

        id_category := v_category_id;
        RETURN NEXT;
    ELSE
        UPDATE event
        SET description = p_event_description,
            date_event = p_event_date,
            name = p_event_name,
            id_category = v_category_id
        WHERE id_event = p_id_event;

        id_event := p_id_event;
        id_category := v_category_id;
        RETURN NEXT;
    END IF;
END;
$$;

select * from event inner join category on event.id_category = category.id_category;
select * from save_event((NULL), ('1'::int4), ('Cat 1'), ('Desc 1'), ('Desv e 1'), ('2025-09-18 -06'), ('Evt 1'));

-- Reinicio de secuencias (equivalente a AUTO_INCREMENT)
TRUNCATE TABLE event, category RESTART IDENTITY;