CREATE TABLE IF NOT EXISTS team (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL CHECK (state IN ('AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO')),
    photo_url VARCHAR(1024) NOT NULL,
    UNIQUE(name, state)
    );

CREATE TABLE IF NOT EXISTS matches (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    team_one_id BIGINT NOT NULL REFERENCES team(id) ON UPDATE CASCADE,
    score_team_one INTEGER NOT NULL,
    team_two_id BIGINT NOT NULL REFERENCES team(id) ON UPDATE CASCADE,
    score_team_two INTEGER NOT NULL,
    team_supported_id BIGINT NOT NULL REFERENCES team(id) ON UPDATE CASCADE,
    UNIQUE(date, team_one_id, team_two_id)
    );
