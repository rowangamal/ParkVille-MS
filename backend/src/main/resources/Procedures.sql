SET sql_safe_updates = 0;

-- This procedure checks if the driver is about to arrive
DELIMITER #
CREATE PROCEDURE getAllArrivingWithin10Min()
BEGIN
SELECT Driver_id,
       TIMESTAMPDIFF(MINUTE, NOW(), start_time) AS time_diff
FROM Reserved_Spot
WHERE arrival_time IS NULL
  AND start_time >= NOW()
  AND TIMESTAMPDIFF(MINUTE, NOW(), start_time) <= 10;
END #
DELIMITER ;

-- This procedure checks if the driver is about to leave
DELIMITER #
CREATE PROCEDURE getAllLeavingWithin10Min()
BEGIN
SELECT Driver_id,
       TIMESTAMPDIFF(MINUTE, NOW(), end_time) AS time_diff
FROM Reserved_Spot
WHERE arrival_time IS NOT NULL
  AND leave_time IS NULL
  AND end_time >= NOW()
  AND TIMESTAMPDIFF(MINUTE, NOW(), end_time) <= 10;
END #
DELIMITER ;

-- This procedure checks if the driver has a penalty for overtime
DELIMITER #
CREATE PROCEDURE getPenaltyOverTime()
BEGIN
    DECLARE rows_affected INT;

UPDATE Reserved_Spot
SET penalty = calcPenalty(price,
                          TIMESTAMPDIFF(MINUTE, end_time, NOW()),
                          TIMESTAMPDIFF(MINUTE, start_time, end_time))
WHERE arrival_time IS NOT NULL
  AND leave_time IS NULL
  AND NOW() >= end_time
  AND TIMESTAMPDIFF(MINUTE, end_time, NOW()) >= 10;

GET DIAGNOSTICS rows_affected = ROW_COUNT;

IF rows_affected > 0 THEN
SELECT Driver_id,
       TIMESTAMPDIFF(MINUTE, end_time, NOW()) AS time_diff,
       penalty
FROM Reserved_Spot
WHERE arrival_time IS NOT NULL
  AND leave_time IS NULL
  AND NOW() >= end_time
  AND TIMESTAMPDIFF(MINUTE, end_time, NOW()) >= 10;
END IF;
END #
DELIMITER ;

-- This procedure checks if no driver arrived and the end time has passed
DELIMITER #
CREATE PROCEDURE getNoArrivedDrivers()
BEGIN
    DECLARE rows_affected INT;

UPDATE Parking_Spot
    JOIN Reserved_Spot ON Reserved_Spot.Parking_Spot_id = Parking_Spot.id
    SET Parking_Spot.status = 'empty'
WHERE Reserved_Spot.arrival_time IS NULL
  AND NOW() >= Reserved_Spot.end_time;

GET DIAGNOSTICS rows_affected = ROW_COUNT;

IF rows_affected > 0 THEN
SELECT Driver_id,
       TIMESTAMPDIFF(MINUTE, end_time, NOW()) AS time_diff,
       price,
       Parking_Spot_id
FROM Reserved_Spot
WHERE arrival_time IS NULL
  AND NOW() >= end_time;
END IF;
END #
DELIMITER ;
