insert into driver(username , email ,license_plate_number, password , payment_method) value("rafyhany", "rafy","1233","rafy", "credit card");



insert into parking_lot_manager(username , email  , password ) value("rafyhany", "rafy","1233");

insert into parking_lot value(1,"3","4",1 , 20,"disabled",0,1);

insert parking_spot value(1,"empty", 1);

insert into reserved_spot value ( "2024-12-27 13:00:00","2024-12-27 10:40:00" , null , null , 1 , 1 , 1 , 10 , 0 ) ;
insert into reserved_spot value ( "2024-12-27 13:00:00","2024-12-27 10:57:00" , null , null , 1 , 1 , 1 , 10 , 0 ) ;
insert into reserved_spot value ( "2024-12-27 12:17:00","2024-12-27 10:50:00" , null , null , 1 , 1 , 1 , 10 , 0 ) ;
insert into reserved_spot value ( "2024-12-27 12:17:00","2024-12-27 10:51:00" , "2024-12-27 10:51:0" , null , 1 , 1 , 1 , 10 , 0 ) ;
insert into reserved_spot value ( "2024-12-27 12:55:00","2024-12-27 10:00:00" , "2024-12-27 10:51:0" , null , 1 , 1 , 1 , 175 , 0 ) ;
insert into reserved_spot value ( "2024-12-27 15:55:00","2024-12-27 14:00:00" , null , null , 1 , 1 , 1 , 175 , 0 ) ;


--delete from reserved_spot
--where Driver_id = 1 ;


select * from reserved_spot;



