# Grading feedback - Stream Flix Heroes (Module 2)

**Students:** Vegard and Tobias

**Repository:** `Vegardhgr/assignment3_sql`

**Graded against:** two branches, as your README on `main` directs - `appendix_a` at `3bd7f20` for the SQL scripts, and `appendix_b` at `9c62fea` for the application. This is an exception: normally only `main` is marked - see the next section. This is a pair assignment and both of you are group members, so everything on those branches counts as your submission.

**Status:** reviewed - instructor feedback has been included. Ready to read.

**Outcome:** pass, with improvements to make. See the Overall section at the end.

---

## Important: only `main` is marked

**Submissions are marked on the `main` branch only.** On `main`, this repository holds the RPG
Heroes SQL and no application code at all - the whole of Appendix B lives on `appendix_b`, which
was never merged. Marked strictly, that would mean Appendix B is missing.

This time, your other branches have been graded as the submission, because your README points to
them and it would be unfair to fail work that clearly exists. **From the next assignment on, only
what is on `main` will be marked.** Make sure your finished work is merged before you submit.

This isn't just a marking rule - merging is a core part of the software development lifecycle.
`main` is the branch everyone treats as "the project": reviewers, teammates, CI pipelines and
deployments all start there, and most people will never look further. Branches are for work in
progress; when the work is done, it gets merged back through a pull request, and that merge is
what makes it part of the product. Work left on a branch effectively hasn't been delivered.

For a two-part assignment like this, the usual layout is one branch with both parts side by side
- for example an `appendix-a/` folder with the scripts and a `streamflix/` folder with the
application. As it stands, `appendix_b` also *deleted* the Appendix A scripts to make room, so
there is no single version of this repository that contains your whole submission.

---

## Build and run result

The `appendix_b` application builds on JDK 21 and was run against a fresh PostgreSQL 16 database
(the version in your `docker-compose.yml`), created with your own `db_init/01_dbCreate.sql`. Only
the port in the datasource URL was overridden on the command line. `DemoConsole` runs to the end
and prints a result for every query.

There are no tests beyond the generated `contextLoads`, and the brief doesn't ask for any.

---

## Section A - SQL scripts, RPGHeroesDb

All nine scripts were run in order against a fresh PostgreSQL 16 database, then the resulting
schema and data queried directly.

- [✅] **`01_dbCreate.sql` creates the database** - creates the `rpghero` role and `"RPGHeroesDb"` owned by it. Creating a dedicated role instead of using the superuser is a good habit.
- [✅] **`02_tableCreate.sql` - Hero table** - `hero` has `id` (serial, PK `hero_pkey`), `name`, `class`, `level`.
- [✅] **`02_tableCreate.sql` - Apprentice table** - `apprentice` has `id` (serial, PK) and `name`; `mentorid` is added in `03`.
- [✅] **`02_tableCreate.sql` - Skill table** - `skill` has `id` (serial, PK), `name`, `description`.
- [✅] **`03_relationshipHeroApprentice.sql` - FK constraint** - adds `mentorid`, then `fk_apprenticehero`: `FOREIGN KEY (mentorid) REFERENCES hero(id)`.
- [✅] **`04_relationshipHeroSkill.sql` - linking table** - `heroskill` with composite `heroskill_pkey PRIMARY KEY (heroid, skillid)`.
- [✅] **`04_relationshipHeroSkill.sql` - FK constraints** - both present: `fk_heroskill → hero(id)` and `fk_skillhero → skill(id)`.
- [✅] **`05_insertHeroes.sql` - three heroes** - Bjarne, Bjarne2, Bjarne3.
- [✅] **`06_insertApprentices.sql` - three apprentices, linked** - Knut, Knut2, Knut3, mentored by heroes 1, 2 and 3.
- [✅] **`07_skills.sql` - four skills** - Sword Fighting, Shield Usage, Archery, Magic.
- [✅] **`07_skills.sql` - skills associated with heroes** - 5 rows in `heroskill`, every pair resolving to a real hero and skill.
- [✅] **`07_skills.sql` - one hero with multiple skills** - Bjarne and Bjarne2 have 2 each.
- [✅] **`07_skills.sql` - one skill on multiple heroes** - Sword Fighting sits on Bjarne and Bjarne2.
- [✅] **`08_updateHero.sql` - updates one hero** - Bjarne's `level` goes from 2 to 10.
- [✅] **`09_deleteApprentice.sql` - deletes by name, integrity intact** - `DELETE FROM apprentice WHERE Name = 'Knut'`; count drops 3 → 2.
- [✅] **All nine scripts run in order with no errors** - clean on a fresh database.

