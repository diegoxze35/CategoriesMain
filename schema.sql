create table category(
    id_category int not null auto_increment primary key,
    name varchar(100) not null,
    description varchar(250) not null
);
insert into category(name, description) values ('Sports', 'Sport items');
insert into category(name, description) values ('Sports 2', 'Sport items 2');
insert into category(name, description) values ('Sports 3', 'Sport items 3');
insert into category(name, description) values ('Sports 4', 'Sport items 4');

update category set name = 'electronics', description = 'electronic components' where id_category = 1;
show tables;

select * from category;

create procedure create_category(_name varchar(100), _description varchar(250))
    begin
        insert into category(name, description) values(_name, _description);
    end;

create procedure delete_category(_id_category int)
    begin
        delete from category where id_category = _id_category;
    end;

create procedure update_category(_id_category int, _name varchar(100), _description varchar(250))
    begin
        update category set name = _name, description = _description where id_category = _id_category;
    end;

create procedure select_one(_id_category int)
    begin
        select * from category where id_category = _id_category;
    end;

create procedure select_all()
    begin
        select * from category;
    end;

call create_category('Cómputo', 'Art. de cómputo');

create table event(
    id_event int not null auto_increment primary key,
    description varchar(100) not null,
    date_event date not null,
    name varchar(250) not null,
    id_category int,
    foreign key (id_category) references category(id_category)
);