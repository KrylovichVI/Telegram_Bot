#INSERT INTO flags(name, value, date) VALUES ('FLAG_BIRTHDAY', false, '2018-01-24');
#INSERT INTO flags(name, value, date) VALUES ('FLAG_WEATHER', false, '2018-01-24');
#INSERT INTO flags(name, value, date) VALUES ('FLAG_NOTIFICATION', false, '2018-01-24');
#UPDATE flags set Value = FALSE WHERE Name = 'FLAG_BIRTHDAY';
#DELETE FROM flags WHERE idFlags >= 1;