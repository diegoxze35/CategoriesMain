create table if not exists category
(
    id_category int          not null auto_increment primary key,
    name        varchar(100) not null,
    description varchar(250) not null
);

create table if not exists event
(
    id_event    int          not null auto_increment primary key,
    description varchar(100) not null,
    date_event  date         not null,
    name        varchar(250) not null,
    id_category int,
    foreign key (id_category) references category (id_category)
);

create procedure if not exists create_category(in _name varchar(100), in _description varchar(250), out _id int)
begin
    insert into category(name, description) values (_name, _description);
    set _id = last_insert_id();
end;

create procedure if not exists delete_category(_id_category int)
begin
    delete from category where id_category = _id_category;
end;

create procedure if not exists update_category(_id_category int, _name varchar(100), _description varchar(250))
begin
    update category set name = _name, description = _description where id_category = _id_category;
end;

create procedure if not exists select_one(_id_category int)
begin
    select * from category where id_category = _id_category;
end;

create procedure if not exists select_all()
begin
    select * from category;
end;

create procedure if not exists save_event(
    in p_id_event int,
    in p_id_category int,
    in p_category_name varchar(100),
    in p_category_description varchar(250),
    in p_event_description varchar(100),
    in p_event_date date,
    in p_event_name varchar(250)
)
begin
    declare v_category_id int;
    declare v_event_exists int;

    -- Verificar si el evento existe (para UPDATE)
    if p_id_event is not null then
        select count(*) into v_event_exists
        from event
        where id_event = p_id_event;

        if v_event_exists = 0 then
            signal sqlstate '45000'
            set message_text = 'Error: El ID de evento no existe';
        end if;
    end if;

    -- Manejo de la categoría
    if p_id_category is null then
        -- Insertar nueva categoría si id_category es null
        insert into category (name, description)
        values (p_category_name, p_category_description);

        -- Obtener el ID generado para la nueva categoría
        set v_category_id = last_insert_id();
    else
        -- Verificar si el ID de categoría existe
        if not exists (select 1 from category where id_category = p_id_category) then
            signal sqlstate '45000'
            set message_text = 'Error: El ID de categoría no existe';
        else
            set v_category_id = p_id_category;
        end if;
    end if;

    -- Operación insert o UPDATE
    if p_id_event is null then
        -- insert: Crear nuevo evento
        insert into event (description, date_event, name, id_category)
        values (p_event_description, p_event_date, p_event_name, v_category_id);

        -- Devolver ambos IDs generados
        select last_insert_id() as id_event, v_category_id as id_category;
    else
        -- UPDATE: Actualizar evento existente
        update event
        set description = p_event_description,
            date_event = p_event_date,
            name = p_event_name,
            id_category = v_category_id
        where id_event = p_id_event;

        -- Devolver los IDs actualizados
        select p_id_event as id_event, v_category_id as id_category;
    end if;
end;

delete from event;
delete from category;

ALTER TABLE category AUTO_INCREMENT = 1;
ALTER TABLE event AUTO_INCREMENT = 1;
