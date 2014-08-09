DO $$DECLARE count int;
BEGIN
	SELECT count(*) INTO count FROM information_schema.schemata WHERE schema_name = 'adm';
	IF count < 1 THEN
		create schema ADM authorization adm;
	END IF;
END$$;

DO $$DECLARE count int;
BEGIN
	SELECT count(*) INTO count FROM information_schema.schemata WHERE schema_name = 'sec';
	IF count < 1 THEN
		create schema SEC authorization adm;
	END IF;
END$$;