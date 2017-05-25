
INSERT INTO node (name, description) VALUES ('PAWS_Job_Submission', 'Protein Annotation Webservices, context \'paws\' job submissions');



INSERT INTO node_access_rule (node_id, network_address) 
	SELECT id, '128.95.70.*' FROM node WHERE name = 'PAWS_Job_Submission'  ;

INSERT INTO node_access_rule (node_id, network_address) 
	SELECT id, '127.0.0.1' FROM node WHERE name = 'PAWS_Job_Submission'  ;

INSERT INTO node_access_rule (node_id, network_address) 
	SELECT id, '0:0:0:0:0:0:0:1' FROM node WHERE name = 'PAWS_Job_Submission'  ;



INSERT INTO job_type (priority, name, description, enabled, module_name, minimum_module_version) 
	VALUES ('5', 'ProteinAnnotation_Disopred_2', 'ProteinAnnotation_Disopred_2', 1, 'ProteinAnnotation_RunDisopred_2', '1');
INSERT INTO job_type (priority, name, description, enabled, module_name, minimum_module_version) 
	VALUES ('25', 'ProteinAnnotation_Disopred_2_Batch', 'ProteinAnnotation_Disopred_2 for Batch priority', 1, 'ProteinAnnotation_RunDisopred_2', '1');

INSERT INTO request_type (name) VALUES ('ProteinAnnotation_Disopred_2');

INSERT INTO job_type (priority, name, description, enabled, module_name, minimum_module_version) 
	VALUES ('5', 'ProteinAnnotation_Disopred_3', 'ProteinAnnotation_Disopred_3', 1, 'ProteinAnnotation_RunDisopred_3', '1');
INSERT INTO job_type (priority, name, description, enabled, module_name, minimum_module_version) 
	VALUES ('25', 'ProteinAnnotation_Disopred_3_Batch', 'ProteinAnnotation_Disopred_3 for Batch priority', 1, 'ProteinAnnotation_RunDisopred_3', '1');


INSERT INTO request_type (name) VALUES ('ProteinAnnotation_Disopred_3');


INSERT INTO job_type (priority, name, description, enabled, module_name, minimum_module_version) 
	VALUES ('5', 'ProteinAnnotation_Psipred_3', 'ProteinAnnotation_Psipred_3', 1, 'ProteinAnnotation_RunPsipred_3', '1');
INSERT INTO job_type (priority, name, description, enabled, module_name, minimum_module_version) 
	VALUES ('25', 'ProteinAnnotation_Psipred_3_Batch', 'ProteinAnnotation_Psipred_3 for Batch priority', 1, 'ProteinAnnotation_RunPsipred_3', '1');

INSERT INTO request_type (name) VALUES ('ProteinAnnotation_Psipred_3');
