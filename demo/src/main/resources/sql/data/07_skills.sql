INSERT INTO skill (Name, Description) 
VALUES ('Sword Fighting', 'Proficiency in using swords'),
('Shield Usage', 'Skill in wielding shields for protection'),
('Archery', 'Ability to use bows and arrows effectively'),
('Magic', 'Knowledge and practice of magical spells');


INSERT INTO heroSkill (HeroId, SkillId)
VALUES (1, 1), -- Bjarne has Sword Fighting
(2, 2),
(3, 3), -- Bjarne3 has Archery
(1, 4), -- Bjarne has Magic
(2, 1); -- Bjarne2 has Sword Fighting