A clean run. Wiring the scripts into `spring.sql.init` so the application builds the database
itself, with `db_init` handled by the Postgres container, is a nice touch.

A few things for next time, none of which cost you anything here:

- **Nothing is `NOT NULL`.** A hero with no name, an apprentice with no mentor and a skill with no
  name are all allowed. `mentorid` in particular: the brief says an apprentice *has* one hero as
  their mentor, which is what `NOT NULL` on that column would enforce.
- **The PascalCase identifiers don't survive.** `MentorId`, `HeroId` and `heroSkill` are unquoted,
  so Postgres stores them as `mentorid`, `heroid` and `heroskill`. Everything works, but the
  scripts suggest names the database doesn't actually have. In Postgres, `snake_case`
  (`mentor_id`, `hero_skill`) is the convention because it survives as written.
- **`06` and `07` hard-code ids**, which only works because the serial columns start at 1 on a
  fresh table. Looking the id up by name - `(SELECT id FROM hero WHERE name = 'Bjarne')` -
  doesn't depend on that.

---

## Section B - Repository pattern, StreamFlix application (`appendix_b`)

- [✅] **SQL client library installed** - `org.postgresql:postgresql` with `spring-boot-starter-data-jdbc`.
- [✅] **Repository abstraction exists** - three repositories (`UserRepository`, `MovieRepository`, `SubscriptionTypeRepository`), each implementing an interface, each taking a `DataSource` through its constructor. See the notes - `DemoConsole` depends on the concrete classes rather than the interfaces.
- [✅] **Model classes** - `entity/` for `User` and `Movie`, and `model/` for the result types `UserSubscriptionType`, `MoviesMostWatched` and `MoviesMostPopular`. Separating entities from result objects is a good distinction to make.
- [⚠️] **Req 1 - Read all users** - `getAllUsers()` returns all 29 users with every field populated, but the console only ever shows `Id: 1, Name: Alice Johnson` - `User.toString()` leaves out email, subscription type and date joined, which the brief asks to display.
- [✅] **Req 2 - Read user by Id** - `getUserById(int)` returns `Optional<User>`; id 1 → Alice Johnson. (The console prints the `Optional` itself: `Optional[Id: 1, Name: Alice Johnson]`.)
- [⚠️] **Req 3 - Read user by name (partial match)** - `getUsersByName` uses `WHERE FirstName LIKE ?` but binds the name exactly as given, with no `%` around it, so `LIKE` behaves like `=`. The console searches for `"harl"` and gets `[]`, even though Charlie Brown is in the table. It also only searches first names, so "Smith" can never match.
- [✅] **Req 4 - Paged users** - `getUsersPage(limit, offset)` uses `LIMIT ? OFFSET ?`. It's never called from the console, and there's no `ORDER BY` - see the notes.
- [✅] **Req 5 - Add new user** - `addUser` inserts first name, last name, email, password and subscription type, leaving the id and join date to the database. Knut Lute is created as user 30. `password` isn't on the brief's field list, but the schema makes it `NOT NULL`, so including it is reasonable.
- [⚠️] **Req 6 - Update existing user** - the update never happens. `UserRepositoryInterface` declares `updateUserFirstNameLastName(String email, String fn, String ln)`, but `UserRepository` implements it as `(String fn, String ln, String email)`. `DemoConsole` follows the interface's order, so the email lands in the first-name slot and the query runs `WHERE Email = 'Lute'`. It matches nothing and returns `false` - Knut is still "Knut Lute" in the database afterwards.
- [✅] **Req 7 - Users per subscription type, descending** - `GROUP BY SubscriptionType`, `ORDER BY usersInSubscription DESC`, returned as `List<UserSubscriptionType>`.
- [✅] **Req 8 - Most watched movies, descending** - joins `WatchHistory` to `Movies`, groups by movie and orders by `watchCount DESC`, returned as `List<MoviesMostWatched>` with titles.
- [✅] **Req 9 - Most popular genre per user, with titles** - one query: a subquery finds the user's top genre, and the outer query returns the titles they watched in it. For user 1 that's Sci-Fi: Inception, The Matrix, Interstellar. This is the hardest query in the assignment, and it's done properly, entirely in SQL.

