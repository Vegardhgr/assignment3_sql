ALTER TABLE apprentice
ADD MentorId INT;

ALTER TABLE apprentice
ADD CONSTRAINT Fk_apprenticeHero 
FOREIGN KEY (MentorId) REFERENCES hero(Id);