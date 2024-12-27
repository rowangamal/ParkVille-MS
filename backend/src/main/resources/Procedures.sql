 SET  sql_safe_updates = 0;
 
 --  this procedure is to check if the driver is about to arrive 
DELIMITER #
CREATE PROCEDURE getAllArrivingWithin10Min()
BEGIN
	SELECT Driver_id , timestampdiff(minute , NOW() , start_time ) as time_diff
    FROM reserved_spot
    where arrival_time IS NULL AND start_time >= now() AND timestampdiff(minute , NOW() , start_time ) <= 10 ;
END #

DELIMITER ;


 --  this procedure is to check if the driver is about to leave
DELIMITER #
CREATE PROCEDURE getAllLeavingWithin10Min()
BEGIN
	SELECT Driver_id , timestampdiff(minute , NOW() , end_time ) as time_diff
    FROM reserved_spot
    where arrival_time IS not NULL AND leave_time is NULL AND end_time >= now() AND timestampdiff(minute , NOW() , end_time ) <= 10 ;
END #
DELIMITER ;

 --  this procedure is to check if the driver is has penalty overTime
DELIMITER #
CREATE PROCEDURE getPenaltyOverTime()
BEGIN
    DECLARE rows_affected INT;
    UPDATE reserved_spot
    SET penalty = calcPenalty(price , TIMESTAMPDIFF(MINUTE, end_time, NOW()) ,TIMESTAMPDIFF(MINUTE, start_time, end_time) )
    WHERE arrival_time IS NOT NULL
      AND leave_time IS NULL
      AND NOW() >= end_time
      AND TIMESTAMPDIFF(MINUTE, end_time, NOW()) >= 10 ;
    GET DIAGNOSTICS rows_affected = ROW_COUNT;
    IF rows_affected > 0 THEN
        SELECT Driver_id, TIMESTAMPDIFF(MINUTE, end_time, NOW()) as time_diff, penalty
        FROM reserved_spot
        WHERE arrival_time IS NOT NULL
          AND leave_time IS NULL
          AND NOW() >= end_time
          AND TIMESTAMPDIFF(MINUTE, end_time, NOW()) >= 10;
    END IF;
END #
DELIMITER ;

 --  this procedure is to check if the driver is about to leave
DELIMITER #
CREATE PROCEDURE getAllLeavingWithin10Min()
BEGIN
	SELECT Driver_id , timestampdiff(minute , NOW() , end_time ) as time_diff
    FROM reserved_spot
    where arrival_time IS not NULL AND leave_time is NULL AND end_time >= now() AND timestampdiff(minute , NOW() , end_time ) <= 10 ;
END #
DELIMITER ;

 --  this procedure is to check if no driver arrive and end_time finish
DELIMITER #
CREATE PROCEDURE getNoArrivedDrivers()
BEGIN
    DECLARE rows_affected INT;
    UPDATE parking_spot
    JOIN reserved_spot ON reserved_spot.Parking_Spot_id = parking_spot.id
    SET parking_spot.status = 'empty'
    WHERE reserved_spot.arrival_time IS NULL
      AND NOW() >= reserved_spot.end_time;
    GET DIAGNOSTICS rows_affected = ROW_COUNT;
    IF rows_affected > 0 THEN
        SELECT Driver_id, TIMESTAMPDIFF(MINUTE, end_time, NOW()) as time_diff, price , Parking_Spot_id
        FROM reserved_spot
        WHERE arrival_time IS NULL
          AND NOW() >= end_time ;
    END IF;
END #
DELIMITER ;


