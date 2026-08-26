-- Users Table
CREATE TABLE Users (
    UserId SERIAL PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARCHAR(100) NOT NULL,
    SubscriptionType VARCHAR(10) CHECK (SubscriptionType IN ('Free', 'Basic', 'Premium')) DEFAULT 'Free',
    DateJoined DATE DEFAULT NOW() 
);

-- Movies Table
CREATE TABLE Movies (
    MovieId SERIAL PRIMARY KEY,
    Title VARCHAR(200) NOT NULL,
    Genre VARCHAR(50) NOT NULL,
    ReleaseYear INT NOT NULL,
    Rating DECIMAL(3, 1) CHECK (Rating >= 0 AND Rating <= 10),
    Duration INT NOT NULL -- Duration in minutes
);

-- WatchHistory Table
CREATE TABLE WatchHistory (
    WatchId SERIAL PRIMARY KEY,
    UserId INT NOT NULL,
    MovieId INT NOT NULL,
    WatchDate DATE DEFAULT NOW(),
    CompletionPercentage DECIMAL(5, 2) CHECK (CompletionPercentage >= 0 AND CompletionPercentage <= 100),
    CONSTRAINT FK_WatchHistory_Users FOREIGN KEY (UserId) REFERENCES Users(UserId) ON DELETE CASCADE,
    CONSTRAINT FK_WatchHistory_Movies FOREIGN KEY (MovieId) REFERENCES Movies(MovieId) ON DELETE CASCADE
);

-- Reviews Table
CREATE TABLE Reviews (
    ReviewId SERIAL PRIMARY KEY,
    UserId INT NOT NULL,
    MovieId INT NOT NULL,
    Rating DECIMAL(3, 1) CHECK (Rating >= 0 AND Rating <= 10),
    Comment VARCHAR(1000),
    ReviewDate DATE DEFAULT NOW(),
    CONSTRAINT FK_Reviews_Users FOREIGN KEY (UserId) REFERENCES Users(UserId) ON DELETE CASCADE,
    CONSTRAINT FK_Reviews_Movies FOREIGN KEY (MovieId) REFERENCES Movies(MovieId) ON DELETE CASCADE
);