---

## Section C - Method documentation

- [✅] **Summary on every public method** - Javadoc on every public repository method, plus `mapRow`.
- [✅] **Exceptions documented where thrown** - `@throws RuntimeException` on every method that wraps a `SQLException`.
- [✅] **Return value documented where applicable** - `@return` throughout, including what `Optional.empty()` and `false` mean.

The documentation is there, but it's on the implementation classes - the interfaces have none. The
interface is the contract the rest of the application programs against, so it's where someone
reads to find out what a repository does. Put the Javadoc there; the implementations pick it up
automatically through `@Override`.

Two smaller points. Declaring `throws RuntimeException` in a method signature does nothing - it's
unchecked, and the `@throws` Javadoc line is what tells the caller. And parameter names like `fn`,
`ln`, `em`, `pw` and `st` make the Javadoc do work the names should be doing: with `firstName`,
`lastName` and `email`, half those `@param` lines would explain themselves. That's also exactly
how the Req 6 bug got in - `(fn, ln, email)` and `(email, fn, ln)` are easy to mix up when the
names are that short.

---

## Worked-example checks

Run against the database your `tables.sql` and `testData.sql` create.

- [✅] **User by Id = 1** - Alice Johnson, alice.johnson@example.com, Premium. Matches.
- [⚠️] **Name search `Smith`** - expected Bob Smith. Your query returns nothing for `"Smith"`, and nothing for `"%Smith%"` either, because only `FirstName` is searched.
- [✅] **Page size 5, page 2** - `LIMIT 5 OFFSET 5` returns userids 6-10: Frank White, Grace Clark, Henry Lewis, Isla Walker, Jack Hall. Matches on a fresh table - but see the note about `ORDER BY`.
- [✅] **Users per subscription type** - Free 9, Basic 10, Premium 10 in the seed data (the console shows Free 10, because it runs after adding Knut as a Free user). Matches.
- [✅] **Most watched movies** - every watched movie has 1 view, so there's no single winner. The query is right; the data is flat.
- [✅] **Most popular genre for userid 1** - Sci-Fi: Inception, The Matrix, Interstellar. Exact match.

---

## Notes and observations

Not scored - pointers for discussion.

- **Name your interfaces after what they are, and use them.** Java convention is that the
  interface gets the plain name and the implementation says what kind it is: `UserRepository` for
  the interface, `JdbcUserRepository` for the class. `UserRepositoryInterface` puts the label on
  the wrong side. More importantly, `DemoConsole` asks for `UserRepository`, `MovieRepository` and
  `SubscriptionTypeRepository` - the concrete classes - so the interfaces are never actually used.
  The point of the interface is that everything downstream depends on it, so the implementation
  can be swapped (a different database, a fake for a test). If the caller asks for the concrete
  class, that benefit is gone.

  It would also have caught the Req 6 bug. If `DemoConsole` had depended on the interface and you
  had read its signature, `(email, fn, ln)` versus `(fn, ln, email)` would have been staring at
  you. Java doesn't check that an implementation's parameter *names* match the interface - only the
  types, and here all three are `String`.

