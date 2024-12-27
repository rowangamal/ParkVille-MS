


DELIMITER #
CREATE FUNCTION calcPenalty(price INT, overTime INT , orignalTime INT)
RETURNS INT DETERMINISTIC
BEGIN
    RETURN overTime * (price / orignalTime);
END #
DELIMITER ;
