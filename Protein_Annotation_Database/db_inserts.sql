
use paws;

INSERT INTO `paws`.`annotation_type` (`type`) VALUES ('disopred_2');
INSERT INTO `paws`.`annotation_type` (`type`) VALUES ('disopred_3');
INSERT INTO `paws`.`annotation_type` (`type`) VALUES ('psipred_3');


--  Update these config values as needed

INSERT INTO config_system (config_key, config_value, comment) 
	VALUES ('serverUrlForModuleData', 'http://yeastrc.org/paws/services/internal_only/saveAnnotationJSONToDB',
		'Full URL to the PAWS service for the PAWS Jobcenter Modules to connect to. Should end in: paws/services/internal_only/saveAnnotationJSONToDB');

INSERT INTO config_system (config_key, config_value, comment) 
	VALUES ('jobsubmissionUrl', 'http://localhost:8080/JobCenter_Server_Jersey/', 
		'Path to the Jobcenter server to submit jobs to.  Should end in: /JobCenter_Server_Jersey/');