- **Paging needs an `ORDER BY`.** Without one, Postgres can return rows in any order, so "page 2"
  isn't guaranteed to be the same users twice. It happens to look right on a freshly loaded table;
  after some updates and deletes it may not. `ORDER BY UserId LIMIT ? OFFSET ?` fixes it.

- **The app only starts on an empty database.** `tables.sql` uses plain `CREATE TABLE`, and
  `spring.sql.init.mode=always` runs it on every boot. Your Docker setup gets away with it because
  the database lives in `tmpfs` and is wiped each time. Against a database that persists, the
  second start fails:

  ```
  PSQLException: ERROR: relation "users" already exists
  ```

  `DROP TABLE IF EXISTS ... CASCADE` at the top of `tables.sql` (or `CREATE TABLE IF NOT EXISTS`
  plus data that doesn't duplicate) makes it safe to start repeatedly. The same applies to
  `testData.sql` and the `Knut` user the console inserts - its email is `UNIQUE`, so even with the
  tables kept, a second run would fail on that insert.

- **The console shows less than the code does.** `User.toString()` returns only id and name, so
  the "read all users" output can't show the email, subscription type and join date the brief
  asks for. `getUserById` prints `Optional[...]` because the `Optional` is printed rather than
  unwrapped. `getUsersPage` is never called. The console is the only place anyone sees the
  application work, so it's worth making each step clearly show the requirement it covers.

- **`Movie` is unused and couldn't be used.** It has no constructor, so every field is stuck at its
  default, and `releaseYear` is a `LocalDate` while the column is an `INT`. Either finish it or
  delete it - code that isn't used goes stale.

- **Tidy-ups.**
  - `appendix_b` commits a whole `demo/bin/` folder - 15 files including compiled `.class` files
    and duplicate copies of `pom.xml` and `mvnw`. That's the VS Code Java extension's build output;
    add `bin/` to `.gitignore` and remove it.
  - `pom.xml` includes `spring-boot-starter-data-jpa`, but nothing uses JPA - it just makes
    Hibernate start up for no reason. `spring-boot-starter-data-jdbc` is all you need.
  - `addUser` takes the subscription type as a free `String`, so a typo only shows up as a
    database `CHECK` violation. An enum would catch it at compile time.
  - Error messages: "Could now update user" should be "Could not".

- **Git history.** Small commits with clear prefixes (`FEAT:`, `FIX:`, `DOCS:`) and descriptive
  messages - `6d209c5` explains exactly what was restructured and why. The workflow just stops one
  step short: the branches were never merged back.

---

## Submission requirements

Repository access and format are fine. The submission structure - two unmerged branches, with
`main` holding neither part in full - is covered at the top.

The optional extras weren't required: no relationship diagram, and no Controller → Service →
Adapter architecture (the console calls the repositories directly).

## Overall

**This is a pass, with improvements to make.**

At first glance this looks thinner than it is, because the application lives on a branch that was
never merged. Once you go there, the work is mostly there. The SQL scripts are complete and run
cleanly. Every one of the nine requirements has a repository method, the result types are proper
classes rather than raw maps, and Req 9 - the hardest query in the assignment - is done well, in
a single SQL statement.

But two requirements don't actually work. The name search can never find a partial match because
it's missing its `%` wildcards, and the update never updates anything because the interface and
the implementation disagree on the order of their parameters. Both show up in your own console
output (`[]` and `false`), so running the demo and reading what it printed would have caught them.
On top of that, the interfaces exist but nothing uses them, the documentation is on the
implementations rather than the interfaces, and the finished work isn't on `main`.

For the next assignment: merge your work into `main` before you submit - only `main` will be
marked from now on - and depend on interfaces rather
than classes, use full parameter names, and read your console output critically before you submit
- if a step prints `[]` or `false`, find out why.
