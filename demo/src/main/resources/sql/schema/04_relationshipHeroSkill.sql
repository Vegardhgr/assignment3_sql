CREATE TABLE IF NOT EXISTS heroSkill (
    HeroId INT,
    SkillId INT,

    PRIMARY KEY (HeroId, SkillId),
    CONSTRAINT Fk_HeroSkill FOREIGN KEY (HeroId) REFERENCES hero(Id),
    CONSTRAINT Fk_SkillHero FOREIGN KEY (SkillId) REFERENCES skill(Id)
);